package de.mickare.chatapi.api;

public interface IChatMessage<P> {

	public void sendToPlayer( P player );

	public void sendToConsole();
	
	public String toString();

}
