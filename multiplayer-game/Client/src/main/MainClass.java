package main;

import javax.swing.JFrame;

import utilz.Network;

public class MainClass extends JFrame {
    private Network network;
    private Game game;
    private GamePanel gamePanel;

    public MainClass() {
        super("Jogo Multiplayer");
        
        initializeNetwork();
        initializeGame();
        initializePanel();
        initializeFrame();
    }

    private void initializeNetwork() {
        network = new Network(this, "127.0.0.1", 8080);
    }

    private void initializeGame() {
        game = new Game(this, network);
    }

    private void initializePanel() {
        gamePanel = new GamePanel(this, game, network);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }

    private void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainClass();
    }
}
