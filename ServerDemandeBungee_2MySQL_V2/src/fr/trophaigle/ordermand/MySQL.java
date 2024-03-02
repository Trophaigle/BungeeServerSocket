package fr.trophaigle.ordermand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.trophaigle.ordermand.enums.ServerStates;

public class MySQL {

	private static Connection conn;
	public static HashMap<String,ServerData> serverdata = new HashMap<>();
	 
	/**
	 * Créer connection avec DataBase SQL
	 * @host ipmachine
	 * @database nom table
	 * @port port database
	 * @user nom user
	 * @password password de la database
	 */
	 public static void connect(String host, String database, int port, String user, String password){
	        if(!isConnected()){
	            try {
	                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
	                System.out.println("-----------------------------------------------------------------");
	                System.out.println("§7[§bMySQL§7] §aConnexion etablie avec la base de donnees");
	                System.out.println("-----------------------------------------------------------------");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                System.out.println("§7[§bMySQL§7] §cConnexion refuse avec la base de donnees");
	            }  
	        }
	    }
	   
	 /**
	  * Deconnection base de donnée SQL
	  */
	    public static void disconnect(){
	        if(isConnected()){
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	  
	    /**
	     * Is connected to SQL database ?
	     * @return
	     */
	    public static boolean isConnected(){
	        try {
	            if((conn == null) || (conn.isClosed()) || (conn.isValid(5))){
	                return false;
	            }
	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	   
	    public static Connection getConnection(){
	        return conn;
	    }
	    
	   
	 /*
	    public static void updateServerData(String servername){
	    	
	        if(serverdata.containsKey(servername)){
	            ServerData dataP = serverdata.get(servername);
	            ServerStates state = dataP.getState();
	            
	            try {
	                PreparedStatement rs = MySQL.getConnection().prepareStatement("UPDATE serverinfo SET State = ? WHERE ServerName = ?");
	                rs.setInt(1, state.getId());
	                rs.setString(2, servername);
	                rs.executeUpdate();
	                rs.close();
	                
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	           
	        }
	       
	    }*/
	    /*
	    public static void updateServerDataFromDataBase(){
	    	
	        if(serverdata.containsKey(servername)){
	            ServerData dataP = serverdata.get(servername);
	            ServerStates state = dataP.getState();
	            
	            try {
	                PreparedStatement rs = MySQL.getConnection().prepareStatement("UPDATE serverinfo SET State = ? WHERE ServerName = ?");
	                rs.setInt(1, state.getId());
	                rs.setString(2, servername);
	                rs.executeUpdate();
	                rs.close();
	                
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	           
	        }
	       
	    }*/
	    
	    /**
	     * Créer identité du serveur cible sur database SQL
	     * @param servername
	     * @return
	     */
	    public static ServerData createServerData(String servername){
	  
	        if(!serverdata.containsKey(servername)){
	            try {
	            	
	            	Statement stat = MySQL.getConnection().createStatement();
	            	
	            	ResultSet rs = stat.executeQuery("SELECT COUNT(*) FROM serverinfo WHERE ServerName='" + servername+"'");
	                
	                int n = 0;
	            	
	                ServerStates state = ServerStates.CLOSE;	                
	             
	                if(rs.next()) {
	                	n = rs.getInt(1);
	                }
	                
	                //Resultat null 
	                if(n > 0) {
	                	ResultSet rss = stat.executeQuery("SELECT * FROM serverinfo WHERE ServerName='" + servername+"'");
	                	
	                	while(rss.next()){
	                	
	                		state = ServerStates.getById(rss.getInt("State"));
	                   		
	                	}
	               
	                	ServerData dataP = new ServerData();
	                	dataP.setState(state);
	                	dataP.setServerName(servername);
	              
	                	serverdata.put(servername, dataP);
	               
	                	return dataP;
	                } else { 
	                	String sql = "INSERT INTO serverinfo VALUES ('"+ servername +"', 2)";
	                    Statement stmt = MySQL.getConnection().createStatement();
	                	stmt.executeUpdate(sql);
	                	
	                	ServerData dataPi = new ServerData();
	                	dataPi.setState(ServerStates.CLOSE);
	                	dataPi.setServerName(servername);
	              
	                	serverdata.put(servername, dataPi);
	                	stmt.close();
	                	return dataPi;
	               }
	            } catch (Exception e) {
					e.printStackTrace();
				}
	            
	        } 
	        return new ServerData();
	    }
  
	    /**
	     * Delete le serveur cible de la dataBase SQL  
	     * @param servername
	     */
	    public static void deleteServerData(String servername){
	  
	        if(serverdata.containsKey(servername)){
	            try {
	            	
	            	Statement stat = MySQL.getConnection().createStatement();
	            	
	            	ResultSet rs = stat.executeQuery("SELECT COUNT(*) FROM serverinfo WHERE ServerName='" + servername+"'");
	                
	                int n = 0;
	            	
	                if(rs.next()) {
	                	n = rs.getInt(1);
	                }
	                
	                //Resultat null 
	                if(n > 0) {
	                	PreparedStatement st = MySQL.getConnection().prepareStatement("DELETE FROM serverinfo WHERE ServerName='" + servername+"'");
	                	st.executeUpdate();
	                	
	                	serverdata.remove(servername);
	               
	                } else { 
	                	//Normalment il existe forcement
	                	
	               }
	            } catch (Exception e) {
					e.printStackTrace();
				}
	            
	        } 
	       // LocalDate l = LocalDate.of(2000, Month.APRIL, 20);
	       
	       
	    }
  		
		public static ServerStates getState(String servername){
			if(serverdata.containsKey(servername)){
				ServerData data = serverdata.get(servername);
				return data.getState();
			}
			return ServerStates.NOTEXISTING;
		}
		
		public static void setState(String servername, ServerStates state){
	        if(serverdata.containsKey(servername)){
	            ServerData dataP = serverdata.get(servername);
	            dataP.setState(state);
	            serverdata.remove(servername);
	            serverdata.put(servername, dataP);
	        }
		}
		
	  public static void Update(String qry)
	  {
	    try
	    {
	      Statement stmt = conn.createStatement();
	      stmt.executeUpdate(qry);
	      
	      stmt.close();
	    }
	    catch (Exception ex)
	    {
	      System.err.println(ex);
	      ex.printStackTrace();
	    }
	  }	  
	  
	  public static ResultSet Query(String qry)
	  {
	    ResultSet rs = null;
	    try
	    {
	      Statement stmt = conn.createStatement();
	      rs = stmt.executeQuery(qry);
	    }
	    catch (Exception ex)
	    {
	      System.err.println(ex);
	      ex.printStackTrace();
	    }
	    return rs;
	  }
}
