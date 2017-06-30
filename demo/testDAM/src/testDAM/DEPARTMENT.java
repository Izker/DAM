package testDAM;

import java.sql.Date;
import Annotation.*;

@Table(name = "DEPARTMENT")
public class DEPARTMENT {
	
	@Primarykey(primarykey = "DEPT_ID")
	@Column(name = "DEPT_ID")
	private int DEPT_ID;

	@Column(name = "DEPT_NAME")
	private String DEPT_NAME;
	
	@Column(name = "DEPT_NO")
	private String DEPT_NO;
	
	@Column(name = "LOCATION")
	private String LOCATION;
	
	public int getDEPT_ID() {
		return DEPT_ID;
	}

	public void setDEPT_ID(int dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}

	public String getDEPT_NAME() {
		return DEPT_NAME;
	}

	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}

	public String getDEPT_NO() {
		return DEPT_NO;
	}

	public void setDEPT_NO(String dEPT_NO) {
		DEPT_NO = dEPT_NO;
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}
	
	public DEPARTMENT(int DEPT_ID, String DEPT_NAME, String DEPT_NO, String LOCATION){
		this.DEPT_ID = DEPT_ID;
		this.DEPT_NAME = DEPT_NAME;
		this.DEPT_NO = DEPT_NO;
		this.LOCATION = LOCATION;
	}

}