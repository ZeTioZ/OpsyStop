package fr.zetioz.opsystop;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class OpsyStopCommand implements CommandExecutor
{
	private OpsyStopMain main;
	private YamlConfiguration messagesFile;
	private YamlConfiguration configsFile;
	private String prefix;
	
	public OpsyStopCommand(OpsyStopMain main)
	{
		this.main = main;
		this.messagesFile = main.getFilesManager().getMessagesFile();
		this.configsFile = main.getFilesManager().getConfigsFile();
		this.prefix = ChatColor.translateAlternateColorCodes('&', messagesFile.getString("prefix"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args)
	{
		if(cmd.getName().equals("opsystop"))
		{
			if(args.length == 0)
			{
				if(sender.hasPermission("opsystop.stop"))
				{
					StringBuilder reasonBuilder = new StringBuilder();
					for(String line : configsFile.getStringList("default-stop-message"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						line = line.replace("{reason}", String.valueOf(configsFile.getInt("default-stop-reason")));
						line = line.replace("{time}", String.valueOf(configsFile.getInt("default-stop-time")));
						reasonBuilder.append("\n" + line);
					}
					String reason = reasonBuilder.toString().substring(1);
					for(Player p : Bukkit.getServer().getOnlinePlayers())
					{
						p.kickPlayer(reason);
					}
					Bukkit.getServer().shutdown();
				}
				else
				{
					for(String line : messagesFile.getStringList("errors.not-enough-permission"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						sender.sendMessage(prefix + line);
					}
				}
			}
			else if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					for(String line : messagesFile.getStringList("help-command"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						sender.sendMessage(prefix + line);
					}
				}
				else if(args[0].equalsIgnoreCase("reload"))
				{
					Bukkit.getPluginManager().disablePlugin(this.main);
					Bukkit.getPluginManager().enablePlugin(this.main);
					for(String line : messagesFile.getStringList("reload-command"))
					{
						line = ChatColor.translateAlternateColorCodes('&', line);
						sender.sendMessage(prefix + line);
					}
				}
				else
				{
					int time = 0;
					String reason = "";
					try
					{
						time = Integer.parseInt(args[0]);
						if(time < 0)
						{
							for(String line : messagesFile.getStringList("errors.negative-time"))
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								sender.sendMessage(prefix + line);
							}
							return false;
						}
						if(args.length >= 2)
						{
							StringBuilder reasonBuilder = new StringBuilder();
							String[] argsToKeep = Arrays.copyOfRange(args, 1, args.length);
							for(String arg : argsToKeep)
							{
								reasonBuilder.append(" " + arg);
							}
							reason = reasonBuilder.toString().substring(1);
						}
						
					}
					catch(NumberFormatException ex)
					{
						StringBuilder reasonBuilder = new StringBuilder();
						for(String arg : args)
						{
							reasonBuilder.append(" " + arg);
						}
						reason = reasonBuilder.toString().substring(1);
					}
					StringBuilder kickBuilder = new StringBuilder();
					for(String line : configsFile.getStringList("custom-stop-message"))
					{
						if(!reason.isEmpty())
						{
							line = line.replace("{reason}", reason);
						}
						else
						{
							line = line.replace("{reason}", configsFile.getString("default-stop-reason"));
						}
						if(time != 0)
						{
							line = line.replace("{time}", String.valueOf(time));
						}
						else
						{
							line = line.replace("{time}", configsFile.getString("default-stop-time"));
						}
						line = ChatColor.translateAlternateColorCodes('&', line);
						kickBuilder.append("\n" + line);
					}
					String kickReason = kickBuilder.toString().substring(1);
					for(Player p : Bukkit.getServer().getOnlinePlayers())
					{
						p.kickPlayer(kickReason);
					}
					Bukkit.getServer().shutdown();
				}
			}
		}
		return false;
	}

}
