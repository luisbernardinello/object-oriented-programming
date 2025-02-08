package network;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import core.GameState;

public class PlayerConnectionManager {
    private final ConcurrentHashMap<Integer, PlayerSession> players = new ConcurrentHashMap<>();
    private final GameState gameState = GameState.getInstance();

    public void addPlayer(PlayerSession session) {
        players.put(session.getPlayerId(), session);
    }

    public void handlePlayerDisconnect(int playerId) {
        players.remove(playerId);
        // Notificar o outro jogador sobre a desconexão
        int otherPlayerId = 1 - playerId;
        PlayerSession otherSession = players.get(otherPlayerId);
        if (otherSession != null && otherSession.isRunning()) {
            try {
                otherSession.sendMessage("DISCONNECT");
                otherSession.flush();
            } catch (IOException e) {
                // Ignore as we're already handling a disconnect
            }
        }
    }

    public void sendMessage(int playerId, String message) throws IOException {
        PlayerSession session = players.get(playerId);
        if (session != null && session.isRunning()) {
            session.sendMessage(message);
        }
    }

    public void sendInitialPositions(int playerId) throws IOException {
        PlayerSession session = players.get(playerId);
        if (session != null && session.isRunning()) {
            if (playerId == 0) {
                // Para o player 1, envia na ordem normal
                session.sendInt(GameState.INITIAL_POSITION_PLAYER1.x);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER1.y);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER2.x);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER2.y);
            } else {
                // Para o player 2, inverte a ordem das posições
                session.sendInt(GameState.INITIAL_POSITION_PLAYER2.x);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER2.y);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER1.x);
                session.sendInt(GameState.INITIAL_POSITION_PLAYER1.y);
            }
        }
    }

    public void sendCoordinates(int playerId) throws IOException {
        PlayerSession session = players.get(playerId);
        if (session != null && session.isRunning()) {
            session.sendMessage("COORDINATES");
            if (playerId == 0) {
                // Para o player 1, envia na ordem normal
                session.sendVariationPosition(gameState.getPosition(0));
                session.sendVariationPosition(gameState.getPosition(1));
            } else {
                // Para o player 2, inverte a ordem das variações
                session.sendVariationPosition(gameState.getPosition(1));
                session.sendVariationPosition(gameState.getPosition(0));
            }
            session.flush();
        }
    }
}