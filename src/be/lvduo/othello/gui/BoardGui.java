package be.lvduo.othello.gui;

import be.lvduo.othello.Board;
import be.lvduo.othello.Game;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BoardGui implements IGui {

	private static final double SQUARE_SIZE = 75;
	private static final double BENCH_SIZE = 50;
	private static final double MARGIN = 5;
	private static final double WIDTH = 2*MARGIN + SQUARE_SIZE*Board.WIDTH;
	private static final double HEIGHT = 2*BENCH_SIZE+ SQUARE_SIZE*Board.HEIGHT;
	
	private Game game;
	private Scene scene;
	
	private Canvas canvas;
	private Group group;
	
	public BoardGui(GameOptions options) {
		this.game = options.toGame();
		
		this.scene = new Scene(group = new Group(canvas = new Canvas(WIDTH, HEIGHT)), WIDTH, HEIGHT);
		this.drawBackground();
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
	
	
	
	@Override
	public void setScene(Stage stage) {
		stage.setTitle("Othello - Game");
		stage.setScene(scene);
	}

}
