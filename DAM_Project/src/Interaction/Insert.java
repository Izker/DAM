package Interaction;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Insert extends QueryTemplate{

	private Object o; 
	
	public Insert(Object o){
		this.o = o;
	}
	
	@Override
	public String doTranslate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Class<?>> mappingData(ResultSet rs, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
