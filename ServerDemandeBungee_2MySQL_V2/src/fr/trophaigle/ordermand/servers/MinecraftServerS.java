package fr.trophaigle.ordermand.servers;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import fr.trophaigle.ordermand.MySQL;
import fr.trophaigle.ordermand.Ordermand;
import fr.trophaigle.ordermand.enums.ServerTypes;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class MinecraftServerS {

	private ServerTypes type;
	private String serverName;
	private int port;
	private Thread t;
	
	public MinecraftServerS(ServerTypes type, boolean autostart){
		this.type = type;
		
		//this.serverName = UUID.randomUUID().toString().substring(0, 8).replace("-", "");
		//Choix du servername
		String name = type.getModePrefix() + "_"+  new Random().nextInt(100);
		if(Ordermand.getInstance().getServerManager().exist(name) == false) {
			this.serverName = name;
		} else {
			this.serverName = type.getModePrefix() + "_" +  new Random().nextInt(100);
		}

		//Choix du port
		int p = new Random().nextInt(40000) + 10000;
		if(Ordermand.getInstance().getServerManager().isUsedPort(p) == false) {
			this.port = p;
		} else {
			this.port = new Random().nextInt(40000) + 10000;
		}
		
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					copyDirectory(Ordermand.getInstance().getPathFolderModels() + type.getModePrefix(), Ordermand.getInstance().getPathFolderServers() + serverName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				//ModifyFileText.modifyFile("C:/minecraft-server/CUBLOW-MC/network-minecraft/servers/" + serverName + "/server.properties", "server-port=25566", "server-port=" + String.valueOf(port));
				//ModifyFileText.modifyFile("C:/minecraft-server/CUBLOW-MC/network-minecraft/servers/" + serverName + "/server.properties", "server-name=", "server-name=" + serverName);
				
				//avec la config
				ModifyFileText.modifyFile(Ordermand.getInstance().getPathFolderServers() + serverName + "/server.properties", "server-port=25566", "server-port=" + String.valueOf(port));
				ModifyFileText.modifyFile(Ordermand.getInstance().getPathFolderServers() + serverName + "/server.properties", "server-name=", "server-name=" + serverName);
				
				t.interrupt();
				if(autostart){
					startServer();
				}
			}			
		});
		t.start();
		ServerInfo info = ProxyServer.getInstance().constructServerInfo(serverName, InetSocketAddress.createUnresolved("localhost", port), serverName, false);
		ProxyServer.getInstance().getServers().put(serverName, info);
		Ordermand.getInstance().getServerManager().addServer(this);
		if(MySQL.isConnected()) {
			MySQL.createServerData(serverName);
		}
	}
	
	private void startServer() {
		ProxyServer.getInstance().getLogger().info("Trying to start " + serverName +":" + port);
		try {
			
			//String command = "./start_ordermand_server.bat/" + serverName + " " + type.getRam() + " " + port;
		
			Runtime.getRuntime().exec("cmd /c start " + Ordermand.getInstance().getExecutePathToServers() + serverName + "\\start_ordermand_server.bat", null, new File(Ordermand.getInstance().getExecutePathToServers() + serverName + "\\"));
			
			ProxyServer.getInstance().broadcast("Server: " + serverName + " lancé sur le port: "+ String.valueOf(port));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			ProxyServer.getInstance().getLogger().warning("Failled to start " + serverName + ":" + port);
		}
	}
	
	public void deleteServer(){
		try {
			
			FilesUtils.deleteFolder(new File(Ordermand.getInstance().getPathFolderServers() + serverName));
			ProxyServer.getInstance().getServers().remove(serverName);
			
			Ordermand.getInstance().getServerManager().removeServer(serverName);
			if(MySQL.isConnected()) {
				MySQL.deleteServerData(serverName);
			}
			
			ProxyServer.getInstance().broadcast("$eServeur supprimé: " + serverName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void killServer(){
		try {
			//Runtime.getRuntime().exec("screen -S " + this.serverName + " -X quit");
			
		} catch (Exception e) {
			ProxyServer.getInstance().getLogger().warning("Failled to kill the server " + serverName);
			e.printStackTrace();
			return;
		}
		deleteServer();
		ProxyServer.getInstance().getLogger().info(serverName + " has been killed");
	}
	
	public ServerTypes getServerType(){
		return type;
	}
	
	public String getServerName(){
		return serverName;
	}
	
	public int getPort(){
		return port;
	}
	
	 /**
	  * Meilleurs que copyFolder de FilesUtils
	  * @param sourceDirectoryLocation
	  * @param destinationDirectoryLocation
	  * @throws IOException
	  */
	public void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
          .forEach(source -> {
              Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                .substring(sourceDirectoryLocation.length()));
              try {
                  Files.copy(source, destination);
              } catch (IOException e) {
                  e.printStackTrace();
              }
          });
    }
}
