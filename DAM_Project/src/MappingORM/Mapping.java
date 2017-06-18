package MappingORM;

import java.lang.reflect.*;
import java.lang.annotation.*;
import Annotation.*;
import ConnectDB.*;

import java.sql.*;
import java.util.ArrayList;

public class Mapping {
	public boolean checkMapping(Object o, Session s) throws SQLException {
		// Create connection
		Connection conn = s.getConnection();

		// Get class frame from object
		Class<?> clazz = o.getClass();

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

		// Get info about column name and type from database
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM " + tablename);
		if (rs == null) {
			return false;
		} else {
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList<String> columnNamesDB = new ArrayList<>();

			int n = rsmd.getColumnCount();
			for (int i = 1; i <= n; i++) {
				columnNamesDB.add(rsmd.getColumnName(i));
			}

			if (fields.length != n) {
				return false;
			} else {
				for (int i = 0; i < fields.length; i++) {
					if (columnNames.get(i) != columnNamesDB.get(i)) {
						return false;
					}
				}
				return true;
			}
		}
	}
}
