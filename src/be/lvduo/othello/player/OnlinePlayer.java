package be.lvduo.othello.player;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class OnlinePlayer implements Player {
	
	private Piece color;
	
	public OnlinePlayer(Piece color) {
		this(color, 0);
	}

	public OnlinePlayer(Piece color, int difficulty) {
		if(!color.isPiece()) throw new IllegalArgumentException("The color must be a piece not as "+color);
		this.color = color;
	}
	
	@Override
	public boolean isHuman() {
		return false;
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
