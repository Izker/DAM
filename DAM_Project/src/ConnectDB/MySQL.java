package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends SessionFactory {
	private Configuration config;
	protected static MySQL curinstance;

	public MySQL(String path) throws SQLException{
		this.config = new Configuration();
		this.connectString = config.getconString();
		this.con = this.createConnection();
	}
	
	@Override
	public Session createSession() {
		// TODO Auto-generated method stub
		Session s = new Session(this.con);
		return s;
	}

	@Override
	protected Connection createConnection() throws SQLException {
		// TODO Auto-generated method stub
		String connectionURL = config.getconString();
		Connection conn = DriverManager.getConnection(connectionURL);
		return conn;
	}
	
	public static MySQL Instance(String path) throws SQLException {
		if(curinstance == null){
			curinstance = new MySQL(path);
		}
		return curinstance;
	}
}
