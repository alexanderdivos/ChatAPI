package de.mickare.chatapi.chat.event;

import de.mickare.chatapi.Verify;
import de.mickare.chatapi.api.IComponentChat;
import de.mickare.chatapi.api.IEventHover;
import de.mickare.chatapi.api.action.ActionHover;
import de.mickare.chatapi.chat.ComponentText;

final public class HoverEvent implements IEventHover {

	private final ActionHover action;
	private final IComponentChat value;

	public static HoverEvent HoverEvent_showText( String value ) {
		return new HoverEvent( ActionHover.SHOW_TEXT, ComponentText.create( value ) );
	}

	public static HoverEvent HoverEvent_showText( IComponentChat value ) {
		return new HoverEvent( ActionHover.SHOW_TEXT, value );
	}

	public static HoverEvent HoverEvent_achievement( String value ) {
		return new HoverEvent( ActionHover.SHOW_ACHIEVEMENT, ComponentText.create( value ) );
	}

	public static HoverEvent HoverEvent_item( String value ) {
		return new HoverEvent( ActionHover.SHOW_ITEM, ComponentText.create( value ) );
	}

	public HoverEvent(final ActionHover action, final IComponentChat value) {
		Verify.checkNotNull( action );
		Verify.checkNotNull( value );
		this.action = action;
		this.value = value;
	}

	public ActionHover getAction() {
		return action;
	}

	public IComponentChat getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format( "HoverEvent{action=%s, value=%s}", action, value.toString() );
	}

}