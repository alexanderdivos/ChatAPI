package de.mickare.chatapi.bukkittest;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.mickare.chatapi.BukkitChatAPI;
import de.mickare.chatapi.ChatColor;
import de.mickare.chatapi.api.IChatBBCodeBuilder;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {

		this.getCommand("test").setExecutor(new TestCommand());

	}

	private static final class TestCommand implements CommandExecutor {

		private static final String concat(final String[] arr, final int start,
				final String del) {
			final StringBuilder sb = new StringBuilder();
			for (int i = start; i < arr.length; i++) {
				if (i > start) {
					sb.append(del);
				}
				sb.append(arr[i]);
			}
			return sb.toString();
		}

		public boolean onCommand(final CommandSender sender, final Command cmd,
				final String label, final String[] args) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Need arguments!");
				return false;
			}
			int argindex = 0;

			final Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.RED + "Need arguments!");
					return false;
				}
				argindex++;
			}

			final String text = ChatColor.translateAlternateColorCodes('&', concat(args, argindex, " "));

			final IChatBBCodeBuilder<Player> b = BukkitChatAPI.newBBCodeBuilder();
			b.appendBBCode(text);

			if(target != null) {
				b.build().sendToPlayer(target);
			} else {
				if (sender instanceof Player) {
					b.build().sendToPlayer((Player) sender);
				} else {
					b.build().sendToConsole();
				}
			}

			return true;
		}

	}

}
