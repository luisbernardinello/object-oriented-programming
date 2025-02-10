package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import utilz.Network;

public class KeyboardInputs implements KeyListener {
    private GamePanel gamePanel;
    private Network network;
    private boolean pressingW, pressingA, pressingD;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.network = gamePanel.getNetwork();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não usado
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                pressingW = false;
                break;
            case KeyEvent.VK_A:
                pressingA = false;
                network.sendMovement("STOP_LEFT");
                network.flushOutput();
                break;
            case KeyEvent.VK_D:
                pressingD = false;
                network.sendMovement("STOP_RIGHT");
                network.flushOutput();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (!pressingW) {
                    pressingW = true;
                    network.sendMovement("UPWARD");
                    network.flushOutput();
                }
                break;
            case KeyEvent.VK_A:
                if (!pressingA) {
                    pressingA = true;
                    network.sendMovement("LEFTWARD");
                    network.flushOutput();
                }
                break;
            case KeyEvent.VK_D:
                if (!pressingD) {
                    pressingD = true;
                    network.sendMovement("RIGHTWARD");
                    network.flushOutput();
                }
                break;
        }
    }
}
