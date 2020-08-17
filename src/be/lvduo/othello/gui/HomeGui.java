package be.lvduo.othello.gui;

import java.util.Optional;

import be.lvduo.othello.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeGui implements IGui {

	public static final HomeGui HOME = new HomeGui();
	
	private static final double WIDTH = 1000;
	private static final double HEIGHT = 750;
	
	private Scene home;
	private GameOptions gameOptions = new GameOptions();
	private Stage stage;
	
	public HomeGui() {
		Button newGame = new Button("New game");
		newGame.setOnAction(e -> {
			Optional<GameOptions> opt = this.createDialog();
			if(opt != null && opt.isPresent()) {
				new BoardGui(this.gameOptions = opt.get()).setScene(this.stage);
			}
		});
		Button stats = new Button("Statistics");
		VBox pane = new VBox(15, newGame, stats);
		Image img = new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/home.JPG"));
		BackgroundSize size = new BackgroundSize(-1, -1, true, true, false, true);
		pane.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				size)));
		pane.setAlignment(Pos.CENTER);
		this.home = new Scene(pane, WIDTH, HEIGHT);
	}
	
	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Home");
		stage.setScene(this.home);
		stage.setResizable(false);
		this.stage = stage;
	}
	
	private Optional<GameOptions> createDialog() {
		Dialog<GameOptions> game = new Dialog<>();
	//	game.setGraphic(new ImageView(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/game-options.jpg"))));
	//	game.setHeaderText(null);
		
		return Optional.of(this.gameOptions); //game.showAndWait();
	}
}
