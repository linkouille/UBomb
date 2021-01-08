package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;

/**
 * a Character is a GameObject that can take damage
 */
public class Character extends GameObject {

    private boolean alive = true;

    protected int lives;

    private boolean canTakeDamage;

    private boolean damageTaken;

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isCanTakeDamage() {
        return canTakeDamage;
    }

    public void setCanTakeDamage(boolean canTakeDamage) {
        this.canTakeDamage = canTakeDamage;
    }

    public boolean isDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(boolean damageTaken) {
        this.damageTaken = damageTaken;
    }

    public Character(Game game, Position position) {
        super(game, position);
    }

    /**
     * Add live(s) to a Character
     * trigger damageTaken bool
     * lives can't be < 0 if it does the Character is Dead (set alive to false
     * @param lives the amount of modification for lives can be negative (take damage) or positive (heal)
     */
    public void addLives(int lives) {
        if(!canTakeDamage)
            return;
        this.lives += lives;
        if(lives < 0)
            damageTaken = true;

        if(this.lives <= 0){
            this.lives = 0;
            this.alive = false;
        }
    }

    /**
     * Kill Character (call destroy method from GameObject
     */
    public void Die(){
        this.destroy();
    }

    @Override
    public boolean isCharacter() {
        return true;
    }

    @Override
    public String toString() {
        return "Character{" +
                "lives=" + lives +
                "position=" + getPosition() +
                '}';
    }
}
