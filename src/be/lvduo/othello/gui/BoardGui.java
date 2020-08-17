package be.lvduo.othello.gui;

import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import be.lvduo.othello.Board;
import be.lvduo.othello.Game;
import be.lvduo.othello.Main;
import be.lvduo.othello.Piece;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class BoardGui implements IGui {

	private static final double SQUARE_SIZE = 75;
	private static final double CIRCLE_RADIUS = (SQUARE_SIZE/2) - 10;
	private static final double BENCH_SIZE_TOP = 50;
	private static final double BENCH_SIZE_BOTTOM = BENCH_SIZE_TOP + SQUARE_SIZE;
	private static final double MARGIN = 5;
	private static final double WIDTH = 2*MARGIN + SQUARE_SIZE*Board.WIDTH;
	private static final double HEIGHT = BENCH_SIZE_TOP + BENCH_SIZE_BOTTOM + SQUARE_SIZE*Board.HEIGHT;
	private static final double RECT_MARGIN = 4*MARGIN + SQUARE_SIZE;
	
	private Game game;
	private Scene scene;
	private Stage stage;
	
	private Canvas canvas;
	private Group group;
	
	private Circle[] pieces = new Circle[Board.WIDTH * Board.HEIGHT];
	private Circle lastTest;
	private ImagePattern goodPosition = new ImagePattern(new Image(
			Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/good-position.png")));
	private ImagePattern badPosition = new ImagePattern(new Image(
			Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/bad-position.png")));
	private Circle currentPlayer;
	
	private Timer timer = new Timer();
	
	
	public BoardGui(GameOptions options) {
		this.game = options.toGame();
		
		Button b = new Button("", new ImageView(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/control.png"))));
		b.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		b.setOnAction(e -> this.createControlPanel());
		b.setOnMouseEntered(e -> this.scene.setCursor(Cursor.HAND));
		b.setOnMouseExited(e -> this.scene.setCursor(Cursor.DEFAULT));
		
		this.currentPlayer = new Circle(WIDTH / 2, HEIGHT - .5*(BENCH_SIZE_TOP + SQUARE_SIZE), CIRCLE_RADIUS);
		
		this.scene = new Scene(group = new Group(canvas = new Canvas(WIDTH, HEIGHT), b, this.currentPlayer), WIDTH, HEIGHT);
		this.drawBackground();
		this.game.getBoard().createBoard();
		this.update();
		
		this.scene.setOnMouseDragged(e -> this.testPosition(e.getSceneX(), e.getSceneY()));
		this.scene.setOnMouseReleased(e -> this.handleAction(e.getSceneX(), e.getSceneY()));
	}
	
	
	public void drawBackground() {
		GraphicsContext ctx = this.canvas.getGraphicsContext2D();
		// Hidden background
		ctx.setFill(Color.BLACK);
		ctx.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Board background
		ctx.setFill(Color.GREEN);
		ctx.fillRect(MARGIN, BENCH_SIZE_TOP, WIDTH - 2*MARGIN, HEIGHT - BENCH_SIZE_TOP - BENCH_SIZE_BOTTOM);
		// Board lines
		ctx.setStroke(Color.BLACK);
		for(int x = 1; x < Board.WIDTH; x++) {
			double xPos = MARGIN + x*SQUARE_SIZE;
			ctx.strokeLine(xPos, BENCH_SIZE_TOP, xPos, HEIGHT - BENCH_SIZE_BOTTOM);
		}
		for(int y = 1; y < Board.HEIGHT; y++) {
			double yPos = BENCH_SIZE_TOP + y*SQUARE_SIZE;
			ctx.strokeLine(0 + MARGIN, yPos, WIDTH - MARGIN, yPos);
		}
		// Board bold lines at center
		double xCenterLeft = (Board.WIDTH - 2) / 2;
		double xCenterRight = Board.WIDTH - xCenterLeft;
		double yCenterAbove = (Board.HEIGHT- 2) / 2;
		double yCenterBelow = Board.HEIGHT - yCenterAbove;
		
		double xCenterLeftPos = MARGIN + xCenterLeft*SQUARE_SIZE;
		double xCenterRightPos = MARGIN + xCenterRight*SQUARE_SIZE;
		double yCenterAbovePos = BENCH_SIZE_TOP + yCenterAbove*SQUARE_SIZE;
		double yCenterBelowPos = BENCH_SIZE_TOP + yCenterBelow*SQUARE_SIZE;
		
		ctx.strokeLine(xCenterLeftPos + 1, yCenterAbovePos, xCenterLeftPos + 1, yCenterBelowPos);
		ctx.strokeLine(xCenterRightPos - 1, yCenterAbovePos, xCenterRightPos - 1, yCenterBelowPos);
		ctx.strokeLine(xCenterLeftPos, yCenterAbovePos + 1, xCenterRightPos, yCenterAbovePos + 1);
		ctx.strokeLine(xCenterLeftPos, yCenterBelowPos - 1, xCenterRightPos, yCenterBelowPos - 1);
		// Board circles around center
		double circleRadius = 3;
		double circleDiam = 2*circleRadius;
		double xCircleLeftPos = xCenterLeftPos - SQUARE_SIZE - circleRadius;
		double xCircleRightPos = xCenterRightPos + SQUARE_SIZE - circleRadius;
		double yCircleAbovePos = yCenterAbovePos - SQUARE_SIZE - circleRadius;
		double yCircleBelowPos = yCenterBelowPos + SQUARE_SIZE - circleRadius;
		
		ctx.setFill(Color.BLACK);
		ctx.fillOval(xCircleLeftPos, yCircleAbovePos, circleDiam, circleDiam);
		ctx.fillOval(xCircleRightPos, yCircleAbovePos, circleDiam, circleDiam);
		ctx.fillOval(xCircleLeftPos, yCircleBelowPos, circleDiam, circleDiam);
		ctx.fillOval(xCircleRightPos, yCircleBelowPos, circleDiam, circleDiam);
		// Rectangle to know whom turn
		ctx.setFill(Color.SADDLEBROWN);
		ctx.fillRect(RECT_MARGIN, HEIGHT - SQUARE_SIZE - .5*BENCH_SIZE_TOP, WIDTH - 2*RECT_MARGIN, SQUARE_SIZE);
	}
	
	public void update() {
		for(int x = 0; x < Board.WIDTH; x++) {
			for(int y = 0; y < Board.HEIGHT; y++) {
				int index = x*Board.WIDTH + y;
				Piece piece = this.game.getBoard().getPiece(x, y);
				if(piece.isPiece()) {
					Circle circle = this.pieces[index];
					if(circle == null) {
						circle = this.pieces[index] = this.drawOn(new Point(x, y), null);
						circle.setFill(piece.getColor());
					} else if(circle.getFill() != piece.getColor()) {	// it has changed of color
						circle.setFill(piece.getColor()); // TODO Animation
					}
				}
			}
		}
		if(this.game.isOver()) {
			this.currentPlayer.setOpacity(0);
			GraphicsContext ctx = this.canvas.getGraphicsContext2D();
			ctx.setTextAlign(TextAlignment.CENTER);
			ctx.setTextBaseline(VPos.CENTER);
			ctx.setFont(Font.font("Arial", FontPosture.REGULAR, 48));
			int winner = this.game.getWinner();
			if(winner == 2) {
				ctx.setFill(Color.RED);
			//	ctx.setFont(new Font(Gui, size));
				ctx.fillText("It is equal !", this.currentPlayer.getCenterX(), this.currentPlayer.getCenterY());
			} else {
				ctx.setFill(this.game.getPlayer(winner).getColor().getColor());
				ctx.fillText("The player "+(winner + 1)+" won !", this.currentPlayer.getCenterX(), this.currentPlayer.getCenterY());
			}
			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					HomeGui.HOME.setScene(stage);
				}
			}, 10_000);
		} else {
			this.currentPlayer.setFill(this.game.getCurrent().getColor().getColor());
		}
	}
	
	private void createControlPanel() {
		
	}

	private void testPosition(double x, double y) {
		Point pt;
		if(this.game.getCurrent().isHuman() && Board.isInBoard(pt = this.convertToPoint(x, y)) && !this.game.isOver()) {
			this.lastTest = this.drawOn(pt, this.lastTest);
			if(!this.game.getPossiblesShots(this.game.getCurrent()).containsKey(pt)) {
				this.lastTest.setFill(this.badPosition);
			} else {
				this.lastTest.setFill(this.goodPosition);
			}
		}
	}
	
	private void handleAction(double x, double y) {
		if(this.lastTest != null) {
			this.group.getChildren().remove(this.lastTest);
			this.lastTest = null;
		}
		Point pt;
		if(this.game.getCurrent().isHuman() && Board.isInBoard(pt = this.convertToPoint(x, y)) && this.game.getPossiblesShots(this.game.getCurrent())
				.containsKey(pt) && !this.game.isOver()) {
			this.game.play(pt);
			this.update();
			this.tryToPlayOther();
		}
	}
	
	private void tryToPlayOther() {
		if(!game.getCurrent().isHuman() && !this.game.isOver()) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> {
						BoardGui.this.game.play(BoardGui.this.game.getCurrent().play(BoardGui.this.game.getBoard()));
						BoardGui.this.update();
						BoardGui.this.tryToPlayOther();
					});
				}
			}, 2000);
		}
	}
	
	@Override
	public void setScene(Stage stage) {
		this.stage = stage;
		stage.setTitle("Othello - Game");
		stage.setScene(scene);
	}
	
	private Point convertToPoint(double x, double y) {
		return new Point((int) ((x - MARGIN) / SQUARE_SIZE), (int) (Board.HEIGHT - ((y - BENCH_SIZE_TOP) / SQUARE_SIZE)));
	}
	
	public Circle drawOn(Point point, Circle circle) {
		boolean add = circle == null;
		if(add)
			circle = new Circle(CIRCLE_RADIUS);
		circle.setCenterX(MARGIN + (point.x+.5)*SQUARE_SIZE);
		circle.setCenterY(BENCH_SIZE_TOP + (Board.HEIGHT - point.y -.5)*SQUARE_SIZE);
		if(add)
			this.group.getChildren().add(circle);
		return circle;
	}
}
