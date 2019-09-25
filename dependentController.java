package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;

public class dependentController implements Initializable {
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
	private Label essn;
	@FXML
	private TableView<Dependent> tableView;
	@FXML
	private TableColumn<Dependent, String> essnCol;
	@FXML
	private TableColumn<Dependent, String> nameCol;
	@FXML
	private TableColumn<Dependent, String> sexCol;
	@FXML
	private TableColumn<Dependent, String> dateCol;
	@FXML
	private TableColumn<Dependent, String> relationCol;
	
	//fields that hold dependent information
	@FXML
	private TextField dependentName;
	@FXML
	private TextField gender;
	@FXML
	private TextField birthdate;
	@FXML
	private TextField relationship;
	
	public void setSsn(String ssn) {
		essn.setText(ssn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		essnCol.setCellValueFactory(new PropertyValueFactory<Dependent, String>("essn"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Dependent, String>("depName"));
		sexCol.setCellValueFactory(new PropertyValueFactory<Dependent, String>("sex"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Dependent, String>("bdate"));
		relationCol.setCellValueFactory(new PropertyValueFactory<Dependent, String>("relationship"));
	}
	
	public void addDependent(ActionEvent event) throws ParseException, SQLException, IOException {
		System.out.println();
		java.sql.Date date;
		System.out.println("Dependent Information");
		System.out.println("Employee SSN: " + essn.getText());
		String name = dependentName.getText();
		System.out.println("Name:" + name);
		String sex = gender.getText();
		System.out.println("Sex:" + sex);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date temp = sdf.parse(birthdate.getText());
		date = new java.sql.Date(temp.getTime());
		System.out.println("Birthdate: " + date);
		String relation = relationship.getText();
		System.out.println("Relationship: " + relation);
		
		Dependent d = new Dependent(essn.getText(), name, sex, 
									birthdate.getText(), relation);
		tableView.getItems().add(d);
		
		initiateConnection();
		String empssn = essn.getText();
		initiateConnection();
		String query = "insert into dependent values(?,?,?,?,?)";
		PreparedStatement stmnt = conn.prepareStatement(query);
		stmnt.clearParameters();
		stmnt.setString(1, empssn);
		stmnt.setString(2, name);
		stmnt.setString(3, sex);
		stmnt.setDate(4, date);
		stmnt.setString(5, relation);
		stmnt.executeUpdate();
		stmnt.close();
		conn.close();
		
	}
	
	public void onDonePushed(ActionEvent event) {
		System.exit(0);
	}

}
