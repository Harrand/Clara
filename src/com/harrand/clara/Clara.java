package com.harrand.clara;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Clara extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(new ClaraListener(this), this);
		this.getCommand("gm").setExecutor(new CommandGM());
		this.getCommand("sw").setExecutor(new CommandSpawnWeapon(this));
		this.getCommand("sb").setExecutor(new CommandSpawnMonster(this));
		this.getCommand("tk").setExecutor(new CommandTK(this));
		this.getCommand("al").setExecutor(new CommandAL(this));
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelAllTasks();
	}
}
