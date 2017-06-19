package ConnectDB;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServer extends SessionFactory {
	private Configuration config;

	private static SQLServer curinstance;

	protected SQLServer(String path) throws SQLException {
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

	public static SQLServer Instance(String path) throws SQLException {

		if (curinstance == null) {
			curinstance = new SQLServer(path);
		}
		return curinstance;

	}

}
