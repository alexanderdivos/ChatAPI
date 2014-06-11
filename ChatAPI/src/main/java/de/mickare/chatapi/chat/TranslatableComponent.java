package de.mickare.chatapi.chat;

import java.util.LinkedList;
import java.util.List;

import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IComponentTranslate;

public class TranslatableComponent extends ComponentChat implements IComponentTranslate {

	private String translate = "";
	private List<IComponentChat> with = new LinkedList<>();

	public TranslatableComponent(String translate, List<IComponentChat> with) {
		this.translate = translate;
		this.with.addAll( with );		
	}
	
	public TranslatableComponent(String translate, List<IComponentChat> with, IComponentChat parent) {
		super(parent);
		this.translate = translate;
		this.with.addAll( with );
	}

	@Override
	public String getTranslate() {
		return translate;
	}

	public List<IComponentChat> getWith() {
		return new LinkedList<>( with );
	}

	public void setWith( List<ComponentChat> with ) {
		this.with = new LinkedList<>();
		this.with.addAll( with );
	}

	public void addWith( ComponentChat... with ) {
		for (ComponentChat c : with) {
			this.with.add( c );
		}
	}

	public void clearWith() {
		this.with = new LinkedList<>();
	}

	@Override
	public IComponentChat clone( IComponentChat parent ) {
		IComponentChat c = new TranslatableComponent(translate, with, parent);
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
		sb.append( translate ).append( "(" );
		boolean first = true;
		for(IComponentChat icc : this.getWith()) {
			if(first) {
				first = false;
			} else {
				sb.append( ", " );
			}
			sb.append( icc.toString() );
		}
		sb.append( " )" );
		return sb.toString();
	}

}
