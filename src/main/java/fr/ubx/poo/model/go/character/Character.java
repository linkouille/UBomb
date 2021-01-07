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

    public void addLives(int lives) {
        if(!canTakeDamage)
            return;
        this.lives += lives;
        damageTaken = true;

        // System.out.println(this + " has taken " + lives + " damage ! ");

        if(this.lives <= 0){
            this.lives = 0;
            this.alive = false;
        }
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

    public void Die(){
        this.destroy();
    }

    @Override
    public boolean isCharacter() {
        return true;
    }
}
