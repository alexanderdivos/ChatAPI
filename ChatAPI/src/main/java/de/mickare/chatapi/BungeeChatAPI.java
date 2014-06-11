package de.mickare.chatapi;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeChatAPI {
	
	public static IChatBuilder<ProxiedPlayer> newBuilder() {
		return new Chat_BungeeCord().newBuilder();

	}
	
	public static IChatBBCodeBuilder<ProxiedPlayer> newBBCodeBuilder() {
		return new Chat_BungeeCord().newBBCodeBuilder();

	}
	
}
