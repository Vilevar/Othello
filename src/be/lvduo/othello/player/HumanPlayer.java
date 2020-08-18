package be.lvduo.othello.player;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class HumanPlayer implements Player {
	
	private Piece color;

	public HumanPlayer(Piece color) {
		this(color, 0);
	}
	
	public HumanPlayer(Piece color, int difficulty) {
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
	public Point play(Board board) {
		return null;
	}
	

}
