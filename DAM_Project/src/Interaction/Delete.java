package Interaction;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import java.sql.*;
import java.lang.reflect.*;

import Annotation.Table;
import Annotation.Primarykey;

public class Delete extends QueryTemplate {

	private Object o;

	public Delete(Object o) {
		this.o = o;
	}

	@Override
	public String doTranslate(Connection conn)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		String resString = "DELETE FROM ";
		Class<?> clazz = this.o.getClass();
		Annotation tableAnnotation = clazz.getAnnotation(Table.class);
		String tableName = ((Table) tableAnnotation).name();
		resString += tableName + " WHERE ";
		Field[] fields = clazz.getDeclaredFields();
		String primarykey = "";
		for (Field f : fields) {
			Annotation[] annols = f.getAnnotations();
			for (Annotation a : annols) {
				if (a instanceof Primarykey) {
					primarykey = ((Primarykey) a).primarykey();
				}
			}
		}
		resString += primarykey;
		Object propType = PropertyUtils.getPropertyType(this.o, primarykey);
		Object propValue = PropertyUtils.getProperty(this.o, primarykey);
		if ((propType.toString().equals(Date.class.toString()) == true)
				|| (propType.toString().equals(String.class.toString()) == true)
				|| (propType.toString().equals(Timestamp.class.toString()) == true)) {
			resString += " = '" + propValue + "'";
		} else {
			resString += " = " + propValue;
		}

		return resString;
	}

	@Override
	public ArrayList mappingData(ResultSet rs, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
