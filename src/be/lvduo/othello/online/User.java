package be.lvduo.othello.online;

public class User {
	
	private String name;
	private int victories;
	private int defeats;
	private boolean inGame;
	
	
	public User(String nickname) {
		this.name = nickname;
		this.victories = this.defeats = 0;
		this.inGame = false;
	}
	
	public String getNickName() {
		return this.name;
	}
	
	public double getRatio() {
		if(this.victories == 0 && this.defeats == 0)
			return 0;
		return (this.victories / (this.victories + this.defeats));
	}
	
	public double getPoints() {
		return (Math.max(0, (100*this.victories) - (50*this.defeats)));
	}
	
	public int getVictories() {
		return this.victories;
	}
	
	public int addVictories(int add) {
		return this.victories += add;
	}
	
	public int getDefeats() {
		return this.defeats;
	}
	
	public int addDefeats(int add) {
		return this.defeats += add;
	}
	
	public boolean setStatus(boolean inGame) {
		return this.inGame = inGame;
	}
	
	public boolean isInGame() {
		return this.inGame;
	}

}
