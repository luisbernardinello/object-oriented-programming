package core;

import model.MovementDirection;
import model.Position;

public class GameState {
    private static GameState instance;
    
    public static final Position INITIAL_POSITION_PLAYER1 = new Position(300, 200);
    public static final Position INITIAL_POSITION_PLAYER2 = new Position(500, 200);

    private final Position[] positionVariations;
    private final Position[] absolutePositions;
    
    private final float gravity = 1.5f; // Gravidade constante
    private final float jumpSpeed = -15f; // Velocidade inicial do salto
    private final float maxFallSpeed = 15f; // Velocidade máxima de queda
    private final float[] verticalSpeed; // Velocidade vertical dos jogadores
    private final Object lock = new Object();

    private GameState() {
        this.positionVariations = new Position[]{new Position(0, 0), new Position(0, 0)};
        this.absolutePositions = new Position[]{
            new Position(INITIAL_POSITION_PLAYER1.x, INITIAL_POSITION_PLAYER1.y),
            new Position(INITIAL_POSITION_PLAYER2.x, INITIAL_POSITION_PLAYER2.y)
        };
        this.verticalSpeed = new float[]{0, 0}; // Inicializa velocidade Y dos jogadores
    }

    public static synchronized GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    /**
     * 🔥 Aplica a gravidade continuamente
     */
    public void applyGravity() {
        synchronized (lock) {
            for (int player = 0; player < 2; player++) {
                verticalSpeed[player] += gravity; // Aplica gravidade
                
                if (verticalSpeed[player] > maxFallSpeed) {
                    verticalSpeed[player] = maxFallSpeed; // Limita a velocidade de queda
                }

                // Atualiza a variação de posição Y
                positionVariations[player].y = (int) verticalSpeed[player];
                absolutePositions[player].y += (int) verticalSpeed[player]; 
            }
        }
    }

    /**
     * 🔥 Inicia um salto para o jogador
     */
    public void jump(int player) {
        synchronized (lock) {
            verticalSpeed[player] = jumpSpeed; // Define velocidade inicial do salto
        }
    }

    /**
     * 🔥 Aplica movimento horizontal do jogador
     */
    public void movement(int player, MovementDirection direction) {
        synchronized (lock) {
            int dx = 0;
            
            switch (direction) {
                case RIGHTWARD:
                    dx = 10;
                    break;
                case LEFTWARD:
                    dx = -10;
                    break;
                case UPWARD:
                    jump(player); // Pressionou "W", inicia o salto
                    break;
            }

            positionVariations[player].x = dx;
            absolutePositions[player].x += dx;
        }
    }

    /**
     * 🔥 Retorna a variação da posição do jogador
     */
    public Position getPosition(int player) {
        synchronized (lock) {
            return positionVariations[player];
        }
    }

    /**
     * 🔥 Retorna a posição absoluta do jogador
     */
    public Position getAbsolutePosition(int player) {
        synchronized (lock) {
            return absolutePositions[player];
        }
    }

    /**
     * 🔥 Reseta a variação do movimento para evitar teleporte
     */
    public void resetPosition() {
        synchronized (lock) {
            positionVariations[0].x = 0;
            positionVariations[0].y = 0;
            positionVariations[1].x = 0;
            positionVariations[1].y = 0;
        }
    }
}
