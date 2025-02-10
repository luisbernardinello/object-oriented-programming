package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.LoadSave;
import utilz.Position;
import utilz.Network;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, jump, right;
    private int[][] lvlData;
    private int movementFramesLeft = GetSpriteAmount(RUNNING);
    private final float xDrawOffset = 21 * Game.SCALE;
    private final float yDrawOffset = 4 * Game.SCALE;
    private Position position, variation;
    private Network network;

    public Player(float x, float y, int width, int height, int player, Network network) {
        super(x, y, width, height);
        this.position = new Position((int) x, (int) y);
        this.variation = new Position(0, 0);
        this.network = network;
        
        loadAnimations(player);
        initHitbox(x, y, 20 * Game.SCALE, 27 * Game.SCALE);
    }

    public void update() {
        updatePos(variation);
        updateAnimationTick();
        setAnimation();
        updateHitbox();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], 
                    (int) (hitbox.x - xDrawOffset), 
                    (int) (hitbox.y - yDrawOffset), 
                    width, height, null);
        drawHitbox(g); // Ative para depuração
    }

    private void updateHitbox() {
        hitbox.x = x;
        hitbox.y = y;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

		if (inAir) {
			if (airSpeed < 0)
                playerAction = JUMP;
			else
                playerAction = FALLING;
		}
        if (attacking) {
            playerAction = ATTACK_1;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public void initialPosition(Position initial) {
        this.position = new Position(initial.x, initial.y);
        this.x = initial.x;
        this.y = initial.y;
    }

    public Position getPosition() {
        return position;
    }

   

    public void updatePos(Position variation) {
        
        if (variation == null || lvlData == null) {
            return;
        } 

        if (variation.x == 0 && variation.y == 0) {
            if (movementFramesLeft > 0) {
                movementFramesLeft--;  
            } else {
                moving = false;
            }
            return;
        }
    
        float newX = x + variation.x;
        float newY = y + variation.y;
    
        boolean movedX = updateXPos(newX);
        boolean movedY = updateYPos(newY);
    
        moving = movedX || movedY;
    
        if (moving) {
            movementFramesLeft = GetSpriteAmount(RUNNING);
        }

        if (jump)
            jump();

        // Verifica se está no ar
        if (!inAir && !IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
            network.sendMovement("IN_AIR");
        }
    
        // Verifica se tocou o chão
        if (variation.y > 0 && !movedY) {
            inAir = false;
            network.sendMovement("GROUNDED");
        }
      
        
    }

    private void jump() {
        if (inAir) return; // Impede saltos infinitos no ar

        inAir = true;
        variation.y = -15; // Define uma velocidade de pulo
    }

   

    private boolean updateXPos(float newX) {
        if (CanMoveHere(newX, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            x = newX;
            hitbox.x = newX;
            position.x = (int) newX;
            return true;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, newX - x);
            return false;
        }
    }

    private boolean updateYPos(float newY) {
        if (CanMoveHere(hitbox.x, newY, hitbox.width, hitbox.height, lvlData)) {
            y = newY;
            hitbox.y = newY;
            position.y = (int) newY;
            return true;
        } else {
            // hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, newY - y);
            return false;
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    private void loadAnimations(int player) {
        BufferedImage img;
        if (player == 1) {
            img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        } else {
            img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_REMOTE_ATLAS);
        }

        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }


    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }
    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) {
		this.jump = jump;
	}
}
