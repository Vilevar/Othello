package be.lvduo.othello;

import javafx.scene.paint.Color;

public enum Piece {
	
	UNDIFINED(null),
	BLANK(null),
	WHITE_PIECE(Color.WHITE),
	BLACK_PIECE(Color.BLACK);

	private Color color;
	Piece(Color color) {
		this.color = color;
	}
	
	public boolean isPiece() {
		return color != null;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Piece getOpposite() {
		  switch(this) {
		    case BLACK_PIECE: return WHITE_PIECE;
		    case WHITE_PIECE: return BLACK_PIECE;
		    default: return this;
		  }
		}
}
