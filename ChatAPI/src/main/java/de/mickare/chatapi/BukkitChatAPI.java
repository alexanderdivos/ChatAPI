package de.mickare.chatapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mickare.chatapi.api.IChatBBCodeBuilder;
import de.mickare.chatapi.api.IChatBuilder;

public class BukkitChatAPI {
	
	private static enum Version {
		protocolLib,
		bukkit_v1_7_4,
		spigot
	}
	
	private static Version v = null;
	
	private static Version getVersion() {
		if ( v == null ) {
			// ProtocolLib ftw!
			if ( Bukkit.getPluginManager().isPluginEnabled( "ProtocolLib" ) ) {
				v = Version.protocolLib;
				return v;
			}
			// Standard Spigot
			try {
				Class.forName( "org.spigotmc.SpigotConfig", false, BukkitChatAPI.class.getClassLoader() );
				v = Version.spigot;
				return v;
			} catch ( ClassNotFoundException e ) {
			}
			// Standard CraftBukkit
			try {
				Class.forName( "net.minecraft.server.v1_7_R4.IChatBaseComponent", false, BukkitChatAPI.class
						.getClassLoader() );
				v = Version.bukkit_v1_7_4;
				return v;
			} catch ( ClassNotFoundException e ) {
			}
			
			if ( v == null ) {
				throw new UnsupportedOperationException( "No supported ServerVersions for BukkitChatAPI" );
			}
		}
		return v;
	}
	
	public static IChatBuilder<Player> newBuilder() {
		switch ( getVersion() ) {
			case bukkit_v1_7_4:
				return new Chat_Bukkit_v1_7_4().newBuilder();
			case protocolLib:
				return new Chat_ProtocolLib().newBuilder();
			case spigot:
				return new Chat_Spigot().newBuilder();
			default:
				throw new UnsupportedOperationException();
		}
		
	}
	
	public static IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		switch ( getVersion() ) {
			case bukkit_v1_7_4:
				return new Chat_Bukkit_v1_7_4().newBBCodeBuilder();
			case protocolLib:
				return new Chat_ProtocolLib().newBBCodeBuilder();
			case spigot:
				return new Chat_Spigot().newBBCodeBuilder();
			default:
				throw new UnsupportedOperationException();
		}
		
	}
	
}
