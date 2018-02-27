package controller;

import client.ClientMonitor;
import client.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SignUpController {
	private MainApp main;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	
	private ClientMonitor mon;
	
	
	public void setMain(MainApp main) {
		this.main = main;
		
	}
	
	public void setMonitor(ClientMonitor mon) {
		this.mon = mon;
	}
	
	@FXML
	public void backButtonClicked(ActionEvent e){
		main.showMain();
	}
	
	@FXML
	public void signUpButtonClicked(ActionEvent e) {
		if(usernameField != null && passwordField != null) {
			String username = usernameField.getText().trim();
			String password = passwordField.getText();
			if(!username.equals("") && !password.equals("")) {
				mon.storeMessage("S"+username+"*"+password);
				Platform.runLater(() -> {
					String answer = mon.getLogin();
					if(answer.equals("1")) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.initOwner(main.getPrimaryStage());
						alert.setTitle("Success");
						alert.setHeaderText("User created!");
						alert.showAndWait();
						main.showMain();
						mon.storeMessage("G");
					} else if(answer.equals("0")){
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.initOwner(main.getPrimaryStage());
						alert.setTitle("Non successful sign up");
						alert.setHeaderText("Username already exists");
						alert.showAndWait();
					}
				});
				
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(main.getPrimaryStage());
				alert.setTitle("Field can not be empty");
				alert.setHeaderText("Field can not be empty");
				alert.showAndWait();
			}	
		}
		else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Field can not be empty");
			alert.setHeaderText("Field can not be empty");
			alert.showAndWait();
		}
	}
}
