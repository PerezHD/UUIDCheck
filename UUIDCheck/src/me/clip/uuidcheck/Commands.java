package me.clip.uuidcheck;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	UUIDCheck plugin;
	
	public Commands(UUIDCheck instance) {
		plugin = instance;
	}
	
	
	private void sendMsg(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can only use this command in game!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("uuidcheck.use")) {
			sendMsg(p, "&cYou do not have permission to use that command!");
			return true;
		}
		
		if (args.length == 0) {
			sendMsg(p, "&8------------------------------------------------");
			sendMsg(p, "&eUUID&fCheck &c" + this.plugin.getDescription().getVersion());
			sendMsg(p, "&fCreated by: &cextended_clip");
			sendMsg(p, "&e/cp uuid <name> &8- &flookup a players uuid from name");
			sendMsg(p, "&e/cp name <uuid> &8- &flookup a players name from uuid");
			sendMsg(p, "&8------------------------------------------------");
			return true;
		}
		else if (args.length != 0 && args[0].equalsIgnoreCase("uuid")) {
			if (args.length != 2) {
				sendMsg(p, "&cIncorrect usage! &e/cp uuid <playername>");
				return true;
			}
			
			String target = args[1];
			
			Player t = Bukkit.getServer().getPlayer(target);
			
			if (t == null) {
			
			UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(target));
			
			Map<String, UUID> response = null;
			try {
			response = fetcher.call();
			} catch (Exception e) {
				sendMsg(p, "&cCould not retrieve any information for &f" + target + "&c!");
				return true;
			}
			
			if (response == null) {
				sendMsg(p, "&cCould not retrieve any information for &f" + target + "&c!");
				return true;
			}
			
			Set<String> keys = response.keySet();
			
			for (String name : keys) {
				if (name.equals("null") || response.get(name) == null) {
					sendMsg(p, "&cCould not retrieve any information for &f" + target + "&c!");
					return true;
				}
				target = name;
				UUID uuid = response.get(name);
				
				sendMsg(p, "&f" + target + " &7: &f" + uuid.toString());
				
			}
			
			
			
			return true;
			}
			
			target = t.getName();
			String uuid = t.getUniqueId().toString();
			
			sendMsg(p, "&f" + target + " &7: &f" + uuid);
			return true;
			
		}
		else if (args.length != 0 && args[0].equalsIgnoreCase("name")) {
			
			if (args.length != 2) {
				sendMsg(p, "&cIncorrect usage! &e/cp name <uuid>");
				return true;
			}
			
			
			UUID uuid = null;
			try {
				uuid = UUID.fromString(args[1]);
			} catch (IllegalArgumentException ex) {
				sendMsg(p, "&f" + args[1] + " &cis not a valid uuid!");
				return true;
			}
			
			NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
			
			Map<UUID, String> fetched = null;
			

				try {
					fetched = fetcher.call();
				} catch (Exception e) {
					
					sendMsg(p, "&f" + args[1] + "&cis not a valid uuid for any Minecraft user!");
					return true;
				}

				if (fetched == null || fetched.isEmpty()) {
					sendMsg(p, "&cCould not retrieve a name for &f" + uuid + " &cat this time!");
					sendMsg(p, "&cAre you sure that is a valid players uuid?");
					return true;
				}

			
			for (UUID u : fetched.keySet()) {
				
				sendMsg(p, "&f" + uuid.toString() + " &7: &f" + fetched.get(u));
			}
			
			return true;
			
		}

		else {
			sendMsg(p, "&8------------------------------------------------");
			sendMsg(p, "&cIncorrect usage!!");
			sendMsg(p, "&e/cp uuid <name> &8- &flookup a players uuid from name");
			sendMsg(p, "&e/cp name <uuid> &8- &flookup a players name from uuid");
			sendMsg(p, "&8------------------------------------------------");
		}
		
		
		return true;
	}
	
	
	


	
	
}