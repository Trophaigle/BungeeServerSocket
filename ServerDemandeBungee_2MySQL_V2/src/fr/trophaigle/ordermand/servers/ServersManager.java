package fr.trophaigle.ordermand.servers;

import java.util.HashMap;

import fr.trophaigle.ordermand.enums.ServerTypes;

public class ServersManager {

	private HashMap<String, MinecraftServerS> servers;
	
	public ServersManager(){
		servers = new HashMap<>();
	}
	
	public boolean exist(String serverName){
		return servers.containsKey(serverName);
	}
	
	public boolean isUsedPort(int port){
		return servers.values().stream().filter(server -> server.getPort() == port).count() > 0;
	}
	
	public void addServer(MinecraftServerS s){
		if(!exist(s.getServerName())){
			servers.put(s.getServerName(), s);
		}
	}
	
	public void removeServer(String server){
		if(exist(server)){
			servers.remove(server);
		}
	}

	public MinecraftServerS getServer(String serverName) {
		return servers.get(serverName);
	}

	public void removeAll() {
		servers.values().forEach(server -> server.killServer());
	}
	
	//FAIT PAR MOI
	public void orderServer(Integer i){
		ServerTypes type = ServerTypes.getById(i);
		new MinecraftServerS(type, true);
	}
	
}
