package com.harrand.clara;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGM implements CommandExecutor
{
	public ChatColor error = ChatColor.DARK_RED;
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(cs instanceof Player)
		{
			Player player = (Player) cs;
			if(args.length < 1 || args.length > 2)
			{
				player.sendMessage(error + "[Clara]: Expected 1 argument(s), received " + args.length + ".");
				return false;
			}
			String arg = args[0];
			String targetStr = args.length == 2 ? args[1] : cs.getName();
			Player target = Bukkit.getServer().getPlayer(targetStr);
			if(target == null)
			{
				player.sendMessage(error + "[Clara]: The target player '" + targetStr + "' that you specified could not be resolved to an online player.");
				return false;
			}
			if(arg.equalsIgnoreCase("c"))
			{
				target.setGameMode(GameMode.CREATIVE);
			}
			else if(arg.equalsIgnoreCase("s"))
			{
				target.setGameMode(GameMode.SURVIVAL);
			}
			else if(arg.equalsIgnoreCase("sp"))
			{
				target.setGameMode(GameMode.SPECTATOR);
			}
			else if(arg.equalsIgnoreCase("a"))
			{
				target.setGameMode(GameMode.ADVENTURE);
			}
			return true;
		}
		return false;
	}

}
