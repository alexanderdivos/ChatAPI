package de.mickare.chatapi.chat.event;

import com.google.common.base.Preconditions;

import de.mickare.chatapi.api.IEventClick;
import de.mickare.chatapi.api.action.ActionClick;

final public class ClickEvent implements IEventClick {

	private final ActionClick action;

	private final String value;

	public ClickEvent(final ActionClick action, final String value) {
		Preconditions.checkNotNull( action );
		Preconditions.checkNotNull( value );
		this.action = action;
		this.value = value;
	}

	public ActionClick getAction() {
		return action;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("ClickEvent{action=%s, value=%s}", action, value);
	}
	
	
}
