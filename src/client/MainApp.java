package client;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.Socket;

import controller.ChatController;
import controller.LoginController;
import controller.MainController;
import controller.SignUpController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Group;
import javafx.scene.Scene;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane window;
	private ClientMonitor mon;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Chat Client");
		Socket s;
		try {
			s = new Socket("localhost", 30000);
			mon = new ClientMonitor();
			new ClientReadThread(s, mon);
			new ClientWriteThread(s, mon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ParentPane.fxml"));
			window = (BorderPane) loader.load();
			Scene scene = new Scene(window, 600, 600);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setScene(scene);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					mon.storeMessage("Q");
					Platform.exit();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		showMain();
	}

	public void showMain() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			window.setCenter(anchorPane);
			MainController controller = loader.getController();
			controller.setMain(this);
			controller.setMonitor(mon);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void showLoginPane() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			AnchorPane anchor = (AnchorPane) loader.load();
			window.setCenter(anchor);
			LoginController controller = loader.getController();
			controller.setMain(this);
			controller.setMonitor(mon);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showSignUpPane() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
			AnchorPane anchor = (AnchorPane) loader.load();
			window.setCenter(anchor);
			SignUpController controller = loader.getController();
			controller.setMain(this);
			controller.setMonitor(mon);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showChatPane() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Chat.fxml"));
			AnchorPane anchor = (AnchorPane) loader.load();
			window.setCenter(anchor);
			ChatController controller = loader.getController();
			controller.setMain(this);
			controller.setMonitor(mon);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Application.launch(args);
		
	}

	public void showDialog(String name) {
		Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(new Group(new Text(25, 20, "This is a test: " + name)), 300, 100);
		dialog.setTitle("WinDialog");
		dialog.setScene(scene);
		dialog.show();
	}
	
	public Stage getPrimaryStage() {
        return primaryStage;
    }

}