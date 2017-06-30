package ConnectDB;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Configuration {
	private String conString;
	
	//D:\workspace2\DAM_Project\src\Config.xml
	protected String readDBConfig(String path) {
		// TODO Auto-generated method stub
		Document document = null;
		File file = new File(path);
		String conString;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
			conString = document.getElementsByTagName("conString").item(0).getTextContent();
			return conString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getconString(String path){
		this.conString = readDBConfig(path);
		return this.conString;
	}
	
}
