package ConnectDB;

import java.sql.*;

public class Session {
	private Connection con;
	
	public Session(Connection con){
		this.con = con;
	}
	
//	public Session getSession(){
//	
//	}
	
	public Connection getConnection(){ 
		return this.con;
	}
	
	public void closeSession(){ 
		
	}
	
	public void executeQuery(){
		
	}
	
	public boolean PropertyMapping(Mapping m, Object o){
		return m.checkMapping(o);
	}
	
}
