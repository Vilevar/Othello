package be.lvduo.othello.online.network.packets;

import be.lvduo.othello.online.network.ICPacketHandler;
import be.lvduo.othello.online.network.Packet;
import io.netty.buffer.ByteBuf;

public class SPacketBeginner implements Packet<ICPacketHandler> {
	
	public SPacketBeginner() {
	}

	@Override
	public void write(ByteBuf buf) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(ByteBuf buf, ICPacketHandler handler) throws Exception {
		// TODO Auto-generated method stub
		handler.begin();
	}

}
