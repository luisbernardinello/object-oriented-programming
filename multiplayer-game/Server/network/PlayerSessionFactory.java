package network;

import java.net.Socket;

import core.GameState;

public class PlayerSessionFactory {
    public static PlayerSession createSession(Socket socket, PlayerConnectionManager manager, GameState state, int playerId) {
        return new PlayerSession(socket, manager, state, playerId);
    }
}
