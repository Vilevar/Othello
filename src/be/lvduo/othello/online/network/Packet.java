package be.lvduo.othello.online.network;

import io.netty.buffer.ByteBuf;

public interface Packet<U> {

	void write(ByteBuf buf) throws Exception;
	void read(ByteBuf buf, U handler) throws Exception;
}