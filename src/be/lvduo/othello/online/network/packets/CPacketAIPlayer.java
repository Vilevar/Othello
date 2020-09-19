package be.lvduo.othello.online.network.packets;

import be.lvduo.othello.online.network.ISPacketHandler;
import be.lvduo.othello.online.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketAIPlayer implements Packet<ISPacketHandler> {

	private boolean b;
	public CPacketAIPlayer() {}
	public CPacketAIPlayer(boolean AIPlayerMode) {
		this.b = AIPlayerMode;
	}
	
	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(this.b);
		
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		handler.startWithAI(buf.readBoolean());
		
	}

}
