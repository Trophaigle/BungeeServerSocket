package fr.trophaigle.ordermand;

import fr.trophaigle.ordermand.enums.ServerStates;

public class ServerData {

	private String serverName;
	private ServerStates state;
	
	public String getServerName() {
		return serverName;
	}
	
	public ServerStates getState() {
		return state;
	}
	
	public void setServerName(String name) {
		this.serverName = name;
	}
	
	public void setState(ServerStates state) {
		this.state = state;
	}
	
}
