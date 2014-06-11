package de.mickare.chatapi;

import java.util.LinkedList;
import java.util.List;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import de.mickare.chatapi.api.*;
import de.mickare.chatapi.api.action.*;
import de.mickare.chatapi.chat.ChatMessage;

public final class Chat_BungeeCord implements IChatMessageFactory<net.md_5.bungee.api.connection.ProxiedPlayer> {

	public static final net.md_5.bungee.api.ChatColor convert( final ChatColor c ) {
		return net.md_5.bungee.api.ChatColor.getByChar( c.getCode() );
	}

	public static final net.md_5.bungee.api.chat.ClickEvent.Action convert( final ActionClick a ) {
		if (a == ActionClick.OPEN_FILE) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_FILE;
		} else if (a == ActionClick.OPEN_URL) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
		} else if (a == ActionClick.SUGGEST_COMMAND) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND;
		} else if (a == ActionClick.RUN_COMMAND) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.md_5.bungee.api.chat.ClickEvent convert( final IEventClick e ) {
		if (e == null) {
			return null;
		}
		return new net.md_5.bungee.api.chat.ClickEvent( convert( e.getAction() ), e.getValue() );
	}

	public static final net.md_5.bungee.api.chat.HoverEvent.Action convert( final ActionHover a ) {
		if (a == ActionHover.SHOW_ACHIEVEMENT) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ACHIEVEMENT;
		} else if (a == ActionHover.SHOW_ITEM) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM;
		} else if (a == ActionHover.SHOW_TEXT) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.md_5.bungee.api.chat.HoverEvent convert( final IEventHover e ) {
		if (e == null) {
			return null;
		}
		return new net.md_5.bungee.api.chat.HoverEvent( convert( e.getAction() ), convert( e.getValue() ).toArray(
				new net.md_5.bungee.api.chat.BaseComponent[0] ) );
	}

	public static final net.md_5.bungee.api.chat.BaseComponent[] convert( final IComponentChat[] v ) {
		final List<net.md_5.bungee.api.chat.BaseComponent> result = new LinkedList<>();
		for (int i = 0; i < v.length; i++) {
			result.addAll( convert( v[i] ) );
		}
		return result.toArray( new net.md_5.bungee.api.chat.BaseComponent[0] );
	}

	public static final List<net.md_5.bungee.api.chat.BaseComponent> convert( final List<IComponentChat> v ) {
		final List<net.md_5.bungee.api.chat.BaseComponent> result = new LinkedList<>();
		for (IComponentChat c : v) {
			result.addAll( convert( c ) );
		}
		return result;
	}

	public static final List<net.md_5.bungee.api.chat.BaseComponent> convert( final IComponentChat c ) {
		List<net.md_5.bungee.api.chat.BaseComponent> result = new LinkedList<>();

		net.md_5.bungee.api.chat.BaseComponent parent;
		if (c instanceof IComponentText) {
			parent = _convert( (IComponentText) c );
		} else if (c instanceof IComponentTranslate) {
			parent = _convert( (IComponentTranslate) c );
		} else {
			throw new IllegalStateException();
		}

		parent.setColor( convert( c.getColorRaw() ) );
		parent.setBold( c.isBoldRaw() );
		parent.setItalic( c.isItalicRaw() );
		parent.setUnderlined( c.isUnderlined() );
		parent.setStrikethrough( c.isStrikethroughRaw() );
		parent.setObfuscated( c.isObfuscatedRaw() );
		parent.setClickEvent( convert( c.getClickEvent() ) );
		parent.setHoverEvent( convert( c.getHoverEvent() ) );

		result.add( parent );

		for (IComponentChat v : c.getExtra()) {
			result.addAll( convert( v ) );
		}

		return result;
	}

	private static final net.md_5.bungee.api.chat.BaseComponent _convert( final IComponentText c ) {
		return new net.md_5.bungee.api.chat.TextComponent( c.getText() );
	}

	private static final net.md_5.bungee.api.chat.BaseComponent _convert( final IComponentTranslate c ) {
		net.md_5.bungee.api.chat.TranslatableComponent t = new net.md_5.bungee.api.chat.TranslatableComponent();
		t.setTranslate( c.getTranslate() );
		t.setWith( convert( c.getWith() ) );
		return t;
	}

	public final BungeecordChatMessage createMessage( final List<IComponentChat> lines ) {
		return new BungeecordChatMessage( lines );
	}

	private static final class BungeecordChatMessage extends
			ChatMessage<net.md_5.bungee.api.connection.ProxiedPlayer> {

		private List<net.md_5.bungee.api.chat.BaseComponent[]> converted = null;

		public BungeecordChatMessage(final List<IComponentChat> lines) {
			super( lines );
		}

		private final List<net.md_5.bungee.api.chat.BaseComponent[]> getConverted() {
			if (converted == null) {
				converted = new LinkedList<>();
				for (final IComponentChat c : this.getComponents()) {
					converted.add( convert( c ).toArray( new net.md_5.bungee.api.chat.BaseComponent[0] ) );
				}
			}
			return converted;
		}

		@Override
		public final void sendToPlayer( final net.md_5.bungee.api.connection.ProxiedPlayer player ) {
			for (final net.md_5.bungee.api.chat.BaseComponent[] c : getConverted()) {
				player.sendMessage( c );
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public final void sendToConsole( ) {
			ProxyServer.getInstance().getConsole().sendMessage( toString() );
		}

	}

	@Override
	public IChatBuilder<ProxiedPlayer> newBuilder() {
		return new ChatBuilder<ProxiedPlayer>( this );
	}

	@Override
	public IChatBBCodeBuilder<ProxiedPlayer> newBBCodeBuilder() {
		return new BBCodeChatBuilder<ProxiedPlayer>( this );
	}

	
	
}
