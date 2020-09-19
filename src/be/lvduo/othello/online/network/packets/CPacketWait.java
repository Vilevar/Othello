package be.lvduo.othello.online.network.packets;

import be.lvduo.othello.online.network.ISPacketHandler;
import be.lvduo.othello.online.network.Packet;
import io.netty.buffer.ByteBuf;

public class CPacketWait implements Packet<ISPacketHandler> {
	
	private boolean b;
	
	public CPacketWait() {}
	public CPacketWait(boolean b) {
		
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		buf.writeBoolean(b);
		
	}

	@Override
	public void read(ByteBuf buf, ISPacketHandler handler) throws Exception {
		handler.clientIsWaiting(buf.readBoolean());
		
	}

}
