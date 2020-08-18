package be.lvduo.othello.gui;

import be.lvduo.othello.Game;
import be.lvduo.othello.Piece;
import be.lvduo.othello.player.HumanPlayer;
import be.lvduo.othello.player.OpponentType;

public class GameOptions {
	
	private OpponentType opponentType;
	private int difficulty;
	
	public GameOptions(OpponentType opponentType, int difficulty) {
		this.opponentType = opponentType;
		this.difficulty = difficulty;
	}

	public OpponentType getOpponentType() {
		return opponentType;
	}
	
	public double getDifficulty() {
		return difficulty;
	}
	
	public Game toGame() {
		return new Game(new HumanPlayer(Piece.BLACK_PIECE), this.opponentType.createInstance(Piece.WHITE_PIECE, this.difficulty));
	}
}
