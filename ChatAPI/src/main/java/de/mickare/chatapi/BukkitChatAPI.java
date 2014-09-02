package de.mickare.chatapi;

import org.bukkit.entity.Player;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;

public class BukkitChatAPI {

	private static enum Version {
		v1_7_3
	}

	private static Version v = null;

	private static Version getVersion() {
		if (v == null) {
			try {
				Class.forName( "net.minecraft.server.v1_7_R3.IChatBaseComponent", false, BukkitChatAPI.class.getClassLoader() );
				v = Version.v1_7_3;
			} catch (ClassNotFoundException e) {
				throw new UnsupportedOperationException( e );
			}
		}
		return v;
	}

	public static IChatBuilder<Player> newBuilder() {
		switch (getVersion()) {
		case v1_7_3:
			return new Chat_Bukkit_v1_7_4().newBuilder();
		default:
			throw new UnsupportedOperationException();
		}

	}
	
	public static IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		switch (getVersion()) {
		case v1_7_3:
			return new Chat_Bukkit_v1_7_4().newBBCodeBuilder();
		default:
			throw new UnsupportedOperationException();
		}

	}

}
