package fr.trophaigle.ordermand.enums;

public enum ServerStates {

	STARTING(0),
	OPEN(1),
	CLOSE(2),
	STOPING(3),
	TODELETE(4),//State pour dire que le serveur doit �tre supprim� (cette info sera envoy�e depuis le serveur lui m�me)
	NOTEXISTING(5);
	
	private int id;
	
	private ServerStates (int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public static ServerStates getById(int id){
		return ServerStates.values()[id];
	}

}
