package Interaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import Annotation.Table;
import ConnectDB.Session;

public class Insert extends QueryTemplate {

	private Object o;

	public Insert(Object o) {
		this.o = o;
	}

	@Override
	public String doTranslate(Connection conn)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
		// TODO Auto-generated method stub
		String resString = "resString = insert into ";
		Class<?> clazz = this.o.getClass();
		Field[] fields = clazz.getDeclaredFields();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT TOP 2 * FROM DEPARTMENT");
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		ArrayList<String> columnNamesDB = new ArrayList<String>();
		for (int i = 1; i <= numberOfColumns; i++) {
			columnNamesDB.add(rsmd.getColumnName(i));
		}

		Annotation tableAnnotation = clazz.getAnnotation(Table.class);
		String tablename = ((Table) tableAnnotation).name();
		resString += tablename + "values (";

		PropertyDescriptor[] objDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
		int i = 1;
		for (String columnName : columnNamesDB) {
			for (PropertyDescriptor objDes : objDescriptors) {

				String propertyName = objDes.getName();
				Object propType = PropertyUtils.getPropertyType(this.o, propertyName);
				Object propValue = PropertyUtils.getProperty(this.o, propertyName);

				if (columnName.equals(propertyName) == true) {
					if (i != (objDescriptors.length - 1)) {
						if (propertyName.equals("class") == false) {
							if (propType.toString().equals(String.class.toString()) == true) {
								resString += "'" + propValue + "', ";
							} else {
								if (propType.toString().equals(Date.class.toString()) == true
										|| propType.toString().equals(Time.class.toString()) == true) {
									resString += "'" + propValue + "', ";
								} else {
									resString += propValue + ", ";
								}
							}
						}
					} else {
						if (propertyName.equals("class") == false) {
							if (propType.toString().equals(String.class.toString()) == true) {
								resString += "'" + propValue + "',)";
							} else {
								if (propType.toString().equals(Date.class.toString()) == true
										|| propType.toString().equals(Time.class.toString()) == true) {
									resString += "'" + propValue + "',)";
								} else {
									resString += propValue + ",)";
								}
							}
						}
					}
				}
			}
			i++;
		}
		resString += ";";
		return resString;
	}

	@Override
	public ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz) {
		// TODO Auto-generated method stub

		return null;
	}

}
