package be.lvduo.othello.player;

import be.lvduo.othello.Piece;

public enum OpponentType {

	HUMAN(HumanPlayer::new),
	COMPUTER(ComputerPlayer::new),
	ONLINE(OnlinePlayer::new);
	
	private IPlayerCreator creator;
	OpponentType(IPlayerCreator creator) {
		this.creator = creator;
	}
	
	public Player createInstance(Piece color, double difficulty, boolean boss) {
		return this.creator.create(color, difficulty, boss);
	}
	
	private static interface IPlayerCreator {
		Player create(Piece color, double difficulty, boolean boss);
	}
}
