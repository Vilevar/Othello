package be.lvduo.othello.online.network;

import be.lvduo.othello.online.User;

public interface ISPacketHandler {
	
	User getUser();
	void testName(String name) throws Exception;
	void clientIsWaiting(boolean isWaiting);
	void getRequest(String name);
	void setRandomMode(boolean randomMode);
	void startWithAI(boolean AIMode);
	void hasPlaying(int readInt, int readInt2);
	void getResponse(boolean readBoolean);

}
