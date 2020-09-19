package be.lvduo.othello.online.network;

import java.util.List;

import be.lvduo.othello.online.User;

public interface ICPacketHandler {
	
	void getStateOfName(boolean good);
	void getUsersList(List<User> users);
	void startGame(String opponent);
	void hasPlaying(int x, int y);
	void getRequest(String string);
	void getResponse(String string);
	void begin();
	void gameOver(boolean readBoolean);

}
