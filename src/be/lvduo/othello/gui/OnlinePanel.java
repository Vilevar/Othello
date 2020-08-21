package be.lvduo.othello.gui;

import java.util.ArrayList;
import java.util.List;

import be.lvduo.othello.Main;
import be.lvduo.othello.online.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class OnlinePanel implements IGui {
	
	private double WIDTH = 700, HEIGHT = 450;
	
	private Stage stage;
	private Scene scene;
	
	private VBox rightContainer = new VBox(new Label("Error, restart this app"));
	
	private Button playWith;
	private Button playRandom;
	private Button playAI;
	private List<User> users;
	
	private User opponent = null;
	
	public OnlinePanel() {
		
		this.update();
		
		BorderPane pane = new BorderPane();
		pane.setLeft(this.leftContainer());
		pane.setRight(this.rightContainer);
		this.scene = new Scene(pane, WIDTH, HEIGHT);
		this.scene.getStylesheets().add("be/lvduo/othello/gui/css/online.css");
	}
	
	private VBox leftContainer() {
		
		playWith = new Button("Play with ...");
		playWith.getStyleClass().add("button");
		playRandom = new Button("Random");
		playRandom.getStyleClass().add("button");
		playAI = new Button("AI");
		playAI.getStyleClass().add("button");
		
		VBox container = new VBox(50, playWith, playRandom, playAI);
		container.getStyleClass().add("left-container");
		container.setPrefWidth(WIDTH/2);
		container.setAlignment(Pos.CENTER);
		container.setBackground(new Background(new BackgroundImage(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/othello-online.jpg")), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(-1, -1, true, true, false, false))));
		return container;
	}
	
	
	public void update() {
		rightContainer();
	}
	
	
	private void rightContainer() {
		
		this.users = new ArrayList<>();
		
		this.users.add(new User("lyam"));	//exemple
		this.users.add(new User("marie"));	//exemple
		this.users.add(new User("loic"));	//exemple
		this.users.add(new User("Vilevar"));//exmeple

		VBox top = new VBox(new Label("Top"));
		top.getStyleClass().addAll("top", "head-c", "column");
		VBox name = new VBox(new Label("Nickname"));
		name.getStyleClass().addAll("name", "head-c", "column");
		VBox victories = new VBox(new Label("Victories"));
		victories.getStyleClass().addAll("victories", "head-c", "column");
	//	VBox ratio = new VBox(new Label("Ratio"));
	//	ratio.getStyleClass().addAll("ratio", "head-c");
		VBox points = new VBox(new Label("Points"));
		points.getStyleClass().addAll("points", "head-c", "column");
		VBox status = new VBox(new Label("Status"));
		status.getStyleClass().addAll("status", "head-c", "column");
		HBox headLine = new HBox(top, name, victories, points, status);
		headLine.setStyle("-fx-padding: 0 0 5px 0;");
		
		VBox board = new VBox(headLine);
		board.setAlignment(Pos.CENTER);
		
		int i = 1;
		for(User u : users) {
			
			VBox t = new VBox(new Label(i+"."));
			t.getStyleClass().addAll("top", "child", "column");
			VBox n = new VBox(new Label(u.getNickName()));
			n.getStyleClass().addAll("name", "child", "column");
			VBox v = new VBox(new Label(u.getVictories()+""));
			v.getStyleClass().addAll("victories", "child", "column");
		//	VBox r = new VBox(new Label(u.getRatio()+""));
		//	r.getStyleClass().addAll("ratio");
			VBox p = new VBox(new Label(u.getPoints()+""));
			p.getStyleClass().addAll("points", "child", "column");
			VBox s = new VBox(new Label(u.isInGame()?"Playing":"Waiting"));
			s.getStyleClass().addAll("status", "child", "column");
			
			HBox l = new HBox(t, n, v, p, s);
			l.getStyleClass().add("line");
			
			l.setOnMouseClicked(e -> {
				this.opponent = u;
				this.playWith.setText("Play with "+u.getNickName());
			});
			
			board.getChildren().add(l);
			
			i+=1;
		}
		
		VBox container = new VBox(board);
		container.setAlignment(Pos.TOP_CENTER);
		container.setPrefWidth(WIDTH/2);
		this.rightContainer = container;
	}
	

	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Online");
		stage.setScene(this.scene);
		stage.setResizable(false);
		this.stage = stage;
	}

}
