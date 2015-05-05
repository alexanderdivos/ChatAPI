package de.mickare.chatapi;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ProxyServer;
import de.mickare.chatapi.api.*;
import de.mickare.chatapi.api.action.*;
import de.mickare.chatapi.chat.ChatMessage;

public final class Chat_Spigot implements IChatMessageFactory<Player> {

	public static final net.md_5.bungee.api.ChatColor convert( final ChatColor c ) {
		if (c == null) {
			return null;
		}
		return net.md_5.bungee.api.ChatColor.getByChar( c.getCode() );
	}

	public static final net.md_5.bungee.api.chat.ClickEvent.Action convert( final ActionClick a ) {
		if (a == ActionClick.OPEN_FILE) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_FILE;
		} else if (a == ActionClick.OPEN_URL) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
		} else if (a == ActionClick.SUGGEST_COMMAND) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND;
		} else if (a == ActionClick.RUN_COMMAND) {
			return net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.md_5.bungee.api.chat.ClickEvent convert( final IEventClick e ) {
		if (e == null) {
			return null;
		}
		return new net.md_5.bungee.api.chat.ClickEvent( convert( e.getAction() ), e.getValue() );
	}

	public static final net.md_5.bungee.api.chat.HoverEvent.Action convert( final ActionHover a ) {
		if (a == ActionHover.SHOW_ACHIEVEMENT) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ACHIEVEMENT;
		} else if (a == ActionHover.SHOW_ITEM) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM;
		} else if (a == ActionHover.SHOW_TEXT) {
			return net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT;
		} else {
			throw new IllegalStateException();
		}
	}

	public static final net.md_5.bungee.api.chat.HoverEvent convert( final IEventHover e ) {
		if (e == null) {
			return null;
		}
		return new net.md_5.bungee.api.chat.HoverEvent( convert( e.getAction() ),
				new net.md_5.bungee.api.chat.BaseComponent[] { convert( e.getValue() ) } );
	}

	public static final net.md_5.bungee.api.chat.BaseComponent[] convert( final IComponentChat[] v ) {
		final List<net.md_5.bungee.api.chat.BaseComponent> result = new LinkedList<>();
		for (int i = 0; i < v.length; i++) {
			result.add( convert( v[i] ) );
		}
		return result.toArray( new net.md_5.bungee.api.chat.BaseComponent[0] );
	}

	public static final List<net.md_5.bungee.api.chat.BaseComponent> convert( final List<IComponentChat> v ) {
		final List<net.md_5.bungee.api.chat.BaseComponent> result = new LinkedList<>();
		for (IComponentChat c : v) {
			result.add( convert( c ) );
		}
		return result;
	}

	public static final net.md_5.bungee.api.chat.BaseComponent convert( final IComponentChat c ) {
		net.md_5.bungee.api.chat.BaseComponent result;
		if (c instanceof IComponentText) {
			result = _convert( (IComponentText) c );
		} else if (c instanceof IComponentTranslate) {
			result = _convert( (IComponentTranslate) c );
		} else {
			throw new IllegalStateException();
		}

		result.setColor( convert( c.getColorRaw() ) );
		result.setBold( c.isBoldRaw() );
		result.setItalic( c.isItalicRaw() );
		result.setUnderlined( c.isUnderlined() );
		result.setStrikethrough( c.isStrikethroughRaw() );
		result.setObfuscated( c.isObfuscatedRaw() );
		result.setClickEvent( convert( c.getClickEvent() ) );
		result.setHoverEvent( convert( c.getHoverEvent() ) );

		for (IComponentChat v : c.getExtra()) {
			result.addExtra( convert( v ) );
		}

		return result;
	}

	private static final net.md_5.bungee.api.chat.BaseComponent _convert( final IComponentText c ) {
		return new net.md_5.bungee.api.chat.TextComponent( c.getText() );
	}

	private static final net.md_5.bungee.api.chat.BaseComponent _convert( final IComponentTranslate c ) {
		net.md_5.bungee.api.chat.TranslatableComponent t = new net.md_5.bungee.api.chat.TranslatableComponent();
		t.setTranslate( c.getTranslate() );
		t.setWith( convert( c.getWith() ) );
		return t;
	}

	public final SpigotChatMessage createMessage( final List<IComponentChat> lines ) {
		return new SpigotChatMessage( lines );
	}

	private static final class SpigotChatMessage extends ChatMessage<Player> {

		private List<net.md_5.bungee.api.chat.BaseComponent[]> converted = null;

		public SpigotChatMessage(final List<IComponentChat> lines) {
			super( lines );
		}

		private final List<net.md_5.bungee.api.chat.BaseComponent[]> getConverted() {
			if (converted == null) {
				converted = new LinkedList<>();
				for (final IComponentChat c : this.getComponents()) {
					converted.add( new net.md_5.bungee.api.chat.BaseComponent[] { convert( c ) } );
				}
			}
			return converted;
		}

		@Override
		public final void sendToPlayer( final Player player ) {
			for (final net.md_5.bungee.api.chat.BaseComponent[] c : getConverted()) {
				player.spigot().sendMessage( c );
			}
		}

		@Override
		public final void sendToConsole() {
			Bukkit.getConsoleSender().sendMessage( toString() );
		}

	}

	@Override
	public IChatBuilder<Player> newBuilder() {
		return new ChatBuilder<Player>( this );
	}

	@Override
	public IChatBBCodeBuilder<Player> newBBCodeBuilder() {
		return new BBCodeChatBuilder<Player>( this );
	}

}
