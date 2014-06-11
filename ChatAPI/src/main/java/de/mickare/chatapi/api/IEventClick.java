package de.mickare.chatapi.api;

import de.mickare.chatapi.api.action.ActionClick;

public interface IEventClick extends IEvent {

	public ActionClick getAction();
	
	public String getValue();
	
}
