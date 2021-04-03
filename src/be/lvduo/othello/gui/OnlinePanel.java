package be.lvduo.othello.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.lvduo.othello.Main;
import be.lvduo.othello.online.User;
import be.lvduo.othello.online.network.packets.CPacketRandom;
import be.lvduo.othello.online.network.packets.CPacketRequest;
import be.lvduo.othello.online.network.packets.CPacketWait;
import be.lvduo.othello.player.OpponentType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	
	private static final double WIDTH = 700, HEIGHT = 450;
	
	private Stage stage;
	private Scene scene;
	
	private VBox rightContainer = new VBox(new Label("Error, restart this app"));
	private VBox leftContainer = new VBox(new Label("Error, restart this app"));
	private VBox playersList;
	private VBox board;
	
	private Button playWith;
	private Button playRandom;
	private Button playAI;
	private List<User> users;
	
	private User opponent = null;
	private boolean randomMode = false;
	
	public OnlinePanel() {
		Main.client.setHome(this);
		try {
			Main.client.getNetwork().sendPacket(new CPacketWait(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.users = new ArrayList<>();
		this.leftContainer();
		this.rightContainer();
		
		BorderPane pane = new BorderPane();
		pane.setLeft(this.leftContainer);
		pane.setRight(this.rightContainer);
		this.scene = new Scene(pane, WIDTH, HEIGHT);
		this.scene.getStylesheets().add("be/lvduo/othello/gui/css/online.css");
	}
	
	public void leftContainer() {
		
		playWith = new Button("Play with ...");
		playWith.getStyleClass().add("button");
		playWith.setOnMouseClicked((e) -> {
			if(this.opponent != null)
				if(!randomMode)
					try {
						Main.client.getNetwork().sendPacket(new CPacketRequest(this.opponent.getNickname()));
						Main.client.sendMessage("Request", "The request sended to "+opponent.getNickname());
					} catch (Exception e1) {
						Main.client.sendErrorMessage("the packet was not send, try again", false);
						e1.printStackTrace();
					}
		});
		playRandom = new Button("Random");
		playRandom.getStyleClass().add("button");
		playRandom.setOnMouseClicked((e) -> {
			try {
				Main.client.getNetwork().sendPacket(new CPacketRandom(randomMode = !randomMode));
				if(randomMode)
					playRandom.getStyleClass().add("active");
				else
					playRandom.getStyleClass().remove("active");
				
			} catch (Exception e1) {
				Main.client.sendErrorMessage("the packet was not send, try again", false);
				e1.printStackTrace();
			}
		});
		playAI = new Button("AI");
		playAI.getStyleClass().add("button");
		//AIMode
		
		VBox container = new VBox(50, playWith, playRandom, playAI);
		container.getStyleClass().add("left-container");
		container.setPrefWidth(WIDTH/2);
		container.setAlignment(Pos.CENTER);
		container.setBackground(new Background(new BackgroundImage(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/othello-online.jpg")), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(-1, -1, true, true, false, false))));
		this.leftContainer = container;
	}
	
	
	public void update() {
		Platform.runLater(() -> usersList());
		
		if(this.opponent != null)
			if(!this.users.contains(this.opponent))
				this.opponent = null;
	}
	
	
	public void rightContainer() {
		
	//	this.users.add(new User("lyam"));	//exemple
	//	this.users.add(new User("marie"));	//exemple
	//	this.users.add(new User("loic"));	//exemple
	//	this.users.add(new User("Vilevar"));//exmeple
		
		Collections.sort(this.users,
				(u1, u2) -> u2.getPoints() - u1.getPoints());
		System.out.println(Arrays.toString(this.users.toArray()));

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
		
		board = new VBox(headLine);
		board.setAlignment(Pos.CENTER);
		usersList();
		
		VBox container = new VBox(board);
		container.setAlignment(Pos.TOP_CENTER);
		container.setPrefWidth(WIDTH/2);
		this.rightContainer = container;
	}
	
	public void usersList() {
		VBox pl = new VBox();
		pl.setAlignment(Pos.CENTER);
		int i = 1;
		for(User u : users) {
			
			VBox t = new VBox(new Label(i+"."));
			t.getStyleClass().addAll("top", "child", "column");
			VBox n = new VBox(new Label(u.getNickname()));
			n.getStyleClass().addAll("name", "child", "column");
			VBox v = new VBox(new Label(u.getVictories()+""));
			v.getStyleClass().addAll("victories", "child", "column");
		//	VBox r = new VBox(new Label(u.getRatio()+""));
		//	r.getStyleClass().addAll("ratio");
			VBox p = new VBox(new Label(u.getPoints()+""));
			p.getStyleClass().addAll("points", "child", "column");
			VBox s = new VBox(new Label(u.isInGame() ? "Playing" : "Waiting"));
			s.getStyleClass().addAll("status", "child", "column");
			
			HBox l = new HBox(t, n, v, p, s);
			l.getStyleClass().add("line");
			
			if(u.equals(Main.client.getMyself()))
				Main.client.setMyself(u);
			
			l.setOnMouseClicked((e) -> {
				this.opponent = (!u.equals(Main.client.getMyself()) && !u.isInGame()) ? u: null;
				if(opponent != null)
					this.playWith.setText("Play with "+this.opponent.getNickname());
			});
			
			pl.getChildren().add(l);
			
			i+=1;
		}
		
		if(board.getChildren().contains(playersList))
			board.getChildren().remove(playersList);
		board.getChildren().add(pl);
		
		this.playersList = pl;
		
	}
	
	public void setList(List<User> users) {
		this.users = new ArrayList<User>(users);
		update();
	}
	
	public void startGame(String opponent) {
		
		Platform.runLater(() -> {
			Alert info = new Alert(AlertType.INFORMATION, "your opponent is "+opponent, ButtonType.OK);
			info.setTitle("the game starts");
			info.showAndWait();
		});
		
		Platform.runLater(() -> {
			new BoardGui(new GameOptions(OpponentType.ONLINE, 0)).setScene(this.stage);
		});
		
	}
	
	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Online");
		stage.setScene(this.scene);
		stage.setResizable(false);
		stage.centerOnScreen();
		this.stage = stage;
		

		this.stage.focusedProperty().addListener((obs, old, v) -> {
			if(old != v)
				update();
		});
	}

}
