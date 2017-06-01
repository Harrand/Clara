package com.harrand.clara;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandSpawnMonster implements CommandExecutor
{
	public Clara parent;
	public CommandSpawnMonster(Clara parent)
	{
		this.parent = parent;
	}
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(cs instanceof Player && args.length >= 1)
		{
			Player p = (Player) cs;
			Location loc = p.getLocation();
			if(args[0].equalsIgnoreCase("anthor"))
			{
				loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 3.0F, 0.8F);
				for(Entity e : p.getWorld().getNearbyEntities(loc, 200, 200, 200))
					e.sendMessage(ChatColor.DARK_PURPLE + "You begin to experience unrelenting dread; something terrible has just happened...");
				new BukkitRunnable()
				{
					public void run()
					{
						Monsters.spawnAnthor(parent, loc);
					}
				}.runTaskLater(parent, 200L);
				new BukkitRunnable()
				{
					int num = 1;
					public void run()
					{
						loc.getWorld().playEffect(loc, Effect.SMOKE, num/10);
						loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, num/10);
						if(++num > 200)
							this.cancel();
						for(Entity e : p.getWorld().getNearbyEntities(loc, num/40, num/40, num/40))
						{
							if(e.getType() != EntityType.WITHER)
								e.setVelocity(e.getLocation().toVector().subtract(loc.toVector().normalize()));
						}
					}
				}.runTaskTimerAsynchronously(parent, 0, 1);
			}
			return true;
		}
		return false;
	}
}
