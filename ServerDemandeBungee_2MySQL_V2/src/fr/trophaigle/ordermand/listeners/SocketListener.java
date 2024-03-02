package fr.trophaigle.ordermand.listeners;

import java.util.concurrent.TimeUnit;

import fr.trophaigle.ordermand.Ordermand;
import fr.trophaigle.socket.event.InMessageSocketEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SocketListener implements Listener {

	@EventHandler
	public void onSocket(InMessageSocketEvent event) {
		String serverName = event.getServerName();
		String message = event.getMessage();
		
		ProxyServer.getInstance().broadcast("Event message recu " + message);
		
		if(message.equalsIgnoreCase("willClose")) {   //LE == ne marche pas pour test égalité de String
			
			ProxyServer.getInstance().getScheduler().schedule(Ordermand.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					if(Ordermand.getInstance().getServerManager().exist(serverName)){
						Ordermand.getInstance().getServerManager().getServer(serverName).killServer();
						//SERVER DETRUIT
					} else {
						//EXISTE PAS
					}
				}
			}, 10, TimeUnit.SECONDS);
			
		}
	}
}
