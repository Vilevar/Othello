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
	
	public Player createInstance(Piece color, int difficulty) {
		return this.creator.create(color, difficulty);
	}
	
	private static interface IPlayerCreator {
		Player create(Piece color, int difficulty);
	}
}
