package de.mickare.chatapi.chat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.Verify;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IEventClick;
import de.mickare.chatapi.api.IEventHover;

public abstract class ComponentChat implements IComponentChat {

	private final IComponentChat parent;

	private Boolean bold = null;
	private Boolean italic = null;
	private Boolean underlined = null;
	private Boolean strikethrough = null;
	private Boolean obfuscated = null;

	private ChatColor color = null;

	private IEventClick clickEvent = null;
	private IEventHover hoverEvent = null;

	private final List<IComponentChat> extra = new LinkedList<>();

	protected ComponentChat() {
		this(null);
	}

	protected ComponentChat(final IComponentChat parent) {
		this.parent = parent;
	}

	public final void copyFormat( final IComponentChat c ) {
		this.bold = c.isBoldRaw();
		this.italic = c.isItalicRaw();
		this.underlined = c.isUnderlinedRaw();
		this.strikethrough = c.isStrikethroughRaw();
		this.obfuscated = c.isObfuscatedRaw();
		this.color = c.getColorRaw();
		this.clickEvent = c.getClickEvent();
		this.hoverEvent = c.getHoverEvent();
	}

	public final Boolean isBoldRaw() {
		return bold;
	}

	public final Boolean isBold() {
		if (bold == null) {
			return parent != null ? parent.isBold() : null;
		}
		return bold;
	}

	public final Boolean isItalicRaw() {
		return italic;
	}

	public final Boolean isItalic() {
		if (italic == null) {
			return parent != null ? parent.isItalic() : null;
		}
		return italic;
	}

	public final Boolean isUnderlinedRaw() {
		return underlined;
	}

	public final Boolean isUnderlined() {
		if (underlined == null) {
			return parent != null ? parent.isUnderlined()  : null;
		}
		return underlined;
	}

	public final Boolean isStrikethroughRaw() {
		return strikethrough;
	}

	public final Boolean isStrikethrough() {
		if (strikethrough == null) {
			return parent != null ? parent.isStrikethrough() : null;
		}
		return strikethrough;
	}

	public final Boolean isObfuscatedRaw() {
		return obfuscated;
	}

	public final Boolean isObfuscated() {
		if (obfuscated == null) {
			return parent != null  ? parent.isObfuscated() : null;
		}
		return obfuscated;
	}

	public final ChatColor getColorRaw() {
		return color;
	}

	public final ChatColor getColor() {
		if (color == null) {
			if (parent == null) {
				return ChatColor.WHITE;
			}
			return parent.getColor();
		}
		return color;
	}

	public final boolean hasFormatting() {
		return color != null || bold != null || italic != null || underlined != null || strikethrough != null || obfuscated != null
				|| hoverEvent != null || clickEvent != null;
	}

	public final IEventClick getClickEvent() {
		return clickEvent;
	}

	public final IEventHover getHoverEvent() {
		return hoverEvent;
	}

	public final void setBold( final Boolean bold ) {
		this.bold = bold;
	}

	public final void setItalic( final Boolean italic ) {
		this.italic = italic;
	}

	public final void setUnderlined( final Boolean underlined ) {
		this.underlined = underlined;
	}

	public final void setStrikethrough( final Boolean strikethrough ) {
		this.strikethrough = strikethrough;
	}

	public final void setObfuscated( final Boolean obfuscated ) {
		this.obfuscated = obfuscated;
	}

	public final void setColor( final ChatColor color ) {
		this.color = color;
	}

	public final void setClickEvent( final IEventClick clickEvent ) {
		this.clickEvent = clickEvent;
	}

	public final void setHoverEvent( final IEventHover hoverEvent ) {
		this.hoverEvent = hoverEvent;
	}

	public final IComponentChat getParent() {
		return parent;
	}

	public final List<IComponentChat> getExtra() {
		return (this.extra != null ? new LinkedList<IComponentChat>( extra ) : new LinkedList<IComponentChat>());
	}
	@Override
	public final void clearExtra() {
		extra.clear();
	}
	
	@Override
	public final void addAll( final Collection<IComponentChat> o ) {
		for(IComponentChat e : o) {
			add(e);
		}
	}

	@Override
	public final IComponentChat add( final IComponentChat o ) {
		Verify.checkNotNull( o );
		IComponentChat cl = o.clone( this );
		this.extra.add( cl );
		return cl;
	}

	@Override
	public final boolean remove( final IComponentChat o ) {
		Verify.checkNotNull( o );
		return this.extra.remove( o );
	}

	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.toString() );
		for(IComponentChat c : this.getExtra()) {
			sb.append( c.toString() );
		}
		return sb.toString();
	}

	@Override
	public abstract String toString();

}
