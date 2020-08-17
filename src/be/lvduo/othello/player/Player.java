package be.lvduo.othello.player;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public interface Player {
	
	boolean isHuman();
	Piece getColor();
	void play(Board board);

}
