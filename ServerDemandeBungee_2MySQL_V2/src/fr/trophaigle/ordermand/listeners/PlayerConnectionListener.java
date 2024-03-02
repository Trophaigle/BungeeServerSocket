package fr.trophaigle.ordermand.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.trophaigle.ordermand.Ordermand;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

public class PlayerConnectionListener implements Listener {

	protected Ordermand plugin;
	private static Set<Callback<ProxiedPlayer>> triggerOnJoin = new HashSet<>();

	public PlayerConnectionListener(Ordermand plugin) {
		this.plugin = plugin;
	}

	public static void triggerOnJoin(Callback<ProxiedPlayer> trigger) {
		triggerOnJoin.add(trigger);
	}

	@EventHandler
	public void onJoin(final PostLoginEvent e) {
		final ProxiedPlayer p = e.getPlayer();
		
		p.setTabHeader(new TextComponent("§cCublow§eMC"), new TextComponent("§aForum et §eboutique §f-> §bhttps://cublow.fr"));
		
		ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
			TextComponent welcome = new TextComponent("Bienvenue, " + p.getName() + " !");
			welcome.setColor(ChatColor.GOLD);
		
			
/*
			String key = "rejoinlist:" + e.getPlayer().getUniqueId().toString();
			Jedis cache = plugin.getDatabaseConnector().getResource();
			String srv = cache.get(key);
			cache.close();
			if(srv != null) {
				final ServerInfo server = ProxyServer.getInstance().getServerInfo(srv);
				if(server != null)
					ProxyServer.getInstance().getScheduler().schedule(plugin, () -> e.getPlayer().connect(server, (aBoolean, throwable) -> {
						if (aBoolean) {
							p.sendMessage(new ComponentBuilder("").color(ChatColor.GREEN).append("Vous avez été remis en jeu.").append("Serveur: " + server.getName()).create());
						}
					}), 200L, TimeUnit.MILLISECONDS);
			}*/
		});
	}
}

