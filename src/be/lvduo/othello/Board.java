package be.lvduo.othello;

import java.awt.Point;
import java.util.Arrays;

public class Board implements Cloneable {
	
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
		if(!isInBoard(x, y))
			return Piece.UNDIFINED;
		return this.board[y][x];
	}
	
	public static boolean isInBoard(Point pt) {
		return isInBoard(pt.x, pt.y);
	}
	
	public static boolean isInBoard(int x, int y) {
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
	}
	
	public Board clone() {
		Board other = new Board();
		for(int i = 0; i < other.board.length; i++) {
			other.board[i] = Arrays.copyOf(this.board[i], this.board[i].length);
		}
		return other;
	}

}

