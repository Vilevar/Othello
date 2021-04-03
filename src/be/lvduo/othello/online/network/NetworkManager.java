package be.lvduo.othello.online.network;

import java.util.ArrayList;

import be.lvduo.othello.online.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetworkManager extends SimpleChannelInboundHandler<ByteBuf> {
	
	public static NetworkManager create(Client client, String ip, int port) {
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		try {
			Channel channel = new Bootstrap().group(loopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
				@Override
				public void initChannel(Channel channel) throws Exception {
					channel.pipeline().addLast("timeout", new ReadTimeoutHandler(600));
					channel.pipeline().addLast("manager", new NetworkManager(client, channel));
				}
			}).connect(ip, port).sync().channel();
			System.out.println("Connected");
			return (NetworkManager) channel.pipeline().get("manager");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No connection");
		} finally {
		//	loopGroup.shutdownGracefully();
		}
		return null;
	}

	private final Client client;
	private final Channel channel;
	private final ArrayList<Class<? extends Packet<?>>> packets = new ArrayList<>(/*Arrays.asList((new Packet<NetworkManager<?>>() {
		public void read(ByteBuf buf, NetworkManager<?> handler) throws Exception {handler.close(false);}
		public void write(ByteBuf buf) throws Exception {}
	}).getClass())*/);
	private boolean closed;
	private ICPacketHandler handler;
	
	public NetworkManager(Client client, Channel channel) {
		this.client = client;
		this.channel = channel;
	}
	
	public void setHandler(ICPacketHandler handler) {
		this.handler = handler;
	}
	
	public ICPacketHandler getHandler() {
		return handler;
	}
	
	public void registerPacketType(Class<? extends Packet<?>> packetType) {
		if(closed)
			throw new IllegalStateException("Channel closed.");
		packets.add(packetType);
	}
	
	private ByteBuf writePacket(Packet<?> packet) throws Exception {
		if(closed)
			throw new IllegalStateException("Channel closed.");
		int index = packets.indexOf(packet.getClass());
		if(index == -1)
			throw new IllegalArgumentException("The packet of type "+packet.getClass()+" is not registred.");
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(index);
		packet.write(buf);
		return buf;
	}
	
	public void sendPacket(Packet<?> packet) throws Exception {
		this.channel.writeAndFlush(this.writePacket(packet));
		System.out.println("Send packet type "+packet.getClass().getSimpleName());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		if(closed)
			throw new IllegalStateException("Channel closed.");
		int index = buf.readInt();
	//	if(index == 0) {
	//		this.close(false);
	//	} else {
		System.out.println("Get packet type "+packets.get(index).getSimpleName());
		Packet<ICPacketHandler> packet = (Packet<ICPacketHandler>) packets.get(index).newInstance();
		packet.read(buf, handler);
	//	}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	//	System.out.println("Caught exception "+cause.toString());
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("channel inactive ");
		this.client.connectionLost();
	}
	
	public boolean isOpen() {
		return channel.isOpen();
	}
	
	public void close() throws Exception {
		this.close(true);
	}
	
	private void close(boolean packet) throws Exception {
		if(closed) 
			throw new IllegalStateException("Channel closed.");
//		if(packet)
//			this.channel.writeAndFlush(this.writePacket(packets.get(0).newInstance()), channel.voidPromise());
		this.channel.close();
		closed = true;
	}
}