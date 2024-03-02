package fr.trophaigle.ordermand;

import java.util.Map;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerDataManager {
	
	public static void registerLobbiesServers() {
		
		//On r�cup�re la liste des serveurs enregistr�s sous bungeecord
		Map<String,ServerInfo> serverinfo = ProxyServer.getInstance().getServers();
		
		//On les enregistres sur la base de donn�e si ils n'y sont pas d�j�
		for(Map.Entry<String, ServerInfo> mapentry : serverinfo.entrySet()) {
			
			String servername = mapentry.getKey();
			
			ProxyServer.getInstance().broadcast(servername);
			if(MySQL.isConnected()) {
				MySQL.createServerData(servername);
			} else {
				ProxyServer.getInstance().broadcast("Not connected to DataBase, can't register entries on database");
			}
			
		}
		
	}
	
}
