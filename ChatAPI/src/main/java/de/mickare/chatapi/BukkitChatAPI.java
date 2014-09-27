package de.mickare.chatapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;

public class BukkitChatAPI {

	private static enum Version {
		protocolLib, bukkit_v1_7_4
	}

	private static Version v = null;

	private static Version getVersion() {
		if (v == null) {
			// ProtocolLib ftw!
			if(Bukkit.getPluginManager().isPluginEnabled( "ProtocolLib" ))  {
				v = Version.protocolLib;
				return v;
			}
			// Standard CraftBukkit
			try {
				Class.forName( "net.minecraft.server.v1_7_R4.IChatBaseComponent", false, BukkitChatAPI.class.getClassLoader() );
				v = Version.bukkit_v1_7_4;
			} catch (ClassNotFoundException e) {
				throw new UnsupportedOperationException( e );
			}
		}
		return v;
	}

	public static IChatBuilder<Player> newBuilder() {
		switch (getVersion()) {
		case bukkit_v1_7_4:
			return new Chat_Bukkit_v1_7_4().newBuilder();
		case protocolLib:
			return new Chat_ProtocolLib().newBuilder();
		default:
			throw new UnsupportedOperationException();
		}

	}

	public static IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		switch (getVersion()) {
		case bukkit_v1_7_4:
			return new Chat_Bukkit_v1_7_4().newBBCodeBuilder();
		default:
			throw new UnsupportedOperationException();
		}

	}

}
