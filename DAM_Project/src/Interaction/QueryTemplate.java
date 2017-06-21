package Interaction;

import ConnectDB.*;
import java.sql.*;
import java.util.ArrayList;
//import Annotation.*;
//import java.lang.annotation.*;
//import java.lang.reflect.*;

public abstract class QueryTemplate {

	public boolean executeQuery(Session s, Class<?> clazz, ArrayList<Class<?>> resList) throws SQLException {
		Connection conn = s.getConnection();
		String queryString = doTranslate();
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryString);
		
		if(rs == null){
			return false;
		} else {
			resList = mappingData(rs, clazz);
			return true;
		}
	}
	
	public abstract String doTranslate();
	public abstract ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz);

}
