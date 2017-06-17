package ConnectDB;
import java.sql.*;

public abstract class SessionFactory {
	protected String connectString;
	protected Connection con;
	
	public abstract Connection createConnection() throws SQLException;
	public abstract Session createSession() ;		
}
