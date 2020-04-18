package fr.zetioz.opsystop;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.zetioz.opsystop.utils.FilesManager;

public class OpsyStopMain extends JavaPlugin implements Listener
{
	private Plugin plugin;
	private FilesManager filesManager;

	@Override
	public void onEnable()
	{
		this.plugin = this;
		
		filesManager = new FilesManager(this);
		
		filesManager.createMessagesFile();
		filesManager.createConfigsFile();
		
		getCommand("opsystop").setExecutor(new OpsyStopCommand(this));
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
	
	public FilesManager getFilesManager()
	{
		return this.filesManager;
	}
}
