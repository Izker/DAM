package Helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Annotation.Column;
import Annotation.Primarykey;
import Annotation.Table;
import QueryTemplate.SQLQuery;
/*
 * @Author: Chow Chow
 * @Description: Process Param in Query statement
 * @Note: Still not handle errors when user type wrong input  
 * 
 */
public class ProcessParams {
	private static String cRegex = "(?<!@)@\\w+.\\w+";
	private static String tRegex = "(?<!@)@\\w+";
	private static String numRegex = "(?<!@)@\\d+";
	
	public static String ProcessParamToSql(String input, ArrayList<Object> params){
		Pattern tPattern = Pattern.compile(numRegex);
		Matcher m = tPattern.matcher(input);
		while(m.find()){
			String c = m.group();
			c = c.substring(1);			
			//Number Param
			try{
				int num = Integer.parseInt(c);
				if(params.isEmpty() || params == null){
					break;
				}
				Object obj = params.get(num);
				//System.out.println(num);
				//System.out.println(obj.toString());
				if(obj instanceof String){
					input = input.replaceFirst(numRegex,(String)obj);
					//System.out.println("String");
				}
				else{
					if(obj instanceof SQLQuery){
						SQLQuery myQuery = (SQLQuery)obj;
						input = input.replaceFirst(numRegex, myQuery.getSQL());
						//System.out.println("SQL");
					}
					else{
						input = input.replaceFirst(numRegex, obj.toString());
						//System.out.println("Object");
					}
				}
				
			}
			catch (NumberFormatException e){				
			}
		}
		input = ProcessClassToTable(input);
		input = ProcessAttrToColumn(input);
		
		return input;
	}
	/*
	 * @Description: process @ClassName to TableName
	 * @Output: SQL Statement
	 */
	public static String ProcessClassToTable(String input){
		Pattern tPattern = Pattern.compile(tRegex);
		Matcher m = tPattern.matcher(input);
		while(m.find()){
			String c = m.group();
			String className = c.substring(1);
			
			try{
				Class<?> clazz = Class.forName(className);
				Annotation tblAnno = clazz.getAnnotation(Table.class);
				String tblName = ((Table)tblAnno).name();
				input = input.replaceFirst(tRegex, tblName);
			}
			catch(ClassNotFoundException e) {
			    System.err.println(e);				    
			}
		}
		return input;
	}
	
	/*
	 * @Description: Process @ClassName.Attr to TableName.Colum
	 * @Output: SQl statement
	 */
	public static String ProcessAttrToColumn(String input){
		//String cRegex = "(?<!@)@\\w+.\\w+";
		Pattern cPattern = Pattern.compile(cRegex);
		Matcher m = cPattern.matcher(input);
		while(m.find()){
			//System.out.println(m.group());
			String c = m.group();
			//System.out.println(m.start() + " " + m.end());
			String className = c.substring(1,c.indexOf("."));
			String propName = c.substring(c.indexOf(".")+1,c.length());
			
			try{
				Class<?> clazz = Class.forName(className);
				Annotation tblAnno = clazz.getAnnotation(Table.class);
				String tblName = ((Table)tblAnno).name();
				String columnName = "";
				Field[] fields = clazz.getDeclaredFields();
				for(Field f : fields){
					//System.out.println(f.getName());
					if(f.getName().equals(propName)){
						//System.out.println(className + " " + propName );
						Annotation[] annots = f.getAnnotations();
						for(Annotation a : annots){
							if(a instanceof Column){
								columnName = ((Column)a).name();
								//System.out.println("Column name " + columnName);
							}
							if(a instanceof Primarykey){
								columnName = ((Primarykey)a).primarykey();
								//System.out.println("Annotation " + columnName);
							}							
						}						
						//m.replaceAll(tblName+"."+columnName);
						//System.out.println(m.start());
						input = input.replaceFirst(cRegex, tblName+"."+columnName);
						//System.out.println(input);	
					}
				}
				
				}
				catch (ClassNotFoundException e) {
				    System.err.println(e);				    
				}
		}
		return input;
	}
}
