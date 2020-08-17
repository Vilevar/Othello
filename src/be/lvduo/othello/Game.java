package be.lvduo.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.lvduo.othello.player.Player;


public class Game {

	private Board board = new Board();
	
	private Player player1;
	private Player player2;
	
	private Player current;
	
	private boolean isOver;
	
	
	public Game(Player player1, Player player2) {
		this.board = new Board();
		
		this.player1 = player1;
		this.player2 = player2;
		
		this.current = this.player1;
	}
	
	public void play(Player player, Point shot) {
		if(player != this.current)
			return;
		
		HashMap<Point, List<Directions>> availableShots;
		if(this.board.getPiece(shot) == Piece.BLANK && (availableShots = this.getPossiblesShots(player)).containsKey(shot)) {
			this.board.setPiece(player.getColor(), shot);
			this.togglePiece(player, shot, availableShots.get(shot));
			this.toggleCurrent();
			
			if(this.getPossiblesShots(this.current).isEmpty()) {
				if(this.getPossiblesShots(this.current = player).isEmpty())
					this.gameOver();
			}
		}		
	}
	
	public boolean isOver() {
		return isOver;
	}
	
	public void gameOver() {
		this.isOver = true;
		// TODO Other things
	}
	
	private void togglePiece(Player player, Point point, List<Directions> directions) {
		this.board.setPiece(player.getColor(), point);
		for(Directions dir : directions) {
			for(Point copy = new Point(point.x + dir.dirX, point.y + dir.dirY); this.board.getPiece(copy) != player.getColor();
					copy = new Point(copy.x + dir.dirX, copy.y + dir.dirY)) {
				this.board.setPiece(player.getColor(), copy);
			}
		}
	}
	
	public HashMap<Point, List<Directions>> getPossiblesShots(Player player) {
		HashMap<Point, List<Directions>> squares = new HashMap<>();
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++) {
				if(Board.canPlayOn(x, y)) {
					if(this.board.getPiece(x, y) == player.getColor().getOpposite()) {
		
						dir: for(Directions direction : Directions.values()) {
							Point point = new Point(x - direction.dirX, y - direction.dirY);
							if(this.board.getPiece(point) == Piece.BLANK) {
								for(int i = x + direction.dirX, j = y + direction.dirY; Board.canPlayOn(i, j); i += direction.dirX, j += direction.dirY) {
									if(this.board.getPiece(i, j) != player.getColor().getOpposite()) {
										if(this.board.getPiece(i, j) == player.getColor()) {
											List<Directions> availableDirections = squares.getOrDefault(point, new ArrayList<>());
											availableDirections.add(direction);
											squares.put(point, availableDirections);
										}
										continue dir;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return squares;
	}
	
	public Player toggleCurrent() {
		if(this.current == this.player1) {
			this.current = this.player2;
		} else if(this.current == this.player2) {
			this.current = this.player1;
		} else {
			this.current = this.player1;
		}
		return this.current;
	}
	
	public Player getCurrent() {
		return current;
	}
	
	public Board getBoard() {
		return board;
	}
}
