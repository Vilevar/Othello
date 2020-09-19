package be.lvduo.othello.online;

import be.lvduo.othello.gui.IGui;
import be.lvduo.othello.gui.NicknamePanel;
import be.lvduo.othello.online.network.ClientPacketHandler;
import be.lvduo.othello.online.network.NetworkManager;
import be.lvduo.othello.online.network.packets.CPacketAIPlayer;
import be.lvduo.othello.online.network.packets.CPacketName;
import be.lvduo.othello.online.network.packets.CPacketRandom;
import be.lvduo.othello.online.network.packets.CPacketRequest;
import be.lvduo.othello.online.network.packets.CPacketResponse;
import be.lvduo.othello.online.network.packets.CPacketSendAction;
import be.lvduo.othello.online.network.packets.CPacketWait;
import be.lvduo.othello.online.network.packets.SPacketBeginner;
import be.lvduo.othello.online.network.packets.SPacketGameOver;
import be.lvduo.othello.online.network.packets.SPacketName;
import be.lvduo.othello.online.network.packets.SPacketPlayerDenies;
import be.lvduo.othello.online.network.packets.SPacketPlayerList;
import be.lvduo.othello.online.network.packets.SPacketRequest;
import be.lvduo.othello.online.network.packets.SPacketSendAction;
import be.lvduo.othello.online.network.packets.SPacketStartGame;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Client {
	
	private String host;
	private int port;
	
	private NetworkManager network;
	
	private IGui panel;
	private User me;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		
		start();
	}
	
	public void start() {
		NetworkManager network = NetworkManager.create(this, host, port); // TODO
		if(network == null) {
			Alert noConnection = new Alert(AlertType.ERROR, "The connection to the server failed.", ButtonType.OK);
			noConnection.setTitle("Connection problem");
			noConnection.showAndWait();
			System.exit(0);
		} else {
			network.setHandler(new ClientPacketHandler(this));
			network.registerPacketType(CPacketName.class);
			network.registerPacketType(SPacketName.class);
			network.registerPacketType(CPacketWait.class);
			network.registerPacketType(SPacketPlayerList.class);
			network.registerPacketType(CPacketRequest.class);
			network.registerPacketType(SPacketRequest.class);
			network.registerPacketType(CPacketResponse.class); 
			network.registerPacketType(SPacketPlayerDenies.class); 
			network.registerPacketType(CPacketRandom.class);
			network.registerPacketType(CPacketAIPlayer.class);
			network.registerPacketType(SPacketStartGame.class);
			network.registerPacketType(SPacketBeginner.class);
			network.registerPacketType(CPacketSendAction.class);
			network.registerPacketType(SPacketSendAction.class);
			network.registerPacketType(SPacketGameOver.class);
			this.network = network;
		}
	} 
	
	public void connectionLost() {
		this.sendErrorMessage("the connection is losing with the host", true);
	}
	
	public void sendName(String text) {
		try {
			this.me = new User(text);
			network.sendPacket(new CPacketName(text));
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorMessage("the packet doesn't send", true);
		}
	}
	public void nameResponse(boolean b) {
		if(panel instanceof NicknamePanel)
			((NicknamePanel) this.panel).rep(b);
	}
	
	public void setHome(IGui panel) {
		this.panel = panel;
	}
	
	public void sendErrorMessage(String message, boolean exit) {
	//	if(!isExiting) {
	//		if(game == null) {
				Platform.runLater(() -> {
					Alert error = new Alert(AlertType.ERROR, message, ButtonType.OK);
					error.setTitle("An error occured");
					error.showAndWait();
					if(exit)
						System.exit(0);
				});
	//		}
	//	}
	}
	
	public void sendMessage(String title, String message) {
		Platform.runLater(() -> {
			Alert info = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
			info.setTitle(title);
			info.showAndWait();
		});
	}
	
	public User getMyself() {
		return this.me;
	}
	
	public void setMyself(User user) {
		this.me = user;
	}
	
	public IGui getPanel() {
		return this.panel;
	}
	
	public NetworkManager getNetwork() {
		return this.network;
	}

}
