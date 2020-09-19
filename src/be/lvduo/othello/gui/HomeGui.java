package be.lvduo.othello.gui;

import java.util.Optional;

import be.lvduo.othello.Main;
import be.lvduo.othello.player.OpponentType;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
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
	private GameOptions gameOptions = new GameOptions(OpponentType.HUMAN, 0);
	private Stage stage;
	
	public HomeGui() {
		Button newGame = new Button("New game");
		newGame.setOnAction(e -> {
			Optional<GameOptions> opt = this.createDialog();
			if(opt != null && opt.isPresent()) {
				this.gameOptions = opt.get();
				if(opt.get().getOpponentType() == OpponentType.ONLINE) {
					new NicknamePanel().setScene(this.stage);
					// new OnlinePanel().setScene(this.stage);
				} else
					new BoardGui(opt.get()).setScene(this.stage);
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
		
		Slider slider = new Slider(0, 15, this.gameOptions.getDifficulty());
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setBlockIncrement(1);
		slider.setSnapToTicks(true);
		slider.setTooltip(new Tooltip("How high is the difficulty how much time it needs to makes his decision."));
		slider.setDisable(true);
		
		opponent.valueProperty().addListener((obs, old, value) -> {
			slider.setDisable(value != OpponentType.COMPUTER);
		});
		opponent.setValue(this.gameOptions.getOpponentType());
		
		VBox pane = new VBox(20, opponent, slider);
		pane.setAlignment(Pos.CENTER);
		
		game.getDialogPane().setContent(pane);
		game.setResultConverter(buttonType -> buttonType == ButtonType.CANCEL ? null : 
			(this.gameOptions = new GameOptions(opponent.getValue(), (int) Math.round(slider.getValue()))));
		
		return game.showAndWait();
	}
}
