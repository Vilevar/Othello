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
	private HashMap<Point, List<Directions>> availableShots;
	
	private int winner = -1;
	
	
	public Game(Player player1, Player player2) {
		this.board = new Board();
		
		this.player1 = player1;
		this.player2 = player2;
		
		this.current = this.player1;
		
	}
	
	public void play(Point shot) {
		if(this.board.getPiece(shot) == Piece.BLANK && this.getPossiblesShots(this.current).containsKey(shot)) {
			this.togglePiece(shot, this.availableShots.get(shot));
			
			if(this.getPossiblesShots(this.toggleCurrent()).isEmpty() && this.getPossiblesShots(this.toggleCurrent()).isEmpty())
				this.gameOver();
		}		
	}
	
	public boolean isOver() {
		return winner != -1;
	}
	
	public void gameOver() {
		int nBlanks = 0;
		double nPlayer1 = 0.0;
		for(int x = 0; x < Board.WIDTH; x++) {
			for(int y = 0; y < Board.HEIGHT; y++) {
				Piece p = this.board.getPiece(x, y);
				if(!p.isPiece())
					nBlanks++;
				else if(p == this.player1.getColor())
					nPlayer1++;
			}
		}
		double halfAvailablePieces = ((double) (Board.WIDTH*Board.HEIGHT - nBlanks)) / 2.0;
		if(nPlayer1 > halfAvailablePieces) {
			this.winner = 0;
		} else if(nPlayer1 == halfAvailablePieces) {
			this.winner = 2;
		} else {
			this.winner = 1;
		}
	}
	
	public int getWinner() {
		return winner;
	}
	
	private void togglePiece(Point point, List<Directions> directions) {
		this.board.setPiece(this.current.getColor(), point);
		for(Directions dir : directions) {
			for(Point copy = new Point(point.x + dir.dirX, point.y + dir.dirY); this.board.getPiece(copy) != this.current.getColor();
					copy = new Point(copy.x + dir.dirX, copy.y + dir.dirY)) {
				this.board.setPiece(this.current.getColor(), copy);
			}
		}
	}
	
	public HashMap<Point, List<Directions>> getPossiblesShots(Player player) {
		if(this.availableShots != null)
			return this.availableShots;
		HashMap<Point, List<Directions>> squares = new HashMap<>();
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++) {
				if(Board.isInBoard(x, y)) {
					if(this.board.getPiece(x, y) == player.getColor().getOpposite()) {
		
						dir: for(Directions direction : Directions.values()) {
							Point point = new Point(x - direction.dirX, y - direction.dirY);
							if(this.board.getPiece(point) == Piece.BLANK) {
								for(int i = x + direction.dirX, j = y + direction.dirY; Board.isInBoard(i, j); i += direction.dirX, j += direction.dirY) {
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
		
		return this.availableShots = squares;
	}
	
	public Player toggleCurrent() {
		if(this.current == this.player1) {
			this.current = this.player2;
		} else if(this.current == this.player2) {
			this.current = this.player1;
		} else {
			this.current = this.player1;
		}
		this.availableShots = null;
		return this.current;
	}
	
	public Player getCurrent() {
		return current;
	}
	
	public Player getPlayer(int i) {
		switch (i) {
		case 0:
			return player1;
		case 1:
			return player2;
		default:
			return null;
		}
	}
	
	public Board getBoard() {
		return board;
	}
}
