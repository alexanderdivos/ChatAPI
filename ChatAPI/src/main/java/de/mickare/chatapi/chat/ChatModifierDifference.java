package de.mickare.chatapi.chat;

import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IEventClick;
import de.mickare.chatapi.api.IEventHover;

public final class ChatModifierDifference {

	private ChatColor color = null;

	private Boolean bold = null;
	private Boolean italic = null;
	private Boolean underlined = null;
	private Boolean strikethrough = null;
	private Boolean obfuscated = null;

	private IEventClick clickEvent = null;
	private IEventHover hoverEvent = null;

	public final ChatColor getColor() {
		return color;
	}

	public final Boolean getBold() {
		return bold;
	}

	public final Boolean getItalic() {
		return italic;
	}

	public final Boolean getUnderlined() {
		return underlined;
	}

	public final Boolean getStrikethrough() {
		return strikethrough;
	}

	public final Boolean getObfuscated() {
		return obfuscated;
	}

	public final IEventClick getClickEvent() {
		return clickEvent;
	}

	public final IEventHover getHoverEvent() {
		return hoverEvent;
	}

	public final void setColor( final ChatColor color ) {
		if (color == null) {
			return;
		}
		if (color.isFormat()) {
			if (color == ChatColor.RESET) {
				this.color = color;
				this.bold = false;
				this.italic = false;
				this.underlined = false;
				this.strikethrough = false;
				this.obfuscated = false;
			} else {
				switch (color) {
				case BOLD:
					this.bold = true;
					break;
				case ITALIC:
					this.italic = true;
					break;
				case UNDERLINE:
					this.underlined = true;
					break;
				case STRIKETHROUGH:
					this.strikethrough = true;
					break;
				case MAGIC:
					this.obfuscated = true;
					break;
				default:
					throw new IllegalStateException();
				}
			}
		} else {
			this.color = color;
		}
	}

	public final void setColorClassic( final ChatColor color ) {
		if (color == null) {
			return;
		}
		this.setColor( color );
		if (!color.isFormat()) {
			this.bold = null;
			this.italic = null;
			this.underlined = null;
			this.strikethrough = null;
			this.obfuscated = null;
		}
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

	public final void setClickEvent( final IEventClick clickEvent ) {
		this.clickEvent = clickEvent;
	}

	public final void setHoverEvent( final IEventHover hoverEvent ) {
		this.hoverEvent = hoverEvent;
	}

	public IComponentChat format( IComponentChat c ) {
		c.setColor( this.getColor() );
		c.setBold( this.getBold() );
		c.setItalic( this.getItalic() );
		c.setUnderlined( this.getUnderlined() );
		c.setStrikethrough( this.getStrikethrough() );
		c.setObfuscated( this.getObfuscated() );

		c.setClickEvent( this.getClickEvent() );
		c.setHoverEvent( this.getHoverEvent() );
		return c;
	}
}
