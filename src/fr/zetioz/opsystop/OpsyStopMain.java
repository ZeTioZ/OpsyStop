package fr.zetioz.opsystop;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class OpsyStopMain extends JavaPlugin implements Listener
{
	private Plugin plugin;

	@Override
	public void onEnable()
	{
		this.plugin = this;
	}
	
	@Override
	public void onDisable()
	{
		this.plugin = null;
	}
	
	public void registerEvents(Plugin plugin, Listener... listeners)
	{
		for(Listener listener : listeners)
		{
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	public Plugin getPlugin()
	{
		return this.plugin;
	}
}
