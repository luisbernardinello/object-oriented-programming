package network;
import java.io.*;
import java.net.*;

import core.GameLoop;
import core.GameState;
import model.MovementDirection;
import model.Position;

public class PlayerSession extends Thread {
    private final Socket clientSocket;
    private final PlayerConnectionManager connectionManager;
    private final GameState gameState;
    private final int playerId;
    private final int enemyId;
    private DataInputStream input;
    private DataOutputStream output;
    private volatile boolean running = true;

    public PlayerSession(Socket socket, PlayerConnectionManager manager, GameState state, int playerId) {
        this.clientSocket = socket;
        this.connectionManager = manager;
        this.gameState = state;
        this.playerId = playerId;
        this.enemyId = 1 - playerId;
    }

    private void startGame() throws IOException, InterruptedException {
        // Send initial positions to both players
        sendInitialPositions();
        connectionManager.sendInitialPositions(enemyId);

        // Start countdown timer
        for (int i = 10; i >= 1; i--) {
            sendMessage("TIMER");
            connectionManager.sendMessage(enemyId, "TIMER");
            Thread.sleep(1000);
        }
    }

    public void sendInitialPositions() throws IOException {
        if (playerId == 0) {
            // Para o player 1, envia na ordem normal
            sendInt(GameState.INITIAL_POSITION_PLAYER1.x);
            sendInt(GameState.INITIAL_POSITION_PLAYER1.y);
            sendInt(GameState.INITIAL_POSITION_PLAYER2.x);
            sendInt(GameState.INITIAL_POSITION_PLAYER2.y);
        } else {
            // Para o player 2, inverte a ordem das posições
            sendInt(GameState.INITIAL_POSITION_PLAYER2.x);
            sendInt(GameState.INITIAL_POSITION_PLAYER2.y);
            sendInt(GameState.INITIAL_POSITION_PLAYER1.x);
            sendInt(GameState.INITIAL_POSITION_PLAYER1.y);
        }
    }

    @Override
    public void run() {
        try {
            setupStreams();
            
            // Only player 2 (id = 1) initiates the game sequence
            if (playerId == 1) {
                startGame();
                new GameLoop(this, connectionManager).start();
            }

            handleGameLogic();
        } catch (IOException | InterruptedException e) {
            handleDisconnect();
        } finally {
            cleanup();
        }
    }

    private void handleGameLogic() throws IOException {
        while (running) {
            String type = input.readUTF();
            if (type.equals("MOVEMENT")) {
                handleMovement();
            }
            flush();
        }
    }

    public void sendPosition(Position position) throws IOException {
        sendInt(position.x);
        sendInt(position.y);
    }

    private void handleMovement() throws IOException {
        String direction = input.readUTF();
        gameState.movement(playerId, MovementDirection.valueOf(direction));
    }

    public void sendMessage(String message) throws IOException {
        if (running) {
            output.writeUTF(message);
        }
    }

    public void sendInt(int value) throws IOException {
        if (running) {
            output.writeInt(value);
        }
    }

    public void sendVariationPosition(Position variation) throws IOException {
        sendInt(variation.x);
        sendInt(variation.y);
    }

    public void flush() throws IOException {
        if (running) {
            output.flush();
        }
    }

    private void setupStreams() throws IOException {
        input = new DataInputStream(clientSocket.getInputStream());
        output = new DataOutputStream(clientSocket.getOutputStream());
    }

    private void cleanup() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            clientSocket.close();
        } catch (IOException e) {
            // Ignore cleanup errors
        }
    }

    private void handleDisconnect() {
        running = false;
        connectionManager.handlePlayerDisconnect(playerId);
    }

    // Getters
    public int getPlayerId() { return playerId; }
    public boolean isRunning() { return running; }
    public DataOutputStream getOutputStream() { return output; }
}