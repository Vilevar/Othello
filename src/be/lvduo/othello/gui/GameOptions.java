package be.lvduo.othello.gui;

import be.lvduo.othello.Game;
import be.lvduo.othello.Piece;
import be.lvduo.othello.player.HumanPlayer;
import be.lvduo.othello.player.OpponentType;

public class GameOptions {
	
	private OpponentType opponentType;
	private double difficulty;
	private boolean boss;
	
	public GameOptions(OpponentType opponentType, double difficulty, boolean boss) {
		this.opponentType = opponentType;
		this.difficulty = difficulty;
		this.boss = boss;
	}

	public OpponentType getOpponentType() {
		return opponentType;
	}
	
	public double getDifficulty() {
		return difficulty;
	}
	
	public boolean isBoss() {
		return boss;
	}
	
	public Game toGame() {
		return new Game(new HumanPlayer(Piece.BLACK_PIECE), this.opponentType.createInstance(Piece.WHITE_PIECE, this.difficulty, this.boss));
	}
}
