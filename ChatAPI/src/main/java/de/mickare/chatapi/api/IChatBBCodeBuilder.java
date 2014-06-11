package de.mickare.chatapi.api;

public interface IChatBBCodeBuilder<P> extends IChatBuilder<P> {

	public IChatBBCodeBuilder<P> appendBBCode(String message);
	
}
