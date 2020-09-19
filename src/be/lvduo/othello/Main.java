package be.lvduo.othello;

import be.lvduo.othello.gui.HomeGui;
import be.lvduo.othello.online.Client;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Client client;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("Starting Othello game");
		stage.setOnCloseRequest(e -> {
			e.consume();
			System.exit(0);
		});
		HomeGui.HOME.setScene(stage);
		stage.show();
		stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/logo.png")));
	}

}
