package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;

public class dbguiController {
	public Connection conn;
	
	public void initiateConnection() throws SQLException, IOException{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException x){
			System.out.println("Driver could not be loaded");
		}
		String DBINFO = "jdbc:oracle:thin:@apollo.vse.gmu.edu:1521:ite10g";
		String DBUSERNAME = "cparupal";
		String DBPASSWORD = "ophupu";
		this.conn = DriverManager.getConnection(DBINFO,DBUSERNAME,DBPASSWORD);
	}
	
	@FXML
	private TextField ssn;
	@FXML
	private Label invalidSsn;
	//Textfields that save all the attributes info that manager inputs
	@FXML
	private TextField fname;
	@FXML
	private TextField minit;
	@FXML
	private TextField lname;
	@FXML
	private TextField empssn;
	@FXML
	private TextField bdate;
	@FXML
	private TextField addr;
	@FXML
	private TextField superssn;
	@FXML
	private TextField salary;
	@FXML
	private TextField dno;
	@FXML
	private TextField email;
	@FXML
	private TextField sex;
	@FXML 
	private Label validateEmp;
		
	public void validSsn(ActionEvent event) throws SQLException, IOException{
		
		String ssninput = ssn.getText();
		initiateConnection();
		Statement stmnt = conn.createStatement();
		ResultSet r = stmnt.executeQuery("select ssn from employee, department where ssn=mgrssn and ssn='"+ssninput+"'");
		if(r.next()) {
			String ssni = r.getString("ssn");
			if(ssni.equalsIgnoreCase(ssninput)) {
				System.out.println("Manager SSN: "+ ssni);
				invalidSsn.setText("Valid SSN");
				Parent newPage = FXMLLoader.load(getClass().getResource("/application/newEmpInfo.fxml"));
	            Scene scene = new Scene(newPage);
	            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            appStage.setScene(scene);
	            appStage.show();
	            stmnt.close();
	    		conn.close();
	    		return;
			}
			else {
				invalidSsn.setText("Invalid SSN");
			}
			
			
		}
		else {
			invalidSsn.setText("Invalid SSN");
		}
		stmnt.close();
		conn.close();
		return;
	}
	
	/*
	 * *******************************************************************************************************************
	 */
	
	
	  public void readEmployee(ActionEvent event) throws SQLException, ParseException, IOException {
		System.out.println();
		String firstname = null;
		String middleinit = null;
		String employeessn = null;
		String lastname = null;
		String address = null;
		String gender = null;
		String supervisorssn = null;
		String emailaddr = null;
		java.sql.Date date = null;
		int empsalary = 0;
		int depno = 0;

		System.out.println("New Employee Information");
		firstname = fname.getText();
		System.out.println("First Name: " + firstname);
		middleinit = minit.getText();
		System.out.println("Middle Initial: " + middleinit);
		lastname = lname.getText();
		System.out.println("Last Name: " + lastname);
		employeessn = empssn.getText();
		if(validateNewEmp(employeessn) == false) {
			validateEmp.setText("Enter a valid SSN!");
			//return;
		}
		System.out.println("Employee SSN: " + employeessn);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date temp = sdf.parse(bdate.getText());
		date = new java.sql.Date(temp.getTime());
		System.out.println("Birthdate: " + date);
		
		address = addr.getText();
		System.out.println("Address: "+ address);
		gender = sex.getText();
		System.out.println("Sex: "+ gender);
		
		
		String temp3 = salary.getText();
		empsalary = Integer.parseInt(temp3);
		System.out.println("Salary: " + empsalary);
		
		supervisorssn = superssn.getText();
		System.out.println("Supervisor SSN: " + supervisorssn);
		
		String temp2 = dno.getText();
		depno = Integer.parseInt(temp2);
		System.out.println("Department No.: " + depno);
		
		emailaddr = email.getText();
		System.out.println("Email Address: " + emailaddr);
		
		/////////////////////////////////////////////////////////////////////
		initiateConnection();
		String query = "insert into employee values(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmnt = conn.prepareStatement(query);
		stmnt.clearParameters();
		stmnt.setString(1, firstname);
		stmnt.setString(2, middleinit);
		stmnt.setString(3, lastname);
		stmnt.setString(4, employeessn);
		stmnt.setDate(5, date);
		stmnt.setString(6, address);
		stmnt.setString(7, gender);
		stmnt.setInt(8, empsalary);
		stmnt.setString(9, supervisorssn);
		stmnt.setInt(10, depno);
		stmnt.setString(11, emailaddr);
		stmnt.executeUpdate();
		stmnt.close();	
		conn.close();
		
		

	    //opens the next page
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/empProjects.fxml"));
		Parent newPage = loader.load();
		projController controller = loader.getController();
		controller.getSsn(employeessn);
		Scene scene = new Scene(newPage);
	    Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    appStage.setScene(scene);
	    appStage.show();
	    
	}
	  
	  public boolean validateNewEmp(String ssn) throws SQLException, IOException {
		  if(ssn.trim().equals("")) {
			  return false;
		  }
		  initiateConnection();
		  String query = "select fname, lname, ssn from employee where ssn='"+ssn+"'";
		  Statement stmnt = conn.createStatement();
		  ResultSet rs = stmnt.executeQuery(query);
		  if(rs == null) {
			  return true;
		  }
		  return false;
		  
	  }
	  
	  
	  
}
