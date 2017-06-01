package com.harrand.clara;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CommandSpawnWeapon implements CommandExecutor
{
	public Clara parent;
	public CommandSpawnWeapon(Clara parent)
	{
		this.parent = parent;
	}
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args)
	{
		if(cs instanceof Player)
		{
			Player p = (Player) cs;
			if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("anthor"))
				{
					ItemStack killer = new ItemStack(Material.DIAMOND_SWORD, 1);
					ItemMeta im = killer.getItemMeta();
					im.setDisplayName(ChatColor.DARK_PURPLE + "Anthor Killer");
					List<String> lore = new ArrayList<String>();
					lore.add("Effective against Anthor");
					im.setLore(lore);
					im.addEnchant(Enchantment.DAMAGE_ALL, 75, true);
					killer.setItemMeta(im);
					p.getInventory().addItem(killer);
					p.sendMessage("Behold...");
				}
			}
			return true;
		}
		return false;
	}
}
