package core;

import java.io.*;
import java.net.*;

import network.PlayerConnectionManager;
import network.PlayerSession;
import network.PlayerSessionFactory;

public class GameServer {
    private static final int PORT = 8080;
    private final ServerSocket serverSocket;
    private final PlayerConnectionManager connectionManager;
    private final GameState gameState;

    public GameServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.gameState = GameState.getInstance();
        this.connectionManager = new PlayerConnectionManager();
    }

    public void start() {
        System.out.println("Server is running...");
        System.out.println("Waiting for players...");

        try {
            // Wait for exactly 2 players. Player 1 == PlayerId 0, Player 2 == PlayerId 1
            for (int i = 0; i < 2; i++) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player " + (i + 1) + " entered the game.");
                PlayerSession session = PlayerSessionFactory.createSession(clientSocket, connectionManager, gameState, i);
                connectionManager.addPlayer(session);
                session.start();
            }
        } catch (IOException e) {
            System.err.println("Error accepting connections: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            GameServer server = new GameServer();
            server.start();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
    }
}
