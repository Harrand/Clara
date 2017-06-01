package com.harrand.clara;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Abilities
{
	protected static List<Block> getNearbyBlocks(Location location, int radius)
	{
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
        {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
            {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                {
                   blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
	
	public static void absorbLife(Player p, Clara pl, int radius)
	{
		double lifeAbsorption = 0;
		double regeneration = 0;
		for(Entity e : p.getWorld().getNearbyEntities(p.getLocation(), radius, radius, radius))
		{
			if(e instanceof LivingEntity && !e.getName().equals(p.getName()))
			{
				LivingEntity le = (LivingEntity) e;
				regeneration += 0.25;
				le.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 1));
				le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
			}
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, (int)Math.round(regeneration)));
		}
		for(Block b : Abilities.getNearbyBlocks(p.getLocation(), radius))
		{
			switch(b.getType())
			{
			case LEAVES:
				b.breakNaturally();
				lifeAbsorption += 5;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case LONG_GRASS:
				b.setType(Material.DEAD_BUSH);
				lifeAbsorption += 5;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case DOUBLE_PLANT:
				b.setType(Material.DEAD_BUSH);
				lifeAbsorption += 10;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case CACTUS:
				b.setType(Material.AIR);
				lifeAbsorption += 10;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case RED_ROSE:
				b.setType(Material.DEAD_BUSH);
				lifeAbsorption += 10;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case MOSSY_COBBLESTONE:
				b.setType(Material.COBBLESTONE);
				lifeAbsorption += 15;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case SUGAR_CANE_BLOCK:
				b.setType(Material.AIR);
				lifeAbsorption += 10;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			case GRASS:
				b.setType(Material.DIRT);
				lifeAbsorption += 5;
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 2);
				break;
			default:break;
			}
			double curHealth = p.getHealthScale();
			if(curHealth + lifeAbsorption > 20)
			{
				p.removePotionEffect(PotionEffectType.ABSORPTION);
				p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, (int) ((curHealth + lifeAbsorption - 20)/200)));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1200, (int) ((curHealth + lifeAbsorption - 20)/800)));
				p.setHealth(20);
			}
			else
				p.setHealth(curHealth + lifeAbsorption);
		}
	}
	
	public static void telekinesis(Player p, Clara pl)
	{
		Block b = p.getTargetBlock((Set<Material>) null, 256);
		int telekinesisDistance = 2;
		FallingBlock fb = p.getWorld().spawnFallingBlock(b.getLocation(), new MaterialData(b.getType()));
		fb.setGravity(false);
		fb.setInvulnerable(true);
		b.setType(Material.AIR);
		p.sendMessage("[Clara]: Sending you a block.");
		new BukkitRunnable()
		{
			public void run()
			{
				if(!fb.isValid())
				{
					p.sendMessage("[Clara]: The block was lost...");
					this.cancel();
				}
				Vector telekinesisLocation = p.getEyeLocation().toVector().add(p.getEyeLocation().getDirection().normalize().multiply(telekinesisDistance));
				if(fb.getLocation().toVector().distance(telekinesisLocation) < 2)
				{
					fb.setVelocity(new Vector());
					p.sendMessage("[Clara]: Block Arrived.");
					p.setMetadata("invoker", new FixedMetadataValue(pl, "no"));
					new BukkitRunnable()
					{
						public void run()
						{
							// Continue to update tkLocation
							Vector tkLocation = p.getEyeLocation().toVector().add(p.getEyeLocation().getDirection().normalize().multiply(telekinesisDistance));
							fb.teleport(tkLocation.toLocation(p.getWorld(), (float)Math.sin(System.currentTimeMillis() / 1000), 0));
							if(p.hasMetadata("invoker") && p.getMetadata("invoker").get(0).value().equals("yes"))
							{
								fb.getWorld().playEffect(fb.getLocation(), Effect.DRAGON_BREATH, 1);
								fb.setVelocity((fb.getLocation().toVector().subtract(p.getEyeLocation().toVector())).normalize());
								fb.setMetadata("explosion", new FixedMetadataValue(pl, 5.0f));
								fb.setGravity(true);
								p.sendMessage("[Clara]: Block launched.");
								p.removeMetadata("invoker", pl);
								this.cancel();
							}
						}
					}.runTaskTimerAsynchronously(pl, 0, 1L);
					this.cancel();
				}
				else
				{
					fb.setVelocity((fb.getLocation().toVector().subtract(telekinesisLocation)).normalize().multiply(-2.0f));
					fb.getWorld().playEffect(fb.getLocation(), Effect.ENDER_SIGNAL, 1);
				}
			}
		}.runTaskTimerAsynchronously(pl, 0, 1L);
	}
}
