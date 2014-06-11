package de.mickare.chatapi;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import net.minecraft.util.com.google.common.base.Preconditions;
import net.minecraft.util.com.google.common.collect.ImmutableList;

import de.mickare.chatapi.api.*;
import de.mickare.chatapi.api.action.ActionClick;
import de.mickare.chatapi.chat.ChatModifierDifference;
import de.mickare.chatapi.chat.ComponentText;
import de.mickare.chatapi.chat.ModifierTree;
import de.mickare.chatapi.chat.TranslatableComponent;
import de.mickare.chatapi.chat.event.ClickEvent;

public class ChatBuilder<P> implements IChatBuilder<P> {

	private static final Pattern COLOR_PATTERN = Pattern.compile(
			"(" + String.valueOf( org.bukkit.ChatColor.COLOR_CHAR ) + "[0-9a-fk-or])", Pattern.CASE_INSENSITIVE );
	private static final Pattern URL_PATTERN = Pattern.compile( "(?:(https?://[^ ][^ ]*?)(?=[\\.\\?!,;:]?(?:[ \\n]|$)))",
			Pattern.CASE_INSENSITIVE );

	/*
	 * public static IChatBuilder builder() { return new ChatBuilder(); }
	 */
	private boolean oneliner = false;

	private final IChatMessageFactory<P> messageFactory;
	private final LinkedList<IComponentChat> components = new LinkedList<IComponentChat>();

	private final ModifierTree rootModifierTree = new ModifierTree();
	private ModifierTree currentModifierTree = rootModifierTree;

	public ChatBuilder(IChatMessageFactory<P> messageFactory) {
		this.messageFactory = messageFactory;
	}

	public IChatMessageFactory<P> getMessageFactory() {
		return messageFactory;
	}

	@Override
	public List<IComponentChat> getComponents() {
		return ImmutableList.copyOf( this.components );
	}

	@Override
	public IChatMessage<P> build() {
		if (this.components.isEmpty()) {
			return this.messageFactory.createMessage( Lists.<IComponentChat> newArrayList( ComponentText.create( "" ) ) );
		}
		ListIterator<IComponentChat> li = this.components.listIterator();
		while (li.hasNext()) {
			IComponentChat c = li.next();
			if (c == null) {
				li.set( ComponentText.create( "" ) );
			}
		}
		return this.messageFactory.createMessage( this.components );
	}

	@Override
	public ChatBuilder<P> newLine() {
		IComponentChat c = ComponentText.create( "" );
		this.components.add( c );
		this.currentModifierTree.clearComponents( c );
		return this;
	}

	@Override
	public ChatBuilder<P> append( ChatColor color ) {
		Preconditions.checkNotNull( color );
		this.currentModifierTree.add( color );
		return this;
	}

	@Override
	public ChatBuilder<P> openColor( ChatColor color ) {
		Preconditions.checkNotNull( color );
		this.currentModifierTree = this.currentModifierTree.open( color );
		return this;
	}

	@Override
	public ChatBuilder<P> closeColor() {
		this.currentModifierTree = this.currentModifierTree.closeColor();
		return this;
	}

	@Override
	public ChatBuilder<P> closeColor( ChatColor color ) {
		Preconditions.checkNotNull( color );
		this.currentModifierTree = this.currentModifierTree.close( color );
		return this;
	}

	@Override
	public ChatBuilder<P> append( IComponentChat c ) {
		Preconditions.checkNotNull( c );
		if (this.components.isEmpty()) {
			this.newLine();
		}

		// ListIterator<IComponentChat> li = this.components.listIterator( this.components.size() - 1 );

		if (c.getColorRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( c.getColorRaw() );
		}
		if (c.isBoldRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( ChatColor.BOLD );
		}
		if (c.isItalicRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( ChatColor.ITALIC );
		}
		if (c.isUnderlinedRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( ChatColor.UNDERLINE );
		}
		if (c.isStrikethroughRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( ChatColor.STRIKETHROUGH );
		}
		if (c.isObfuscatedRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.open( ChatColor.MAGIC );
		}
		if (c.getClickEvent() != null) {
			this.currentModifierTree = this.currentModifierTree.open( c.getClickEvent() );
		}
		if (c.getHoverEvent() != null) {
			this.currentModifierTree = this.currentModifierTree.open( c.getHoverEvent() );
		}

		this.currentModifierTree.addComponent( c );

		if (c.getHoverEvent() != null) {
			this.currentModifierTree = this.currentModifierTree.close( c.getHoverEvent() );
		}
		if (c.getClickEvent() != null) {
			this.currentModifierTree = this.currentModifierTree.close( c.getClickEvent() );
		}
		if (c.isObfuscatedRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( ChatColor.MAGIC );
		}
		if (c.isStrikethroughRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( ChatColor.STRIKETHROUGH );
		}
		if (c.isUnderlinedRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( ChatColor.UNDERLINE );
		}
		if (c.isItalicRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( ChatColor.ITALIC );
		}
		if (c.isBoldRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( ChatColor.BOLD );
		}
		if (c.getColorRaw() != null) {
			this.currentModifierTree = this.currentModifierTree.close( c.getColorRaw() );
		}

		return this;
	}

