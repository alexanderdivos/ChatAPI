package de.mickare.chatapi.chat;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.mickare.chatapi.api.IChatMessage;
import de.mickare.chatapi.api.IComponentChat;

public abstract class ChatMessage<P> implements IChatMessage<P> {

	private final List<IComponentChat> lines;
	private String toString = null;

	public ChatMessage(List<IComponentChat> lines) {
		this.lines = ImmutableList.copyOf( lines);
	}

	public String toString() {
		if (this.toString == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (IComponentChat c : lines) {
				if (first) {
					first = false;
				} else {
					sb.append( "\n" );
				}
				sb.append( c.getString() );
			}
			this.toString = sb.toString();
		}
		return toString;
	}

	protected List<IComponentChat> getComponents() {
		return lines;
	}

}
