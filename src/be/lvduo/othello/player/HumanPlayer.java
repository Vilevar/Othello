package be.lvduo.othello.player;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class HumanPlayer implements Player {
	
	private Piece color;

	public HumanPlayer(Piece color) {
		this(color, 0, false);
	}
	
	public HumanPlayer(Piece color, double difficulty, boolean boss) {
		if(!color.isPiece()) throw new IllegalArgumentException("The color must be a piece not as "+color);
		this.color = color;
	}

	@Override
	public boolean isHuman() {
		return true;
	}

	@Override
	public Piece getColor() {
		return color;
	}

	@Override
	public void play(Board board) {
		// Do nothing
	}
	

}