	@Override
	public ChatBuilder<P> appendAndFormat( IComponentChat c ) {
		Preconditions.checkNotNull( c );
		if (this.components.isEmpty()) {
			this.newLine();
		}

		this.currentModifierTree.addComponent( c );

		return this;
	}

	@Override
	public ChatBuilder<P> append( String text ) {
		if (text == null || text.isEmpty()) {
			return this;
		}

		final Matcher matcher = URL_PATTERN.matcher( text );
		int last_MatchEnd = 0;

		while (matcher.find()) {
			if (last_MatchEnd < matcher.start()) {
				this.appendText( text.substring( last_MatchEnd, matcher.start() ) );
			}
			last_MatchEnd = matcher.end();
			this.appendURL( matcher.group() );
		}
		if (last_MatchEnd < text.length()) {
			this.appendText( text.substring( last_MatchEnd, text.length() ) );
		}

		return this;
	}

	@Override
	public ChatBuilder<P> appendText( final String text ) {
		if (text == null || text.isEmpty()) {
			return this;
		}

		String[] lines;
		if (!oneliner) {
			lines = text.split( "\n" );
		} else {
			lines = new String[] { text };
		}

		int index = 0;
		do {
			if (index > 0 || this.components.isEmpty()) {
				this.newLine();
			}
			// final ListIterator<IComponentChat> li = this.components.listIterator();
			// IComponentChat parent = this.components.getLast();
			final String line = lines[index];
			int last_MatchEnd = 0;

			if (line.trim().length() > 0) {
				final Matcher matcher = COLOR_PATTERN.matcher( line );
				while (matcher.find()) {
					if (last_MatchEnd < matcher.start()) {
						this.currentModifierTree.addComponent( ComponentText.create( line.substring( last_MatchEnd, matcher.start() ) ) );
					}
					last_MatchEnd = matcher.end();
					this.currentModifierTree.add( ChatColor.getByChar( matcher.group().toLowerCase().charAt( 1 ) ) );
				}
			}

			if (last_MatchEnd < line.length()) {
				this.currentModifierTree.addComponent( ComponentText.create( line.substring( last_MatchEnd, line.length() ) ) );
			}

			index++;
		} while (index < lines.length);
		return this;
	}

	@Override
	public ChatBuilder<P> appendURL( String url ) {
		if (url == null || url.isEmpty()) {
			return this;
		}
		return appendURL( url, url );
	}

	@Override
	public ChatBuilder<P> appendURL( String url, String text ) {
		if ((url == null || url.isEmpty()) || (text == null || text.isEmpty())) {
			return appendText( text );
		}
		if (this.components.isEmpty()) {
			this.newLine();
		}
		this.openClick( new ClickEvent( ActionClick.OPEN_URL, url ) );
		this.currentModifierTree.addComponent( ComponentText.create( text ) );
		this.closeClick();
		return this;
	}

	@Override
	public ChatBuilder<P> appendTranslation( String translate ) {
		Preconditions.checkNotNull( translate );
		return appendTranslation( translate, Collections.<IComponentChat> emptyList() );
	}

	@Override
	public ChatBuilder<P> appendTranslation( String translate, List<IComponentChat> with ) {
		Preconditions.checkNotNull( translate );
		Preconditions.checkNotNull( with );

		if (this.components.isEmpty()) {
			this.newLine();
		}

		this.currentModifierTree.addComponent( new TranslatableComponent( translate, with ) );

		return this;
	}

	@Override
	public ChatBuilder<P> openClick( IEventClick event ) {
		Preconditions.checkNotNull( event );
		this.currentModifierTree = this.currentModifierTree.open( event );
		return this;
	}

	@Override
	public ChatBuilder<P> closeClick() {
		this.currentModifierTree = this.currentModifierTree.closeClick();
		return this;
	}

	@Override
	public ChatBuilder<P> closeClick( IEventClick event ) {
		Preconditions.checkNotNull( event );
		this.currentModifierTree = this.currentModifierTree.close( event );
		return this;
	}

	@Override
	public ChatBuilder<P> openHover( IEventHover event ) {
		Preconditions.checkNotNull( event );
		this.currentModifierTree = this.currentModifierTree.open( event );
		return this;
	}

	@Override
	public ChatBuilder<P> closeHover() {
		this.currentModifierTree = this.currentModifierTree.closeHover();
		return this;
	}

	@Override
	public ChatBuilder<P> closeHover( IEventHover event ) {
		Preconditions.checkNotNull( event );
		this.currentModifierTree = this.currentModifierTree.close( event );
		return this;
	}

	@Override
	public boolean isOneLiner() {
		return oneliner;
	}

	@Override
	public ChatBuilder<P> setOneLiner( boolean oneliner ) {
		this.oneliner = oneliner;
		return this;
	}

	@Override
	public IEventClick getClick() {
		ChatModifierDifference m = this.currentModifierTree.getFormatDifference( this.rootModifierTree );
		return m.getClickEvent();
	}

	@Override
	public IEventHover getHover() {
		ChatModifierDifference m = this.currentModifierTree.getFormatDifference( this.rootModifierTree );
		return m.getHoverEvent();
	}

}
