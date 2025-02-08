package core;

import java.io.IOException;
import network.PlayerConnectionManager;
import network.PlayerSession;

public class GameLoop extends Thread {
    private final PlayerSession session;
    private final PlayerConnectionManager connectionManager;
    private final GameState gameState;

    public GameLoop(PlayerSession session, PlayerConnectionManager connectionManager) {
        this.session = session;
        this.connectionManager = connectionManager;
        this.gameState = GameState.getInstance();
    }

    @Override
    public void run() {
        while (session.isRunning()) {
            try {
                synchronized (gameState) {
                    gameState.applyGravity(); // 🔥 Aplica gravidade antes de enviar os dados
                    
                    session.sendMessage("COORDINATES");
                    
                    // Envia as variações na ordem correta baseado no ID do jogador
                    if (session.getPlayerId() == 0) {
                        session.sendVariationPosition(gameState.getPosition(0));
                        session.sendVariationPosition(gameState.getPosition(1));
                    } else {
                        session.sendVariationPosition(gameState.getPosition(1));
                        session.sendVariationPosition(gameState.getPosition(0));
                    }
                    session.flush();

                    connectionManager.sendCoordinates(1 - session.getPlayerId());
                    gameState.resetPosition();
                }
                Thread.sleep(100); // Pequeno delay para suavizar a física
            } catch (InterruptedException | IOException e) {
                break;
            }
        }
    }
}
