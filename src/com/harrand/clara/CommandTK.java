package com.harrand.clara;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTK implements CommandExecutor
{
	public Clara parent;
	public CommandTK(Clara parent)
	{
		this.parent = parent;
	}
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(cs instanceof Player)
		{
			Player p = (Player) cs;
			Abilities.telekinesis(p, this.parent);
			return true;
		}
		return false;
	}
}
