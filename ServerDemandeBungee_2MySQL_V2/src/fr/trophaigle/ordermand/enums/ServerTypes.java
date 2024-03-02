package fr.trophaigle.ordermand.enums;

public enum ServerTypes {

	HUB(0,"Hub","hub", 2048),
	STICKGUN(1, "Stickgun", "stickgun", 2048),
	SKYUHC(2, "SkyUHC", "skyuhc", 2048),
	DBZUHC(3, "DbzUHC", "dbzuhc", 2048);
	
	private int id;
	private String displayname, modePrefix;
	private int ram;
	
	private ServerTypes(int id, String displayName, String modePrefix, int ram){
		this.id = id;
		this.displayname = displayName;
		this.modePrefix = modePrefix;
		this.ram = ram;
	}

	public int getId() {
		return id;
	}
	
	public String getDisplayName(){
		return displayname;
	}
	
	public String getModePrefix(){
		return modePrefix;
	}
	
	public int getRam(){
		return ram;
	}
	
	public static ServerTypes getById(int id){
		return ServerTypes.values()[id];
	}
}
