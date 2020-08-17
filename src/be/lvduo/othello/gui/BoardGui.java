package be.lvduo.othello.gui;

import java.awt.Point;
import java.util.List;

import be.lvduo.othello.Board;
import be.lvduo.othello.Game;
import be.lvduo.othello.Main;
import be.lvduo.othello.Piece;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;

public class BoardGui implements IGui {

	private static final double SQUARE_SIZE = 75;
	private static final double BENCH_SIZE = 50;
	private static final double MARGIN = 5;
	private static final double WIDTH = 2*MARGIN + SQUARE_SIZE*Board.WIDTH;
	private static final double HEIGHT = 2*BENCH_SIZE+ SQUARE_SIZE*Board.HEIGHT;
	private static final double CIRCLE_RADIUS = (SQUARE_SIZE/2) - 10;
	
	private Game game;
	private Scene scene;
	private List<Point> possibleShots;
	
	private Canvas canvas;
	private Group group;
	
	private Circle[] pieces = new Circle[Board.WIDTH * Board.HEIGHT];
	private Circle lastTest;
	private ImagePattern goodPosition = new ImagePattern(new Image(
			Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/good-position.png")));
	private ImagePattern badPosition = new ImagePattern(new Image(
			Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/bad-position.png")));
	
	
	public BoardGui(GameOptions options) {
		this.game = options.toGame();
		
		Button b = new Button("", new ImageView(new Image(Main.class.getClassLoader().getResourceAsStream("be/lvduo/othello/gui/control.png"))));
		b.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		b.setOnAction(e -> this.createControlPanel());
		b.setOnMouseEntered(e -> this.scene.setCursor(Cursor.HAND));
		b.setOnMouseExited(e -> this.scene.setCursor(Cursor.DEFAULT));
		
		this.scene = new Scene(group = new Group(canvas = new Canvas(WIDTH, HEIGHT), b), WIDTH, HEIGHT);
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
		ctx.fillRect(MARGIN, BENCH_SIZE, WIDTH - 2*MARGIN, HEIGHT - 2*BENCH_SIZE);
		// Board lines
		ctx.setStroke(Color.BLACK);
		for(int x = 1; x < Board.WIDTH; x++) {
			double xPos = MARGIN + x*SQUARE_SIZE;
			ctx.strokeLine(xPos, BENCH_SIZE, xPos, HEIGHT - BENCH_SIZE);
		}
		for(int y = 1; y < Board.HEIGHT; y++) {
			double yPos = BENCH_SIZE + y*SQUARE_SIZE;
			ctx.strokeLine(0 + MARGIN, yPos, WIDTH - MARGIN, yPos);
		}
		// Board bold lines at center
		double xCenterLeft = (Board.WIDTH - 2) / 2;
		double xCenterRight = Board.WIDTH - xCenterLeft;
		double yCenterAbove = (Board.HEIGHT- 2) / 2;
		double yCenterBelow = Board.HEIGHT - yCenterAbove;
		
		double xCenterLeftPos = MARGIN + xCenterLeft*SQUARE_SIZE;
		double xCenterRightPos = MARGIN + xCenterRight*SQUARE_SIZE;
		double yCenterAbovePos = BENCH_SIZE + yCenterAbove*SQUARE_SIZE;
		double yCenterBelowPos = BENCH_SIZE + yCenterBelow*SQUARE_SIZE;
		
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
		
		this.possibleShots = this.game.getPossiblesShots(this.game.getCurrent());
	}
	
	private void createControlPanel() {
		
	}

	private void testPosition(double x, double y) {
		Point pt;
		if(this.game.getCurrent().isHuman() && Board.canPlayOn(pt = this.convertToPoint(x, y))) {
			this.lastTest = this.drawOn(pt, this.lastTest);
			if(!this.possibleShots.contains(pt)) {
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
		if(this.game.getCurrent().isHuman() && Board.canPlayOn(pt = this.convertToPoint(x, y)) && this.possibleShots.contains(pt)) {
			this.game.play(this.game.getCurrent(), pt);
			this.update();
			while(!game.getCurrent().isHuman() && !this.game.isOver()) {
				this.game.getCurrent().play(this.game.getBoard());
				this.update();
			}
		}
	}
	
	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Game");
		stage.setScene(scene);
	}
	
	private Point convertToPoint(double x, double y) {
		return new Point((int) ((x - MARGIN) / SQUARE_SIZE), (int) (Board.HEIGHT - ((y - BENCH_SIZE) / SQUARE_SIZE)));
	}
	
	public Circle drawOn(Point point, Circle circle) {
		boolean add = circle == null;
		if(add)
			circle = new Circle(CIRCLE_RADIUS);
		circle.setCenterX(MARGIN + (point.x+.5)*SQUARE_SIZE);
		circle.setCenterY(BENCH_SIZE + (Board.HEIGHT - point.y -.5)*SQUARE_SIZE);
		if(add)
			this.group.getChildren().add(circle);
		return circle;
	}
}
