package de.mickare.chatapi.api;

import de.mickare.chatapi.api.action.ActionHover;

public interface IEventHover extends IEvent {

	public ActionHover getAction();
	
	public IComponentChat getValue();
	
}
