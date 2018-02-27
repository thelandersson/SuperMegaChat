package controller;
import client.ClientMonitor;
import client.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;

public class MainController {
	private MainApp main;
	private ClientMonitor mon;
	
	public void setMain(MainApp main) {
		this.main = main;
	}
	
	public void setMonitor(ClientMonitor mon) {
		this.mon = mon;
		
	}
	
	@FXML
	public void loginButtonClicked(ActionEvent e) {
		main.showLoginPane();
	}
	
	@FXML
	public void signUpButtonClicked(ActionEvent e) {
		main.showSignUpPane();
	}

	@FXML
	public void quitButtonClicked(ActionEvent e) {
		mon.storeMessage("Q");
		Platform.exit();
	}

	

}