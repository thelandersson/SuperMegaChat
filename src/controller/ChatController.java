package controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import client.ClientMonitor;
import client.Logger;
import client.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChatController {
	private MainApp main;
	private Stage dialogStage;
	@FXML
	private Label fileLabel;
	@FXML
	private TextArea messageText;
	@FXML
	private TextArea inputMessages;
	
	private File file;
	private ClientMonitor mon;
	
	public void setMain(MainApp main) {
		this.main = main;
		
	}
	
	public void setMonitor(ClientMonitor mon) {
		this.mon = mon;
	}
	
	@FXML
	public void initialize() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(final int i) throws IOException {
				Platform.runLater(() -> inputMessages.appendText(String.valueOf((char) i)));
			}
		};
		Logger.setOutput(new PrintStream(out, true), inputMessages);
	}
 	
	@FXML
	public void backButtonClicked(ActionEvent e){
		main.showMain();
	}
	
	@FXML
	public void attachButtonClicked(ActionEvent e) {
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Open File");
	    file = chooser.showOpenDialog(new Stage());
	    if (file != null) {
	    		//fileLabel.setText(file.getName() + " is attached to next message");
	    		//System.out.println(file.getAbsolutePath());
	    		mon.storeMessage("i" + file.getAbsolutePath());
	    }
		

	}
	
	@FXML
	public void sendButtonClicked(ActionEvent e) {
		String text = messageText.getText();
		if(text.length() > 0) {
			inputMessages.appendText("You: " + text + "  (" + getDateAsString() +")" + "\n" );
			text = text + "*" + getDateAsString();
			mon.storeMessage("m " + text);
			messageText.clear();
			fileLabel.setText("");
		}
		
	}
	
	//denna behövs imp
	private String getDateAsString() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
