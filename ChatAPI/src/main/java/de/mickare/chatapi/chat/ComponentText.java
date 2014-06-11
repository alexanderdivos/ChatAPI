package de.mickare.chatapi.chat;

import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IComponentText;

public final class ComponentText extends ComponentChat implements IComponentText {

	public static final ComponentText create( final String text ) {
		if (text == null) {
			return new ComponentText( "" );
		}
		/*
		 * final String[] spl = text.split( "\n" ); final ComponentText[] result = new ComponentText[spl.length];
		 * for(int i = 0; i < spl.length; i++) { result[i] = new ComponentText(spl[i]); }
		 */
		return new ComponentText( text );
	}

	public static final ComponentText create( final String text, final IComponentChat parent ) {
		if (text == null) {
			return new ComponentText( "" );
		}
		/*
		 * final String[] spl = text.split( "\n" ); final ComponentText[] result = new ComponentText[spl.length];
		 * for(int i = 0; i < spl.length; i++) { result[i] = new ComponentText(spl[i]); }
		 */
		return new ComponentText( text, parent );
	}

	private final String text;

	private ComponentText(final String text) {
		this.text = text;
	}

	private ComponentText(final String text, final IComponentChat parent) {
		super( parent );
		this.text = text;
	}

	public final String getText() {
		return text;
	}

	@Override
	public final IComponentChat clone( final IComponentChat parent ) {
		IComponentChat c = new ComponentText( text, parent );
		c.copyFormat( this );
		return c;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ChatColor c = this.getColor();
		sb.append( c != null ? c : ChatColor.RESET );
		if (this.isBold()) {
			sb.append( ChatColor.BOLD );
		} else if (this.isItalic()) {
			sb.append( ChatColor.ITALIC );
		} else if (this.isUnderlined()) {
			sb.append( ChatColor.UNDERLINE );
		} else if (this.isStrikethrough()) {
			sb.append( ChatColor.STRIKETHROUGH );
		} else if (this.isObfuscated()) {
			sb.append( ChatColor.MAGIC );
		}
		sb.append( text );
		return sb.toString();
	}

}
