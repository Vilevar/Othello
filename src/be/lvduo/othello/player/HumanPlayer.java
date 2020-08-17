package be.lvduo.othello.player;

import be.lvduo.othello.Piece;

public class HumanPlayer implements Player {
	
	private Piece color;

	public HumanPlayer(Piece color) {
		if(!color.isPiece()) throw new IllegalArgumentException("The color must be a piece not as "+color);
		this.color = color;
	}

	@Override
	public boolean isHuman() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Piece getColor() {
		// TODO Auto-generated method stub
		return color;
	}
	

}
