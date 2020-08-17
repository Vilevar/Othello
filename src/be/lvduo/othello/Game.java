package be.lvduo.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import be.lvduo.othello.player.Player;

public class Game {

	private Board board;
	
	private Player black;
	private Player white;
	
	private Player current;
	
	public Game(Player player1, Player player2) {
		this.board = new Board();
		
		this.black = player1;
		this.white = player2;
		
		this.current = this.black;
	}
	
	public void play(Player player, int shot) {
		
	}
	
	private List<Point> getPossiblesShots(Player player) {
		
		Piece opposite = null;
		Piece piece = null;
		if(player.equals(this.black)) { 
			opposite = Piece.WHITE_PIECE;
			piece = Piece.BLACK_PIECE;
		}
		if(player.equals(this.white)) { 
			opposite = Piece.BLACK_PIECE;
			piece = Piece.WHITE_PIECE;
		}
		if(opposite == null || piece == null) return null;
		
		List<Point> squares = new ArrayList<>();
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++) {
				if(Board.canPlayOn(x, y))
					if(this.board.getPiece(x, y) != Piece.BLANK && this.board.getPiece(x, y) == opposite) {
						
						Point square = null;
						
						if(this.board.getPiece(x-1, y) == Piece.BLANK) {
							for(int i = x-1; i < (Board.WIDTH);i++) {
								if(this.board.getPiece(i, y) == piece) {
									new Point(x+1, y);
								}
							}
						}
						if(this.board.getPiece(x+1, y) == Piece.BLANK) {
							for(int i = x+1; i < x;i++) {
								if(this.board.getPiece(i, y) == piece) {
									new Point(x-1, y);
								}
							}
						}
						square = (this.board.getPiece(x+1, y) == Piece.BLANK)? new Point(x+1, y): null;		// 2
						square = (this.board.getPiece(x, y-1) == Piece.BLANK)? new Point(x, y-1): null;		// 3
						square = (this.board.getPiece(x, y+1) == Piece.BLANK)? new Point(x, y+1): null;		// 4
						square = (this.board.getPiece(x+1, y+1) == Piece.BLANK)? new Point(x+1, y+1): null;	// 5
						square = (this.board.getPiece(x-1, y-1) == Piece.BLANK)? new Point(x-1, y-1): null;	// 6
						square = (this.board.getPiece(x-1, y+1) == Piece.BLANK)? new Point(x-1, y+1): null;	// 7
						square = (this.board.getPiece(x+1, y-1) == Piece.BLANK)? new Point(x+1, y-1): null;	// 8
						
						if(square != null && !squares.contains(square)) squares.add(square);
					}
			}
		}
		
		
		
		if(player.equals(this.black)) {
			
		} else if(player.equals(this.white)) {
			
		} else {return null;}
		
		return null;
	}
	
	public Player getCurrent() {
		return current;
	}
	
	public Board getBoard() {
		return board;
	}
}
