package de.mickare.chatapi.api;

import java.util.List;

import de.mickare.chatapi.ChatBuilder;
import de.mickare.chatapi.ChatColor;


public interface IChatBuilder<P> {
	
	public IChatMessage<P> build();
	
	public List<IComponentChat> getComponents();
	
	public IChatBuilder<P> newLine();

	public IChatBuilder<P> append(ChatColor color);
	public IChatBuilder<P> openColor(ChatColor color);
	public IChatBuilder<P> closeColor();
	public IChatBuilder<P> closeColor(ChatColor color);
	
	public IChatBuilder<P> append(IComponentChat c);
	public ChatBuilder<P> appendAndFormat( IComponentChat c );
	
	public IChatBuilder<P> append(String text);
	public IChatBuilder<P> appendText( String text );
	public IChatBuilder<P> appendURL(String url);
	public IChatBuilder<P> appendURL(String url, String text);
	public IChatBuilder<P> appendTranslation(String translate, List<IComponentChat> with);
	public IChatBuilder<P> appendTranslation( String translate );

	public IChatBuilder<P> openClick(IEventClick event);
	public IChatBuilder<P> closeClick();
	public ChatBuilder<P> closeClick( IEventClick event );
	public IChatBuilder<P> openHover(IEventHover event);
	public IChatBuilder<P> closeHover();
	public ChatBuilder<P> closeHover( IEventHover event );

	public IEventClick getClick();
	public IEventHover getHover();

	public boolean isOneLiner();
	public ChatBuilder<P> setOneLiner( boolean oneliner );

}
