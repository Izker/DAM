package Interaction;

import ConnectDB.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import MappingORM.*;
//import Annotation.*;
//import java.lang.annotation.*;
//import java.lang.reflect.*;

public abstract class QueryTemplate {

	public boolean executeQuery(Session s, Class<?> clazz, ArrayList<Class<?>> resList)
			throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Connection conn = s.getConnection();
		Mapping checkMapping = new Mapping();
		if (checkMapping.checkMapping(clazz, conn) == true) {
			String queryString = this.doTranslate(conn);
			System.out.println(queryString);
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			// rs = stmt.executeQuery(queryString);
			boolean res = stmt.execute(queryString);
			rs = stmt.getResultSet();
			if (rs != null) {

				resList = mappingData(rs, clazz);
			}

			if (res == true) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public abstract String doTranslate(Connection conn)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException;

	public abstract ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz);

}
