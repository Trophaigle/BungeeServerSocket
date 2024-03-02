package fr.trophaigle.ordermand.commands;

import fr.trophaigle.ordermand.Ordermand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class OrdermandKill extends Command{

	/**
	 * Commande pour supprimer à la main un serveur qui doti au préalable être éteint
	 */
	
	public OrdermandKill() {
		super("killserver");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0){
			sender.sendMessage(new TextComponent("/killserver <server-name>"));
			return;
		}
		String serverName = args[0];
		
		if(Ordermand.getInstance().getServerManager().exist(serverName)){
			Ordermand.getInstance().getServerManager().getServer(serverName).killServer();
			//SERVER DETRUIT
		} else {
			//EXISTE PAS
		}
		
	}



}
