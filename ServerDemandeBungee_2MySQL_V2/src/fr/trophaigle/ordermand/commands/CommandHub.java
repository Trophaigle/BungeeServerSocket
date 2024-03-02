package fr.trophaigle.ordermand.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandHub extends Command {
	
	public CommandHub() {
		super("hub");	
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo("lobby_01"));
		}
	}

}
