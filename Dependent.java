package application;

public class Dependent {
	private String essn;
	private String depName;
	private String sex;
	private String bdate;
	private String relationship;
	public Dependent(String essn, String depName, String sex, String bdate, String relationship) {
		super();
		this.essn = essn;
		this.depName = depName;
		this.sex = sex;
		this.bdate = bdate;
		this.relationship = relationship;
	}
	public String getEssn() {
		return essn;
	}
	public String getDepName() {
		return depName;
	}
	public String getSex() {
		return sex;
	}
	public String getBdate() {
		return bdate;
	}
	public String getRelationship() {
		return relationship;
	}
	
	
}
