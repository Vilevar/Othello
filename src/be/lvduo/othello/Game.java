package be.lvduo.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import be.lvduo.othello.player.Player;
import be.lvduo.othello.Directions.*;


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
		
		if(this.board.getPiece(shot) == Piece.BLANK && this.getPossiblesShots(player).contains(shot)) {
			this.board.setPiece(player.getColor(), shot);
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
	
	public List<Point> getPossiblesShots(Player player) {
		List<Point> squares = new ArrayList<>();
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++) {
				if(Board.canPlayOn(x, y)) {
					if(this.board.getPiece(x, y) == player.getColor().getOpposite()) {
		
						dir: for(Directions direction : Directions.values()) {
							Point point = new Point(x - direction.dirX, y - direction.dirY);
							
							if(this.board.getPiece(point) == Piece.BLANK) {
								for(int i = x + direction.dirX, j = y + direction.dirY; Board.canPlayOn(i, j); i += direction.dirX, j += direction.dirY) {
									if(this.board.getPiece(i,j) != player.getColor().getOpposite()) {
										if(this.board.getPiece(i,j) == player.getColor() && !squares.contains(point)) {
											squares.add(point);
										}
										continue dir;
									}
								}
							}
							
						}
						/*
						left: if(this.board.getPiece(x - 1, y) == Piece.BLANK) {
							for(int i = x + 1; i < Board.WIDTH; i++) {
								if(this.board.getPiece(i, y) != player.getColor().getOpposite()) {
									if(this.board.getPiece(i, y) == player.getColor() && !squares.contains(new Point(x-1, y))) {
										squares.add(new Point(x-1, y));
									}
									break left;
								}
							}
						}
						right: if(this.board.getPiece(x + 1, y) == Piece.BLANK) {
							for(int i = x - 1; i >= 0; i--) {
								if(this.board.getPiece(i, y) != player.getColor().getOpposite()) {
									if(this.board.getPiece(i, y) == player.getColor() && !squares.contains(new Point(x + 1, y))) {
										squares.add(new Point(x+1, y));
									}
									break right;
								}
							}
						}
						down: if(this.board.getPiece(x, y - 1) == Piece.BLANK) {
							for(int i = y + 1; i < Board.HEIGHT; i++) {
								if(this.board.getPiece(x, i) != player.getColor().getOpposite()) {
									if(this.board.getPiece(x, i) == player.getColor() && !squares.contains(new Point(x, y - 1))) {
										squares.add(new Point(x, y - 1));
									}
									break down;
								}
							}
						}
						up: if(this.board.getPiece(x, y + 1) == Piece.BLANK) {
							for(int i = y - 1; i >= 0; i--) {
								if(this.board.getPiece(x, i) != player.getColor().getOpposite()) {
									if(this.board.getPiece(x, i) == player.getColor() && !squares.contains(new Point(x, y + 1))) {
										squares.add(new Point(x, y + 1));
									}
									break up;
								}
							}
						}
						down_left: if(this.board.getPiece(x - 1, y - 1) == Piece.BLANK) {
							for(int i = x + 1, j = y + 1; i < Board.WIDTH && j < Board.HEIGHT; i++, j++) {
								if(this.board.getPiece(i, j) != player.getColor().getOpposite()) {
									if(this.board.getPiece(i, j) == player.getColor() && !squares.contains(new Point(x - 1, y - 1))) {
										squares.add(new Point(x - 1, y - 1));
									}
									break down_left;
								}
							}
						}
						down_right: if(this.board.getPiece(x+1, y-1) == Piece.BLANK) {
							for(int i = y+1, j = x-1; i < (Board.HEIGHT) && j >= 0; i++, j--) {
								if(this.board.getPiece(j, i) != player.getColor().getOpposite()) {
									if(this.board.getPiece(j, i) == player.getColor() && !squares.contains(new Point(x+1, y-1))) {
										squares.add(new Point(x+1, y-1));
									}
									break down_right;
								}
							}
						}
						up_left:if(this.board.getPiece(x-1, y+1) == Piece.BLANK) {
							for(int i = y-1, j = x+1; i >= 0 && j < (Board.WIDTH); i--, j++) {
								if(this.board.getPiece(j, i) != player.getColor().getOpposite()) {
									if(this.board.getPiece(j, i) == player.getColor() && !squares.contains(new Point(x-1, y+1))) {
										squares.add(new Point(x-1, y+1));
									}
									break up_left;
								}
							}
						}
						up_right: if(this.board.getPiece(x+1, y+1) == Piece.BLANK) {
							for(int i = y-1, j = x-1; i >= 0 && j >= 0; i--, j--) {
								if(this.board.getPiece(j, i) != player.getColor().getOpposite()) {
									if(this.board.getPiece(j, i) == player.getColor() && !squares.contains(new Point(x+1, y+1))) {
										squares.add(new Point(x+1, y+1));
									}
									break up_right;
								}
							}
						}
						*/
					}
				}
			}
		}
		
		return squares;
	}
	
	public void toggleCurrent() {
		if(this.current == this.player1) {
			this.current = this.player2;
		} else if(this.current == this.player2) {
			this.current = this.player1;
		} else {
			this.current = this.player1;
		}
	}
	
	public Player getCurrent() {
		return current;
	}
	
	public Board getBoard() {
		return board;
	}
}
