package MappingORM;

import java.lang.reflect.*;
import java.lang.annotation.*;
import Annotation.*;
import ConnectDB.*;
import java.sql.*;
import java.util.ArrayList;

public class Mapping {

	public boolean checkPrimaryKye(Object o, Session s) throws SQLException {
		Connection conn = s.getConnection();

		return true;
	}

	public boolean checkMapping(Class<?> c, Connection con) throws SQLException {
		// Create connection
		Connection conn = con;

		// Get class frame from object
		Class<?> clazz = c;

		// Get annotation info of object's class (object o which is on
		// parameter)
		// Get table annotation of object's class
		Annotation tableAnnotation = clazz.getAnnotation(Table.class);
		String tablename = ((Table) tableAnnotation).name();
		ArrayList<String> columnNames = new ArrayList<String>();

		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Annotation annotation = fields[i].getDeclaredAnnotation(Column.class);

			if (annotation instanceof Column) {
				Column columnAnnotation = (Column) annotation;
				// System.out.println(columnAnnotation.name());
				String columnName = columnAnnotation.name();
				columnNames.add(columnName);
			}
		}
		String primaryKey = null;
		// Get info primarykey from user
		for (int i = 0; i < fields.length; i++) {
			Annotation pkeyAnnotation = fields[i].getDeclaredAnnotation(Primarykey.class);
			if (pkeyAnnotation != null) {
				String columnName = fields[i].getName();
				if (pkeyAnnotation instanceof Primarykey) {
					Primarykey pkeyanno = (Primarykey) pkeyAnnotation;
					primaryKey = pkeyanno.primarykey();
				}
			}
		}

		// Get info about column name and type from database and check
		// Get primarykey
		DatabaseMetaData databaseMetaData = conn.getMetaData();

		String catalog = null;
		String schema = null;
		String primaryColumnName = null;

		ResultSet result = databaseMetaData.getPrimaryKeys(catalog, schema, tablename);

		while (result.next()) {
			primaryColumnName = result.getString(4);
		}

		// Check mapping
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM " + tablename);
		if (rs == null) {
			return false;
		} else {
			// Get column name from database
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList<String> columnNamesDB = new ArrayList<>(); // columnNamesDB
																	// contain
																	// column
																	// name in
																	// table

			int n = rsmd.getColumnCount();
			for (int i = 1; i <= n; i++) {
				columnNamesDB.add(rsmd.getColumnName(i));
			}

			if (fields.length != n) {
				return false;
			} else {
				// check primarykey
				if (primaryKey.equals(primaryColumnName) == false) {
					return false;
				} else {
					// check column name of class and table
					for (int i = 0; i < fields.length; i++) {
						if (columnNames.get(i).equals(columnNamesDB.get(i)) == false) {
							return false;
						}
					}
				}
				return true;
			}
		}
	}
}
