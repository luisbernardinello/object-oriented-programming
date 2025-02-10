package core;

import model.MovementDirection;
import model.Position;

public class GameState {
    private static GameState instance;
    
    public static final Position INITIAL_POSITION_PLAYER1 = new Position(300, 200);
    public static final Position INITIAL_POSITION_PLAYER2 = new Position(500, 200);

    private final Position[] positionVariations;
    private final Position[] absolutePositions;
    
    private final float gravity = 1.5f;
    private final float jumpSpeed = -15f;
    private final float maxFallSpeed = 10f;
    private final float moveSpeed = 5f;
    private final float[] verticalSpeed;
    private final boolean[] inAir;
    private final boolean[] movingLeft;
    private final boolean[] movingRight;
    private final Object lock = new Object();

    private GameState() {
        this.positionVariations = new Position[]{new Position(0, 0), new Position(0, 0)};
        this.absolutePositions = new Position[]{
            new Position(INITIAL_POSITION_PLAYER1.x, INITIAL_POSITION_PLAYER1.y),
            new Position(INITIAL_POSITION_PLAYER2.x, INITIAL_POSITION_PLAYER2.y)
        };
        this.verticalSpeed = new float[]{0, 0};
        this.inAir = new boolean[]{false, false};
        this.movingLeft = new boolean[]{false, false};
        this.movingRight = new boolean[]{false, false};
    }

    public static synchronized GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void applyGravity() {
        synchronized (lock) {
            for (int player = 0; player < 2; player++) {
                if (inAir[player]) {
                    verticalSpeed[player] += gravity;

                    if (verticalSpeed[player] > maxFallSpeed) {
                        verticalSpeed[player] = maxFallSpeed;
                    }

                    positionVariations[player].y = (int) verticalSpeed[player];
                    absolutePositions[player].y += (int) verticalSpeed[player];
                }

                processHorizontalMovement(player);
            }
        }
    }

    private void processHorizontalMovement(int player) {
        if (movingLeft[player]) {
            positionVariations[player].x = (int) -moveSpeed;
            absolutePositions[player].x -= moveSpeed;
        }
        if (movingRight[player]) {
            positionVariations[player].x = (int) moveSpeed;
            absolutePositions[player].x += moveSpeed;
        }
        if (!movingLeft[player] && !movingRight[player]) {
            positionVariations[player].x = 0;
        }
    }

    public void jump(int player) {
        synchronized (lock) {
            if (!inAir[player]) {
                verticalSpeed[player] = jumpSpeed;
                inAir[player] = true;
                positionVariations[player].y = (int) jumpSpeed;
            }
        }
    }

    public void movement(int player, MovementDirection direction) {
        synchronized (lock) {
            
            switch (direction) {
                case RIGHTWARD:
                    movingRight[player] = true;
                    break;
                case LEFTWARD:
                    movingLeft[player] = true;
                    break;
                case STOP_RIGHT:
                    movingRight[player] = false;
                    break;
                case STOP_LEFT:
                    movingLeft[player] = false;
                    break;
                case UPWARD:
                    jump(player);
                    break;
                case IN_AIR:
                    inAir[player] = true;  // 🔥 O jogador está no ar
                    break;
                case GROUNDED:
                    inAir[player] = false; // 🔥 O jogador tocou o chão
                    verticalSpeed[player] = 0; // Reseta a velocidade de queda
                    break;
            }
        }
    }

    

    public Position getPosition(int player) {
        synchronized (lock) {
            return positionVariations[player];
        }
    }

    public void resetPosition() {
        synchronized (lock) {
            positionVariations[0].x = 0;
            positionVariations[0].y = 0;
            positionVariations[1].x = 0;
            positionVariations[1].y = 0;
        }
    }
}
