package co.networkery.uvbeenzaned.NETFire;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NETFire extends JavaPlugin{

	public final MyListener ml = new MyListener();
	Logger log;
	
	public void onEnable()
	{
		log = this.getLogger();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.ml, this);
	    ml.configFile = new File(getDataFolder(), "config.yml");
	    ml.usersFile = new File(getDataFolder(), "users.yml");
	    try {
	        firstRun();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    ml.config = new YamlConfiguration();
	    ml.users = new YamlConfiguration();
	    loadYamls();
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
	}
	
	public void onDisable()
	{
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("netfire"))
		{
				if(args.length != 0)
				{
					if(args[0].equalsIgnoreCase("help"))
					{
						return false;
					}
					if(sender.isOp())
					{
						if(args[0].equalsIgnoreCase("enable"))
						{
							ml.config.set("enabled", true);
							saveYamls();
							sendPluginMessage(sender, "Plugin enabled!");
							return true;
						}
						if(args[0].equalsIgnoreCase("disable"))
						{
							ml.config.set("enabled", false);
							saveYamls();
							sendPluginMessage(sender, "Plugin disabled!");
							return true;
						}
					}
					sendPluginMessage(sender, "You need to be an op to run this command!  Type /netfire help for help.");
					return true;
				}
			sendPluginMessage(sender, "Version: " + this.getDescription().getVersion().toString() + ".");
			sendPluginMessage(sender, "Type /netfire help for help.");
			return true;
		}
		return false;
	}
	
	private void sendPluginMessage(CommandSender sndr, String message)
	{
		sndr.sendMessage("[NETFire] " + message);
	}
	
	private void firstRun() throws Exception {
	    if(!ml.configFile.exists()){
	        ml.configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), ml.configFile);
	    }
	    if(!ml.usersFile.exists()){
	        ml.usersFile.getParentFile().mkdirs();
	        copy(getResource("users.yml"), ml.usersFile);
	    }
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveYamls() {
	    try {
	        ml.config.save(ml.configFile);
	        ml.users.save(ml.usersFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void loadYamls() {
	    try {
	        ml.config.load(ml.configFile);
	        ml.users.load(ml.usersFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
