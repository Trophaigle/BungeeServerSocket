package fr.trophaigle.ordermand.commands;

import fr.trophaigle.ordermand.Ordermand;
import fr.trophaigle.ordermand.enums.ServerTypes;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class OrdermandOrder extends Command{

	/**
	 * Commander un serveur (type à spécifier)
	 */
	
	public OrdermandOrder() {
		super("ordercreate");	
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		try {
			//il faut 1 argument
			if(args.length != 1) {
				sender.sendMessage("Use: /ordercreate <stickgun/skyuhc/dbzuhc/hub>");
				return;
			}
			
			if(args[0].equalsIgnoreCase("stickgun")) {
				Ordermand.getInstance().getServerManager().orderServer(ServerTypes.STICKGUN.getId());
			}
			if(args[0].equalsIgnoreCase("hub")) {
				Ordermand.getInstance().getServerManager().orderServer(ServerTypes.HUB.getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//ID NON VALIDE
			//TYPE SERVER NOM VALIDE
		}
	}
}
