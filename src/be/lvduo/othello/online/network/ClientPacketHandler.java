package be.lvduo.othello.online.network;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

import be.lvduo.othello.Board;
import be.lvduo.othello.gui.BoardGui;
import be.lvduo.othello.gui.OnlinePanel;
import be.lvduo.othello.online.Client;
import be.lvduo.othello.online.User;
import be.lvduo.othello.online.network.packets.CPacketResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ClientPacketHandler implements ICPacketHandler {

	private Client client;
	
	public ClientPacketHandler(Client client) {
		this.client = client;
	}

	@Override
	public void getStateOfName(boolean good) {
		if(good) {
			try {
				client.nameResponse(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			client.nameResponse(false);
		}
	}

	@Override
	public void getUsersList(List<User> users) {
		if(client.getPanel() instanceof OnlinePanel) {
			((OnlinePanel) client.getPanel()).setList(users);
		}
	}

	@Override
	public void startGame(String opponent) {
		if(client.getPanel() instanceof OnlinePanel)
			((OnlinePanel) client.getPanel()).startGame(opponent);
			
	}
	
	@Override
	public void begin() {
		while(!(client.getPanel() instanceof BoardGui))
			try {Thread.sleep(1000*1);} catch (InterruptedException ie) {}
		if(client.getPanel() instanceof BoardGui) {
			((BoardGui) client.getPanel()).getGame().toggleCurrent();
			Platform.runLater(() -> ((BoardGui) client.getPanel()).update());
		}
	}

	@Override
	public void hasPlaying(int x, int y) {
		if(client.getPanel() instanceof BoardGui) {
			BoardGui gui = ((BoardGui) client.getPanel());
			if(!gui.getGame().getCurrent().isHuman()) {
				gui.onlinePlaying(new Point(x,(Board.HEIGHT-1)-y));
			}
		}
		
	}

	@Override
	public void getRequest(String string) {
		if(client.getPanel() instanceof OnlinePanel)
			Platform.runLater(() -> {
				ButtonType accept = new ButtonType("Accept");
				ButtonType deny = new ButtonType("Deny");
				Alert ask = new Alert(AlertType.CONFIRMATION, string+" want to play with you", accept, deny);
				ask.setTitle("ask to playing");
				Optional<ButtonType> button = ask.showAndWait();
				
				if(button.get() == accept)
					try {
						client.getNetwork().sendPacket(new CPacketResponse(true));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						client.getNetwork().sendPacket(new CPacketResponse(false));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			});
		
	}

	@Override
	public void getResponse(String name) {
			Platform.runLater(() -> {
				Alert info = new Alert(AlertType.INFORMATION, name+" has refused your game proposal", ButtonType.OK);
				info.setTitle("Game proposal");
				info.showAndWait();
			});
		
	}

	@Override
	public void gameOver(boolean b) {
		System.out.println(client.getPanel());
		if(client.getPanel() instanceof BoardGui)
			((BoardGui) client.getPanel()).gameOver(b);
		
	}

}
