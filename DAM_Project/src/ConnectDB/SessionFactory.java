package ConnectDB;
import java.sql.*;

public abstract class SessionFactory {
	protected String connectString;
	protected Connection con;
	
	protected abstract Connection createConnection() throws SQLException;
	public abstract Session createSession() ;		
}
