package be.lvduo.othello.player;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class ComputerPlayer implements Player {
	
	private Piece color;
	
	public ComputerPlayer(Piece color) {
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
	public void play(Board board) {
		// TODO Auto-generated method stub
		
	}
	
	

}
