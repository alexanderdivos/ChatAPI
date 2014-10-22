package de.mickare.chatapi;

import org.bukkit.entity.Player;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;

public class SpigotChatAPI {
	
	public static IChatBuilder<Player> newBuilder() {
		return new Chat_Spigot().newBuilder();

	}
	
	public static IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		return new Chat_Spigot().newBBCodeBuilder();

	}
	
}
