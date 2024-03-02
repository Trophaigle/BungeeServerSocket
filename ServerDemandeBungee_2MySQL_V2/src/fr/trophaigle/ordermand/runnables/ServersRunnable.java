package fr.trophaigle.ordermand.runnables;

import java.util.Map;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServersRunnable implements Runnable{

	@Override
	public void run() {
		
		//A tester
		Map<String,ServerInfo> serverinfo = ProxyServer.getInstance().getServers();
		
		//On les enregistres sur la base de donnée si ils n'y sont pas déjà ---> théoriquement ca n'est pas censer arrive
		for(Map.Entry<String, ServerInfo> mapentry : serverinfo.entrySet()) {
			String server_name = mapentry.getValue().getName();
			//register sur SQL
			
		}
		
		//Checker l'état du serveur en faisant des requete SQL (savoir si il faut supprimer un serveur, relancé si éteint...
		
	}
}
