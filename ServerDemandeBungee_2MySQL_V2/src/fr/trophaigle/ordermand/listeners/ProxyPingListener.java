package fr.trophaigle.ordermand.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

	@EventHandler
	public void Ping(ProxyPingEvent event){
		ServerPing ping = event.getResponse();
		
		ping.setVersion(new ServerPing.Protocol("§cPlease use 1.8-1.12.1", ping.getVersion().getProtocol()));
		
		event.setResponse(ping);
	}
	
}
