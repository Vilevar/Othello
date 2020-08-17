package be.lvduo.othello;

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
		// TODO Default board
	}
	
	public void setPiece(Piece piece, int x, int y) {
		this.board[y][x] = piece;
	}
	
	public Piece getPiece(int x, int y) {
		if(x < 0 || x > WIDTH ||
			y < 0 || y > HEIGHT)
			return Piece.UNDIFINED;
		return this.board[y][x];
	}
	
	public static boolean canPlayOn(int x, int y) {
		if(x < 0 || x > WIDTH || y < 0 || y > HEIGHT)
			return false;
		// TODO test the game rules
		return true;
	}

}

