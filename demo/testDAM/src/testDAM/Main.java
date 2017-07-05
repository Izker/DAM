package testDAM;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

import ConnectDB.SQLServer;
import ConnectDB.Session;
import ConnectDB.SessionFactory;
import Interaction.Delete;
import Interaction.Insert;
import Interaction.QueryTemplate;
import Interaction.SQLQuery;
import Interaction.Update;

public class Main {
	public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		// TODO Auto-generated method stub
		SessionFactory f = SQLServer.Instance("src/config.xml");
		Session s = f.createSession();
		DEPARTMENT dep = new DEPARTMENT(46, "FINANCIAL7", "FCA46", "HOCHIMINH");
		Class<?> clazz = dep.getClass();

		ArrayList<Class<?>> resList = new ArrayList<Class<?>>();

		QueryTemplate qt1 = new Insert(dep);
		qt1.executeQuery(s, clazz, resList);

		QueryTemplate qt2 = new Delete(dep);
		qt2.executeQuery(s, clazz, resList);

		QueryTemplate qt3 = new Update(dep);
		qt3.executeQuery(s, clazz, resList);

		// Create Query Statement
		QueryTemplate qt4 = new SQLQuery().Select("*");
		((SQLQuery) qt4).From("@DEPARTMENT"); // Add From Statement to Query
		((SQLQuery) qt4).OrderBy("@DEPARTMENT.DEPT_ID"); // Add OrderBy
															// statement to
															// Query

		if (qt4.executeQuery(s, dep.getClass(), resList)) {
			for (Object d : resList) {
				System.out.println("-----------------------");
				if (d instanceof DEPARTMENT) {
					DEPARTMENT temp = (DEPARTMENT) d;
					System.out.println("ID: " + temp.getDEPT_ID());
					System.out.println("No. :" + temp.getDEPT_NO());
					System.out.println("Name : " + temp.getDEPT_NAME());
					System.out.println("Location: " + temp.getLOCATION());
				}
			}
		}
	}

}
