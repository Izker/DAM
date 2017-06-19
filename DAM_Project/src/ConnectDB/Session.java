package ConnectDB;

import java.sql.*;

public class Session {
	private Connection con;
	
	public Session(Connection con){
		this.con = con;
	}
	
	public Connection getConnection(){ 
		return this.con;
	}
	
	public void closeSession() throws SQLException{ 
		con.close();
	}
	
	public void executeQuery(){
		
	}
	
}
