package be.lvduo.othello.gui;

import be.lvduo.othello.Game;
import be.lvduo.othello.Piece;
import be.lvduo.othello.player.HumanPlayer;

public class GameOptions {

	public Game toGame() {
		return new Game(new HumanPlayer(Piece.BLACK_PIECE), new HumanPlayer(Piece.WHITE_PIECE)); // TODO
	}
}
