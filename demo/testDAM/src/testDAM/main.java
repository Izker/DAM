package testDAM;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import ConnectDB.*;
import Interaction.*;
import MappingORM.*;
import Annotation.Table;



public class main {
	
	public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		SessionFactory f = SQLServer.Instance("D:/workspace2/testDAM/src/config.xml");
		Session s = f.createSession();
		DEPARTMENT dep = new DEPARTMENT(4, "FINANCIAL7", "FCA6", "HOCHIMINH");
		Class<?> clazz = dep.getClass();
		
		ArrayList<Class<?>> resList = new ArrayList<Class<?>>();
		
		QueryTemplate qt1 = new Insert(dep);
		qt1.executeQuery(s, clazz, resList);
		
		QueryTemplate qt2 = new Delete(dep);
		qt2.executeQuery(s, clazz, resList);
		
		QueryTemplate qt3 = new Update(dep);
		qt3.executeQuery(s, clazz, resList);
	}

}
