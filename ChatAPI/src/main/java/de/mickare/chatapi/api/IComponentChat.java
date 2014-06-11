package de.mickare.chatapi.api;

import java.util.Collection;
import java.util.List;

import de.mickare.chatapi.ChatColor;

public interface IComponentChat {

	public List<IComponentChat> getExtra();
	public void clearExtra();
	public void addAll( Collection<IComponentChat> o );
	public IComponentChat add( IComponentChat c );
	public boolean remove( IComponentChat c );
	
	public void copyFormat( IComponentChat c );

	public Boolean isBoldRaw();
	public Boolean isItalicRaw();
	public Boolean isUnderlinedRaw();
	public Boolean isStrikethroughRaw();
	public Boolean isObfuscatedRaw();
	public ChatColor getColorRaw();
	
	public Boolean isBold();
	public Boolean isItalic();
	public Boolean isUnderlined();
	public Boolean isStrikethrough();
	public Boolean isObfuscated();
	public ChatColor getColor();

	public boolean hasFormatting();

	public IEventClick getClickEvent();
	public IEventHover getHoverEvent();

	public void setBold( Boolean bold );
	public void setItalic( Boolean italic );
	public void setUnderlined( Boolean underlined );
	public void setStrikethrough( Boolean strikethrough );
	public void setObfuscated( Boolean obfuscated );
	public void setColor( ChatColor color );

	public void setClickEvent( IEventClick clickEvent );
	public void setHoverEvent( IEventHover hoverEvent );

	public IComponentChat getParent();
	public IComponentChat clone(IComponentChat parent);
	
	public String toString();
	public String getString();
	

}
