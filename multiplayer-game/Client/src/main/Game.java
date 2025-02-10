package main;

import java.awt.Graphics;

import entities.Player;
import levels.LevelManager;
import utilz.Network;
import utilz.Position;

public class Game implements Runnable {
	private Network network;

	private MainClass gameMain;
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	private Player localPlayer, remotePlayer;
	private LevelManager levelManager;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.2f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public Game(MainClass gameMain, Network network) {
        this.gameMain = gameMain;
        this.network = network;
        initClasses();
        startGameLoop();
    }

	private void initClasses() {
		localPlayer = new Player(0, 0, (int) (64 * SCALE), (int) (40 * SCALE), 1, network);
        remotePlayer = new Player(0, 0, (int) (64 * SCALE), (int) (40 * SCALE),2, network);
		levelManager = new LevelManager(this);
	}

	public void initializePlayerPositions(Position localPos, Position remotePos) {
        if (localPlayer != null && localPos != null) {
            localPlayer.initialPosition(new Position(localPos.x, localPos.y));
			localPlayer.loadLvlData(levelManager.getLevelData());
		}
        if (remotePlayer != null && remotePos != null) {
            remotePlayer.initialPosition(new Position(remotePos.x, remotePos.y));
			remotePlayer.loadLvlData(levelManager.getLevelData());
		}
    }

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
        if (localPlayer != null) localPlayer.update();
        if (remotePlayer != null) remotePlayer.update();
        if (levelManager != null) levelManager.update();
    }

	public void render(Graphics g) {
		levelManager.draw(g);
		localPlayer.render(g);
		remotePlayer.render(g);
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				if (gamePanel != null) {  // ver se gamePanel não é null
                    gamePanel.repaint();
                }
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}

	public void windowFocusLost() {
        localPlayer.resetDirBooleans();
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public Player getRemotePlayer() {
        return remotePlayer;
    }

}