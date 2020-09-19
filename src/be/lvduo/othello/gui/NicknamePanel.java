package be.lvduo.othello.gui;

import be.lvduo.othello.Main;
import be.lvduo.othello.online.Client;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NicknamePanel implements IGui {
	
	private static final double WIDTH = 700, HEIGHT = 450;
	
	private Stage stage;
	private Scene scene;
	
	private TextField name = new TextField();

	public NicknamePanel() {
		
		name.setPromptText("nickname");
		name.textProperty().addListener((obs, old, text) -> {
			if(text.length() < 12)
				name.setText(text);
			else
				name.setText(text.substring(0, 12));
		});
		
		Button submit = new Button("Submit");
		submit.setOnMouseClicked((e) -> {
			Main.client.setHome(this);
			Main.client.sendName(name.getText());
		});
		
		BorderPane pane = new BorderPane();
		pane.setCenter(new VBox(10, name, submit));
		this.scene = new Scene(pane, WIDTH, HEIGHT);
	}
	
	public void rep(boolean b) {
		if(b) {
			Platform.runLater(() -> new OnlinePanel().setScene(this.stage));
		} else {
			Main.client.sendErrorMessage("this name is already exist, chose another name", false);
		}
	}
	
	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Online");
		stage.setScene(this.scene);
		stage.setResizable(false);
		Main.client = new Client("127.0.0.1", 8080);
		this.stage = stage;
	}

}
