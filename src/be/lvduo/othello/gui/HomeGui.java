package be.lvduo.othello.gui;

import be.lvduo.othello.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeGui {

	public static final HomeGui HOME = new HomeGui();
	
	private static final double WIDTH = 500;
	private static final double HEIGHT = 500;
	
	private Scene home;
	
	public HomeGui() {
		Button newGame = new Button("New game");
		VBox pane = new VBox(15, newGame);
		Image img = new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/home.JPG"));
		BackgroundSize size = new BackgroundSize(1, 1, true, true, true, false);
		pane.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				size)));
		pane.setAlignment(Pos.CENTER);
		this.home = new Scene(pane, WIDTH, HEIGHT);
	}
	
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Home");
		stage.setScene(this.home);
	}
}
