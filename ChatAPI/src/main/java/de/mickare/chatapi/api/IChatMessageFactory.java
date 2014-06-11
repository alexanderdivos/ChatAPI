package de.mickare.chatapi.api;

import java.util.List;


public interface IChatMessageFactory<P> {

	public IChatMessage<P> createMessage( final List<IComponentChat> lines );
	
	public IChatBuilder<P> newBuilder();

	IChatBBCodeBuilder<P> newBBCodeBuilder();
	
}
