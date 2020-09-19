package be.lvduo.othello.gui;

import be.lvduo.othello.Main;
import be.lvduo.othello.online.Client;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NicknamePanel implements IGui {
	
	private static final double WIDTH = 300, HEIGHT = 200;
	
	private Stage stage;
	private Scene scene;
	
	private TextField name = new TextField();

	public NicknamePanel() {
		
		Text title = new Text("Chose Your Name");
		title.getStyleClass().add("title");
		HBox header = new HBox(title);
		header.setAlignment(Pos.CENTER);
		
		name.setPromptText("nickname");
		name.textProperty().addListener((obs, old, text) -> {
			name.setText(text);
			if(text.length() > 12 || text.length() < 2)
				name.getStyleClass().add("not-good");
			else
				name.getStyleClass().remove("not-good");
		});
		name.getStyleClass().add("not-good");
		name.getStyleClass().add("textfield");
		name.setMaxSize(200, 30);
		
		Button submit = new Button("Submit");
		submit.setOnMouseClicked((e) -> {
			if(name.getText().length() > 2) {
				Main.client.setHome(this);
				Main.client.sendName(name.getText());
			}
		});
		submit.getStyleClass().add("submit-b");
		
		VBox container = new VBox(10, name, submit);
		container.setAlignment(Pos.CENTER);
		
		BorderPane pane = new BorderPane();
		pane.setTop(header);
		pane.setCenter(container);
		this.scene = new Scene(pane, WIDTH, HEIGHT);
		this.scene.getStylesheets().add("be/lvduo/othello/gui/css/nickname.css");
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
		stage.centerOnScreen();
		Main.client = new Client("127.0.0.1", 8080);
		this.stage = stage;
	}

}
