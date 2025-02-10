package model;

public enum MovementDirection {
    RIGHTWARD(1, 0),
    LEFTWARD(-1, 0),
    UPWARD(0, -1),
    STOP_RIGHT(0, 0),
    STOP_LEFT(0, 0),
    IN_AIR(0, -1),   // 🔥 Adicionado para indicar que o jogador está no ar
    GROUNDED(0, 1); // 🔥 Adicionado para indicar que o jogador tocou o chão

    private final int dx;
    private final int dy;
    
    MovementDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public int getDx() { return dx; }
    public int getDy() { return dy; }
    
    public static MovementDirection fromString(String direction) {
        try {
            return valueOf(direction);
        } catch (IllegalArgumentException e) {
            return null; // 🔥 Se for um comando inválido, evita quebrar o servidor
        }
    }
}
