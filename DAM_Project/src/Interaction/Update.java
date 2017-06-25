package Interaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import Annotation.*;

public class Update extends QueryTemplate{

	Object o;
	
	@Override
	public String doTranslate(Connection conn) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		String resString = "UPDATE ";
		Class<?> clazz = this.o.getClass();

		Annotation tableAnnotation = clazz.getAnnotation(Table.class);
		String tableName = ((Table) tableAnnotation).name();
		resString += tableName + " SET ";
		Field[] fields = clazz.getDeclaredFields();
		String pkey = "";
		for (Field f : fields) {
			Annotation[] annols = f.getAnnotations();

			for (Annotation a : annols) {
				if (a instanceof Primarykey) {
					pkey = ((Primarykey) a).primarykey();
				}

				if (a instanceof Column) {
					String propName = ((Column) a).name();
					Object propType = PropertyUtils.getPropertyType(this.o, propName);
					Object propValue = PropertyUtils.getProperty(this.o, propName);

					if ((propType.toString().equals(Date.class.toString()) == true)
							|| (propType.toString().equals(String.class.toString()) == true)
							|| (propType.toString().equals(Timestamp.class.toString()) == true)) {
						resString += propName + " = '" + propValue + "' ";
					} else {
						resString += propName + " = " + propValue + " ";
					}
				}
			}
		}

		resString += "WHERE ";
		Object pkeyType = PropertyUtils.getPropertyType(this.o, pkey);
		Object pkeyValue = PropertyUtils.getProperty(this.o, pkey);
		if ((pkeyType.toString().equals(Date.class.toString()) == true)
				|| (pkeyType.toString().equals(String.class.toString()) == true)
				|| (pkeyType.toString().equals(Timestamp.class.toString()) == true)) {

			resString += pkey + " = '" + pkeyValue + "';";
		} else {
			resString += pkey + " = " + pkeyValue + ";";
		}
		return resString;
	}

	@Override
	public ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
