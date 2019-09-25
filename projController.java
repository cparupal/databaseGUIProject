package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.*;
import java.util.ResourceBundle;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

public class projController implements Initializable{
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
	private TableView<Project> tableView;
	@FXML
	private TableColumn<Project, String> pnameCol;
	@FXML 
	TableColumn<Project, Integer> pnoCol;
	@FXML
	private TableColumn<Project, BigDecimal> hoursCol;
	
	//variables dealing with user input
	@FXML 
	private TextField projName;
	@FXML
	private TextField numHours;
	@FXML
	private Label maxHours;
	double max = 0;
	
	//dependent options
	@FXML
	private CheckBox yes;
	@FXML
	private CheckBox no;
	@FXML 
	private Label select = new Label();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		pnameCol.setCellValueFactory(new PropertyValueFactory<Project, String>("pname"));
		pnoCol.setCellValueFactory(new PropertyValueFactory<Project, Integer>("pno"));
		hoursCol.setCellValueFactory(new PropertyValueFactory<Project, BigDecimal>("hours"));
		
		//load dummy data
		//tableView.setItems(getProjects());
		
	}
	
	/*public ObservableList<Project> getProjects() {
		ObservableList<Project> list = FXCollections.observableArrayList();
		list.add(new Project("ProductX", 1, new BigDecimal("20.0")));
		return list;
	}*/
	
	public void infoProj(ActionEvent e) throws SQLException, IOException {
		System.out.println();
	//first firgure out pno
		int projnum = 0;
		BigDecimal hrs = new BigDecimal(numHours.getText());
		max += Double.parseDouble(numHours.getText());
		if(max > 40) {
			max -= Double.parseDouble(numHours.getText());
			maxHours.setText("Adding this project exceeds employee's hours!");
			return;
		}
		else {
			maxHours.setText(" ");
		}
		System.out.println("Project Information");
		System.out.println("Employee SSN: " + essn.getText());
		String project = projName.getText();
		System.out.println("Project Name: " + project);
		initiateConnection();
		
		Statement stmnt = conn.createStatement();
		ResultSet rs = stmnt.executeQuery("select pnumber from project where pname='"+project+"'");
		if(rs.next()) {
			projnum = rs.getInt(1);
		}
		if(projnum == 0 || rs == null) {
			maxHours.setText("invalid project name!");
		}
		else {
			maxHours.setText(" ");
		}
		stmnt.close();
		conn.close();
		System.out.println("Project Number: " + projnum);
		System.out.println("Number of Hours: " + hrs);
		Project p = new Project(project, projnum, hrs);
		tableView.getItems().add(p);
		
		String empssn = essn.getText();
		initiateConnection();
		String query = "insert into works_on values(?,?,?)";
		PreparedStatement st = conn.prepareStatement(query);
		st.clearParameters();
		st.setString(1, empssn);
		st.setInt(2, projnum);
		st.setBigDecimal(3, hrs);
		st.executeUpdate();
		st.close();
		conn.close();
		
		
		
	}
	
	public void getSsn(String ssn) {
		essn.setText(ssn);
	}
	
	public void onSubmit(ActionEvent event) throws IOException {
		if(yes.isSelected()) {
			select.setText(" ");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/empDependents.fxml"));
			Parent newPage = loader.load();
			dependentController controller = loader.getController();
			controller.setSsn(essn.getText());
            Scene scene = new Scene(newPage);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
		}
		else if(no.isSelected()) {
			select.setText(" ");
			System.exit(0);
		}
		else {
			select.setText("Select an option: YES or NO");
		}
		
	}
	
	
	
	

}
