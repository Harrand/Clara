package com.harrand.clara;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class Monsters
{
	public static Wither spawnAnthor(Clara pl, Location loc)
	{
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 200, 200, 200))
			e.sendMessage(ChatColor.DARK_PURPLE + "[Anthor]: Sk'yahf qi'plahf PH'MAGG!");
		Wither anthor = (Wither) loc.getWorld().spawnEntity(loc, EntityType.WITHER);
		anthor.setCustomName("Anthor");
		// dev
		//anthor.setAI(false);
		anthor.setCustomNameVisible(true);
		anthor.setRemoveWhenFarAway(false);
		anthor.setMetadata("boss", new FixedMetadataValue(pl, "anthor"));
		anthor.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), false);
		return anthor;
	}
	
	public static boolean canDamageAnthor(LivingEntity e)
	{
		if(e.getEquipment() == null)
			return false;
		EntityEquipment ee = e.getEquipment();
		if(ee.getItemInMainHand() == null)
			return false;
		ItemStack mainHand = ee.getItemInMainHand();
		boolean hasEffectiveOffhand = false;
		if(ee.getItemInOffHand() != null && ee.getItemInOffHand().hasItemMeta())
		{
			ItemStack offHand = ee.getItemInOffHand();
			hasEffectiveOffhand = offHand.getItemMeta().getLore().contains("Effective against Anthor");
		}
		if(mainHand.hasItemMeta() && mainHand.getItemMeta().getLore().contains("Effective against Anthor") || hasEffectiveOffhand)
			return true;
		return false;
	}
}
