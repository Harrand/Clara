package com.harrand.clara;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAL implements CommandExecutor
{
	public Clara parent;
	public CommandAL(Clara parent)
	{
		this.parent = parent;
	}
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(cs instanceof Player)
		{
			Player p = (Player) cs;
			Abilities.absorbLife(p, this.parent, 10);
			return true;
		}
		return false;
	}
}
