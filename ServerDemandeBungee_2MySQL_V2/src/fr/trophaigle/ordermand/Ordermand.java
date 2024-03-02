package fr.trophaigle.ordermand;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.trophaigle.ordermand.commands.CommandHub;
import fr.trophaigle.ordermand.commands.OrdermandKill;
import fr.trophaigle.ordermand.commands.OrdermandOrder;

import fr.trophaigle.ordermand.listeners.PlayerConnectionListener;
import fr.trophaigle.ordermand.listeners.ProxyPingListener;
import fr.trophaigle.ordermand.listeners.SocketListener;
import fr.trophaigle.ordermand.runnables.ServersRunnable;
import fr.trophaigle.ordermand.servers.ServersManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Ordermand extends Plugin{
	
	private static Ordermand instance;
	
	Logger logger;
	
	DatabaseConnection database;
	ServersManager manager;
	
	boolean isHubProxy;
	
	String pathToModelsFolder;
	String pathToServersFolder;
	String windowsExecutePathToServers;
	
	@Override
	public void onEnable() {
		//Config
		createFile("config");
		Configuration config = getConfig("config");
		pathToModelsFolder = config.getString("absolutePathToModelsFolder");
		pathToServersFolder = config.getString("absolutePathToServersFolder");
		windowsExecutePathToServers = config.getString("windowsExecutePathToServers");
		if(config.getBoolean("SQL_DATABASE")) {
			//SE co à base de donnée SQL
			MySQL.connect(config.getString("IP_SQL_DATABASE"), 
					config.getString("TABLE"), 
					config.getInt("PORT_DATABASE"), 
					config.getString("USER_NAME"), 
					config.getString("PASSWORD"));
		}
		
		instance = this;
		
		//Listeners
		ProxyServer.getInstance().getPluginManager().registerListener(this, new SocketListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerConnectionListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyPingListener());
		
		//MySQL.connect("localhost", "servermanager", 3306, "root", "root");
		
		this.manager = new ServersManager();
		
		//Commands
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new OrdermandOrder());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new OrdermandKill());
		ProxyServer .getInstance().getPluginManager().registerCommand(this, new CommandHub());
		
		//ProxyServer.getInstance().getScheduler().schedule(this, new ServersRunnable(), 0, TimeUnit.MINUTES);

		if(MySQL.isConnected()) {
			ServerDataManager.registerLobbiesServers();
		}
		
		if(ProxyServer.getInstance().getName().equalsIgnoreCase("proxy1")){
			isHubProxy = true;
		} else {
			isHubProxy = false;
		}
	}
	
	public void onDisable() {
		getServerManager().removeAll();
	}
	
	public static Ordermand getInstance(){
		return instance;
	}
	
	public DatabaseConnection getDatabaseConnector(){
		return database;
	}
	
	public ServersManager getServerManager(){
		return manager;
	}
	
	public String getPathFolderModels(){
		return pathToModelsFolder;
	}
	
	public String getPathFolderServers(){
		return pathToServersFolder;
	}
	
	public String getExecutePathToServers() {
		return windowsExecutePathToServers;
	}
	
	public void logInfo(String log){
		logger.log(Level.INFO, log);
	}
	
	public void logSevere(String log){
		logger.log(Level.SEVERE, log);
	}
	
	public void createFile(String fileName){
		if(!getDataFolder().exists()){
			getDataFolder().mkdir();
		}
		
		File file = new File(getDataFolder(), fileName + ".yml");
		
		if(!file.exists()){
			try {
				file.createNewFile();
				
				if(fileName.equals("config")){
					Configuration config = getConfig(fileName);
					config.set("min_server_opened", 1);
					config.set("SQL_DATABASE", true);
					config.set("IP_SQL_DATABASE", "localhost");
					config.set("PORT_DATABASE", 3306);
					config.set("TABLE", "servermanager");
					config.set("USER_NAME", "root");
					config.set("PASSWORD", "root");
					config.set("absolutePathToModelsFolder", "C:/minecraft-server/CUBLOW-MC/network-minecraft/models/");
					config.set("absolutePathToServersFolder", "C:/minecraft-server/CUBLOW-MC/network-minecraft/servers/");
					config.set("windowsExecutePathToServers", "C:\\minecraft-server\\CUBLOW-MC\\network-minecraft\\servers\\");
					saveConfig(config, fileName);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Configuration getConfig(String fileName){
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveConfig(Configuration config, String fileName){
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), fileName + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
