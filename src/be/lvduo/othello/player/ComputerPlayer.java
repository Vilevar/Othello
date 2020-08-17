package be.lvduo.othello.player;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class ComputerPlayer implements Player {
	
	private Piece color;
	private double difficulty;
	private boolean boss;
	
	public ComputerPlayer(Piece color, double difficulty, boolean boss) {
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
		// TODO
		return null;
	}
	
	

}
