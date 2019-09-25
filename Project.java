package application;

import java.math.BigDecimal;


public class Project {
	
	private String pname;
	private int pno;
	private BigDecimal hours;
	
	public Project(String pname, int pno, BigDecimal hours) {
		this.pname = pname;
		this.pno = pno;
		this.hours = hours;
	}

	public String getPname() {
		return pname;
	}
	

	public int getPno() {
		return pno;
	}

	public BigDecimal getHours() {
		return hours;
	}
	
	

}
