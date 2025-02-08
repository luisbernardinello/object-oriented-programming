package model;

public enum MovementDirection {
    RIGHTWARD(1, 0),
    LEFTWARD(-1, 0),
    UPWARD(0, -1),
    DOWNWARD(0, 1);
    
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
            return null;
        }
    }
}