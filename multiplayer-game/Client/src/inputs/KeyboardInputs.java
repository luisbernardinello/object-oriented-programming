package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import utilz.Network;

import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;
	private Network network;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.network = gamePanel.getNetwork();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.getGame().getLocalPlayer().setUp(false);
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getLocalPlayer().setLeft(false);
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getLocalPlayer().setDown(false);
			break;
		case KeyEvent.VK_D:
			gamePanel.getGame().getLocalPlayer().setRight(false);

			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			gamePanel.getGame().getLocalPlayer().setUp(true);
			network.sendMovement("UPWARD");
			network.flushOutput();
			break;
		case KeyEvent.VK_A:
			gamePanel.getGame().getLocalPlayer().setLeft(true);
			network.sendMovement("LEFTWARD");
            network.flushOutput();
			break;
		case KeyEvent.VK_S:
			gamePanel.getGame().getLocalPlayer().setDown(true);
			network.sendMovement("DOWNWARD");
            network.flushOutput();
			break;
		case KeyEvent.VK_D:
			gamePanel.getGame().getLocalPlayer().setRight(true);
			network.sendMovement("RIGHTWARD");
            network.flushOutput();
			break;
		}
	}
}
