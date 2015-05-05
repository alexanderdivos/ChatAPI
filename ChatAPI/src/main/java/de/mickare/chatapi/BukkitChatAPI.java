package de.mickare.chatapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;

public class BukkitChatAPI {
	
	private static enum Version {
		protocolLib,
		spigot
	}
	
	private static Version v = null;
	
	private static Version getVersion() {
		if ( v == null ) {
			// Standard Spigot
			try {
				Class.forName( "org.spigotmc.SpigotConfig", false, BukkitChatAPI.class.getClassLoader() );
				v = Version.spigot;
				return v;
			} catch ( ClassNotFoundException e ) {
			}
			// ProtocolLib ftw!
			if ( Bukkit.getPluginManager().isPluginEnabled( "ProtocolLib" ) ) {
				v = Version.protocolLib;
				return v;
			}
			
			if ( v == null ) {
				throw new UnsupportedOperationException( "No supported ServerVersions for BukkitChatAPI" );
			}
		}
		return v;
	}
	
	public static IChatBuilder<Player> newBuilder() {
		switch ( getVersion() ) {
			case spigot:
				return new Chat_Spigot().newBuilder();
			case protocolLib:
				return new Chat_ProtocolLib().newBuilder();
			default:
				throw new UnsupportedOperationException();
		}
		
	}
	
	public static IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		switch ( getVersion() ) {
			case spigot:
				return new Chat_Spigot().newBBCodeBuilder();
			case protocolLib:
				return new Chat_ProtocolLib().newBBCodeBuilder();
			default:
				throw new UnsupportedOperationException();
		}
		
	}
	
}
