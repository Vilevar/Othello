package be.lvduo.othello.gui;

import java.util.Optional;

import be.lvduo.othello.Main;
import be.lvduo.othello.player.OpponentType;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeGui implements IGui {

	public static final HomeGui HOME = new HomeGui();
	
	private static final double WIDTH = 1000;
	private static final double HEIGHT = 750;
	
	private Scene home;
	private GameOptions gameOptions = new GameOptions(OpponentType.HUMAN, 0, false);
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
		game.setGraphic(new ImageView(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/game-options.jpg"))));
		game.setHeaderText("Chose the options for your new game");
		game.setTitle("Game options");
		game.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		
		ChoiceBox<OpponentType> opponent = new ChoiceBox<>(FXCollections.observableArrayList(OpponentType.values()));
		opponent.setValue(this.gameOptions.getOpponentType());
		
		Slider slider = new Slider(0, 5, this.gameOptions.getDifficulty());
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(1);
		slider.setBlockIncrement(0.1);
		slider.setDisable(true);
		
		CheckBox max = new CheckBox("Big boss");
		max.setSelected(this.gameOptions.isBoss());
		max.setDisable(true);
		
		opponent.valueProperty().addListener((obs, old, value) -> {
			slider.setDisable(value != OpponentType.COMPUTER);
			max.setDisable(value != OpponentType.COMPUTER);
		});
		max.selectedProperty().addListener((obs, old, value) -> {
			slider.setDisable(value);
		});
		
		VBox pane = new VBox(20, opponent, new HBox(10, slider, max));
		pane.setAlignment(Pos.CENTER);
		
		game.getDialogPane().setContent(pane);
		game.setResultConverter(buttonType -> buttonType == ButtonType.CANCEL ? null : 
			(this.gameOptions = new GameOptions(opponent.getValue(), slider.getValue(), max.isSelected())));
		return game.showAndWait();
	}
}
