package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import utilz.Network;
import utilz.Position;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
	MainClass gameMain;

	JDialog timer = new JDialog();
    JLabel info = new JLabel("Aguarde a entrada de outro jogador.");

	Network network;

	private MouseInputs mouseInputs;
	private Game game;

	boolean gameRunning = false;
	private KeyboardInputs keyboardInputs;
	
	public GamePanel(MainClass gameMain, Game game, Network network) {
		this.gameMain = gameMain;
		this.game = game;
		this.network = network;
		
		this.keyboardInputs = new KeyboardInputs(this);
        this.mouseInputs = new MouseInputs(this);
        
        setPanelSize();
        setupInputs();
		setupDialog();
        startNetworkThread();
    }

	private void setupInputs() {
        addKeyListener(keyboardInputs);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

	private void setupDialog() {
        SwingUtilities.invokeLater(() -> {
            timer = new JDialog();
            timer.setTitle("Aguardando Conexão");
            timer.setLayout(new FlowLayout());
            info = new JLabel("Aguarde a entrada de outro jogador.");
            timer.add(info);
            timer.setSize(300, 100);
            timer.setLocationRelativeTo(null);
            timer.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            timer.setAlwaysOnTop(true);
            timer.setVisible(true);
        });
    }

	private void startNetworkThread() {
        new Thread(() -> {
            Position localPlayerPosition = new Position(0, 0);
            Position remotePlayerPosition = new Position(0, 0);
            
            startNetwork(localPlayerPosition, remotePlayerPosition);
            gameRunning = true;
            
            while(network.alive()) {
                handleNetworkMessages(localPlayerPosition, remotePlayerPosition);
            }
            
            handleDisconnect();
        }).start();
    }

	public Network getNetwork() {
        return network;
    }

    public Game getGame() {
        return game;
    }

	void handleNetworkMessages(Position localPlayerPosition, Position remotePlayerPosition) {
		String action = network.readTypeMessage();
		
		if (action.equals("DISCONNECT")) {
			handleDisconnect();
			return;
		}
		
		if (action.equals("COORDINATES")) {
			try {
				network.readPosition(localPlayerPosition, remotePlayerPosition);
				game.getLocalPlayer().updatePos(localPlayerPosition);
				game.getRemotePlayer().updatePos(remotePlayerPosition);
			} catch (Exception e) {
				System.err.println("Error updating positions: " + e.getMessage());
			}
		}
		
		repaint();
	}



	private void handleDisconnect() {
          gameRunning = false;
          JOptionPane.showMessageDialog(gameMain, 
               "Jogo finalizado, segundo jogador desconectado", 
               "Fim de Jogo", 
               JOptionPane.INFORMATION_MESSAGE);
          System.exit(0);
    }

	 private void startNetwork(Position localPlayerPosition,  Position remotePlayerPosition){

		network.readPosition(localPlayerPosition, remotePlayerPosition);

		// game.getLocalPlayer().initialPosition(localPlayerPosition);
		// game.getRemotePlayer().initialPosition(remotePlayerPosition);

		game.initializePlayerPositions(localPlayerPosition, remotePlayerPosition);


		for(int i = 10; i >= 1; i--){
			 changeInfoDialog("O jogo começa em: " + i + " segundo(s).");
			 String response = network.readTypeMessage();
			 if (response.equals("DISCONNECT")) {
                handleDisconnect();
                return;
            }
		}

		timer.dispose();
   }

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	public void updateGame() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(gameRunning) {
			game.render(g);

		}
		Toolkit.getDefaultToolkit().sync();
	}

	public void changeInfoDialog(String info){
		this.info.setText(info);
   }

  

}