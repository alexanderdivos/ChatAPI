package de.mickare.chatapi;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.mickare.chatapi.api.*;
import de.mickare.chatapi.api.action.ActionClick;
import de.mickare.chatapi.api.action.ActionHover;
import de.mickare.chatapi.chat.ChatMessage;

import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public final class Chat_Bukkit_v1_7_4 implements IChatMessageFactory<org.bukkit.entity.Player> {

	public static final org.bukkit.ChatColor convert( final ChatColor c ) {
		if(c == null) {
			return null;
		}
		return org.bukkit.ChatColor.getByChar( c.getCode() );
	}

	public static final net.minecraft.server.v1_7_R4.EnumClickAction convert( final ActionClick a ) {
		if (a == ActionClick.OPEN_FILE) {
			return net.minecraft.server.v1_7_R4.EnumClickAction.OPEN_FILE;
		} else if (a == ActionClick.OPEN_URL) {
			return net.minecraft.server.v1_7_R4.EnumClickAction.OPEN_URL;
		} else if (a == ActionClick.SUGGEST_COMMAND) {
			return net.minecraft.server.v1_7_R4.EnumClickAction.SUGGEST_COMMAND;
		} else if (a == ActionClick.RUN_COMMAND) {
			return net.minecraft.server.v1_7_R4.EnumClickAction.RUN_COMMAND;
		} else if (a == ActionClick.TWITCH_USER_INFO) {
			return net.minecraft.server.v1_7_R4.EnumClickAction.TWITCH_USER_INFO;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.minecraft.server.v1_7_R4.ChatClickable convert( final IEventClick e ) {
		if(e == null) {
			return null;
		}
		return new net.minecraft.server.v1_7_R4.ChatClickable( convert( e.getAction() ), e.getValue() );
	}

	public static final net.minecraft.server.v1_7_R4.EnumHoverAction convert( final ActionHover a ) {
		if (a == ActionHover.SHOW_ACHIEVEMENT) {
			return net.minecraft.server.v1_7_R4.EnumHoverAction.SHOW_ACHIEVEMENT;
		} else if (a == ActionHover.SHOW_ITEM) {
			return net.minecraft.server.v1_7_R4.EnumHoverAction.SHOW_ITEM;
		} else if (a == ActionHover.SHOW_TEXT) {
			return net.minecraft.server.v1_7_R4.EnumHoverAction.SHOW_TEXT;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.minecraft.server.v1_7_R4.ChatHoverable convert( final IEventHover e ) {
		if(e == null) {
			return null;
		}
		return new net.minecraft.server.v1_7_R4.ChatHoverable( convert( e.getAction() ), convert( e.getValue() ) );
	}

	public static final net.minecraft.server.v1_7_R4.IChatBaseComponent convert( final IComponentChat[] v ) {
		net.minecraft.server.v1_7_R4.IChatBaseComponent first = null;
		for (int i = 0; i < v.length; i++) {
			if (first == null) {
				first = convert( v[i] );
			} else {
				first.addSibling( convert( v[i] ) );
			}

		}
		return first;
	}

	public static final net.minecraft.server.v1_7_R4.IChatBaseComponent convert( final List<IComponentChat> v ) {
		net.minecraft.server.v1_7_R4.IChatBaseComponent first = null;
		for (IComponentChat c : v) {
			if (first == null) {
				first = convert( c );
			} else {
				first.addSibling( convert( c ) );
			}

		}
		return first;
	}

	public static final EnumChatFormat getFormat( final ChatColor c ) {
		if(c == null) {
			return null;
		}
		return EnumChatFormat.b( c.getName() );
	}

	public static final net.minecraft.server.v1_7_R4.IChatBaseComponent convert( final IComponentChat c ) {
		net.minecraft.server.v1_7_R4.IChatBaseComponent result;
		if (c instanceof IComponentText) {
			result = _convert( (IComponentText) c );
		} else if (c instanceof IComponentTranslate) {
			result = _convert( (IComponentTranslate) c );
		} else {
			throw new IllegalStateException();
		}

		final ChatModifier cm = result.getChatModifier();

		cm.setColor( getFormat( c.getColorRaw() ) );
		cm.setBold( c.isBoldRaw() );
		cm.setItalic( c.isItalicRaw() );
		cm.setUnderline( c.isUnderlined() );
		cm.setStrikethrough( c.isStrikethroughRaw() );
		cm.setRandom( c.isObfuscatedRaw() );
		cm.setChatClickable( convert( c.getClickEvent() ) );
		cm.a( convert( c.getHoverEvent() ) );

		for (final IComponentChat v : c.getExtra()) {
			result.addSibling( convert( v ) );
		}

		return result;
	}

	private static final net.minecraft.server.v1_7_R4.ChatComponentText _convert( final IComponentText c ) {
		return new net.minecraft.server.v1_7_R4.ChatComponentText( c.getText() );
	}

	private static final net.minecraft.server.v1_7_R4.IChatBaseComponent _convert( final IComponentTranslate c ) {
		throw new UnsupportedOperationException();
	}
	
	public final BukkitChatMessage createMessage( final List<IComponentChat> lines ) {
		return new BukkitChatMessage( lines );
	}

	private static final class BukkitChatMessage extends ChatMessage<org.bukkit.entity.Player> {

		private List<IChatBaseComponent> converted = null;

		public BukkitChatMessage(final List<IComponentChat> lines) {
			super( lines );
		}

		private final List<IChatBaseComponent> getConverted() {
			if (converted == null) {
				converted = new LinkedList<>();
				for (final IComponentChat c : this.getComponents()) {
					converted.add( convert( c ) );
				}
			}
			return converted;
		}

		@Override
		public final void sendToPlayer( final Player player ) {
			if (player instanceof CraftPlayer) {
				final CraftPlayer p = ((CraftPlayer) player);
				for (final IChatBaseComponent c : getConverted()) {
					p.getHandle().playerConnection.sendPacket( new PacketPlayOutChat( c ) );
				}
			}
		}

		@Override
		public final void sendToConsole() {
			Bukkit.getConsoleSender().sendMessage( toString() );
		}

	}

	@Override
	public IChatBuilder<Player> newBuilder() {
		return new ChatBuilder<Player>(this);
	}

	@Override
	public IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		return new BBCodeChatBuilder<Player>(this);
	}

}
