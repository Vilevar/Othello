package be.lvduo.othello;

import java.awt.Point;

public class Board {
	
	public final static int WIDTH = 8;
	public final static int HEIGHT = 8;
	
	private Piece[][] board;
	
	public Board() {
		this.board = new Piece[HEIGHT][WIDTH];
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < HEIGHT; x++) {
				this.board[y][x] = Piece.BLANK;
			}
		}
		this.createBoard();
	}
	
	public void createBoard() {
		this.setPiece(Piece.BLACK_PIECE, (WIDTH/2) - 1, (HEIGHT/2) - 1);
		this.setPiece(Piece.BLACK_PIECE, WIDTH/2, HEIGHT/2);
		this.setPiece(Piece.WHITE_PIECE, (WIDTH/2) - 1, HEIGHT/2);
		this.setPiece(Piece.WHITE_PIECE, WIDTH/2, (HEIGHT/2) - 1);
	}
	
	public void setPiece(Piece piece, Point point) {
		this.setPiece(piece, point.x, point.y);
	}
	public void setPiece(Piece piece, int x, int y) {
		this.board[y][x] = piece;
	}
	
	public Piece getPiece(Point point) {
		return this.getPiece(point.x, point.y);
	}
	
	public Piece getPiece(int x, int y) {
		if(!canPlayOn(x, y))
			return Piece.UNDIFINED;
		return this.board[y][x];
	}
	
	public static boolean canPlayOn(Point pt) {
		return canPlayOn(pt.x, pt.y);
	}
	
	public static boolean canPlayOn(int x, int y) {
		if(x < 0 || x > WIDTH || y < 0 || y > HEIGHT)
			return false;
		// TODO test the game rules
		return true;
	}

}

