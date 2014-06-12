package de.mickare.chatapi.api;

public interface IChatBBCodeBuilder<P> extends IChatBuilder<P> {

	public IChatBBCodeBuilder<P> appendBBCode(String message);

	public boolean isPlain();

	public void setPlain( boolean plain );
	
}
