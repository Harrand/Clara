package com.harrand.clara;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ClaraListener implements Listener
{
	Clara pl;
	public ClaraListener(Clara c)
	{
		this.pl = c;
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(p.hasMetadata("invoker") && p.getMetadata("invoker").get(0).value().equals("no"))
		{
			p.setMetadata("invoker", new FixedMetadataValue(pl, "yes"));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
    public void onBlockFall(EntityChangeBlockEvent event)
	{
        if ((event.getEntityType() == EntityType.FALLING_BLOCK))
        {
        	if(event.getEntity().hasMetadata("explosion"))
        	{
        		event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), (float)event.getEntity().getMetadata("explosion").get(0).value());
        		event.setCancelled(true);
        		event.getEntity().removeMetadata("explosion", pl);
        	}
        }
    }
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e)
	{
		if(e.getEntity().hasMetadata("boss") && e.getEntity().getMetadata("boss").get(0).value().equals("anthor"))
		{
			// anthor just got hit
			Wither anthor = (Wither) e.getEntity();
			if(e.getDamager() instanceof LivingEntity)
			{
				LivingEntity le = (LivingEntity) e.getDamager();
				if(!Monsters.canDamageAnthor(le))
				{
					le.damage(1, anthor);
					le.setVelocity(le.getLocation().toVector().subtract(anthor.getEyeLocation().toVector()).multiply(1));
					anthor.getWorld().playSound(le.getLocation(), Sound.BLOCK_ANVIL_BREAK, 3.0f, 0.566f);
					le.sendMessage("You are unable to harm " + ChatColor.DARK_PURPLE + "Anthor" + ChatColor.WHITE + "!");
					e.setCancelled(true);
				}
				else
				{
					le.sendMessage("Your attack was effective against " + ChatColor.DARK_PURPLE +  "Anthor" + ChatColor.WHITE + "!");
					if(e.getDamage() >= anthor.getHealth())
					{
						// Anthor will die
						anthor.setMetadata("boss", new FixedMetadataValue(pl, "dying"));
						e.setCancelled(true);
						anthor.setHealth(1.0f);
						anthor.setInvulnerable(true);
						new BukkitRunnable()
						{
							float time = 1.0f;
							public void run()
							{
								if(time >= 6.0f)
								{
									anthor.setVelocity(new Vector(0, -200, 0));
									anthor.setGravity(true);
									anthor.getWorld().playSound(anthor.getLocation(), Sound.ENTITY_WITHER_HURT, 5.0f, 1.5f);
									new BukkitRunnable()
									{
										public void run()
										{
											anthor.setInvulnerable(false);
											anthor.setAI(false);
											anthor.damage(anthor.getHealth() - 1, e.getDamager());
										}
									}.runTaskLaterAsynchronously(pl, 200);
									for(Entity e : anthor.getWorld().getNearbyEntities(anthor.getLocation(), 200, 200, 200))
										e.sendMessage(ChatColor.DARK_PURPLE + "An irresistable happiness fills you");
									this.cancel();
								}
								time += 0.25f;
								anthor.setTarget(null);
								anthor.setVelocity(new Vector(Math.random(), Math.random(), Math.random()).normalize());
								anthor.getWorld().playSound(anthor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3.0f, time/2);
							}
						}.runTaskTimerAsynchronously(pl, 5, 10);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityShoot(ProjectileLaunchEvent e)
	{
	    if(e.getEntity().getShooter() instanceof Wither && ((Wither)e.getEntity().getShooter()).getMetadata("boss").get(0).value().equals("dying"))
	    	e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e)
	{
		if(e.getEntity().hasMetadata("boss"))
		{
			e.getEntity().getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "A great evil has been defeated!");
		}
	}
}
