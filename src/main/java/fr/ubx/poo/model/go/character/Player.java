/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.collectable.Collectable;
import fr.ubx.poo.model.go.collectable.Key;

public class Player extends Character implements Movable {

    Direction direction;

    private boolean moveRequested = false;

    private boolean winner;

    private int keys;

    private int nbrBombMax;

    private int nbrBombPlaced;

    private int range;

    private boolean isNearDoor;

    private Door prevDoor;

    private boolean placeBomb;

    private Timer invicibleTimer;

    private boolean nexLevel;

    private int toLevel;

    public Direction getDirection() {
        return direction;
    }

    public int getKeys() {
        return keys;
    }

    public void addKeys(int keys) {
        this.keys += keys;
    }

    public boolean isPlaceBomb() {
        return placeBomb;
    }

    public void setPlaceBomb(boolean placeBomb) {
        this.placeBomb = placeBomb;
    }

    public int getNbrBombPlaced() {
        return nbrBombPlaced;
    }

    public void modiffNbrBombPlaced(int nbrBombPlaced) {
        this.nbrBombPlaced += nbrBombPlaced;
    }

    public int getNbrBombMax() {
        return nbrBombMax;
    }

    public int getRange() {
        return range;
    }

    public boolean isNexLevel() {
        return nexLevel;
    }

    public void setNexLevel(boolean nexLevel) {
        this.nexLevel = nexLevel;
    }

    public int getToLevel() {
        return toLevel;
    }

    /**
     * Contructor for player
     * @param game current game
     * @param position initial position for the player
     * Define
     * nbrBombMax maximal number of Bombs player can place
     * range of the Bombs
     */
    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.placeBomb = false;
        this.nbrBombMax = 3;
        this.nbrBombPlaced = 0;
        this.range = 1;
        this.invicibleTimer = new Timer(1);

    }

    /**
     * Range can't be less than 1
     * @param range amount added (can be positive or negative)
     */
    public void addRange(int range) {
        this.range += range;
        if(this.range <= 0)
            this.range = 1;
    }

    /**
     * BombMax can't be less than 1
     * @param nbrBombMax amount added (can be positive or negative)
     */
    public void addNbrBombMax(int nbrBombMax) {
        this.nbrBombMax += nbrBombMax;
        if(this.nbrBombMax <= 0)
            this.nbrBombMax = 1;

    }

    /**
     * Request Move which will be applied in the next update call
     * @param direction
     * @see #update(long now)
     */
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    /**
     * Called when user press SPACE and place a bomb
     * @param now current itme in microseconds
     * @see fr.ubx.poo.model.go.Bomb
     */
    public void Placebomb(long now){
        //Called if space is pressed
        if(game.getWorld().getGO(this.getPosition()) == null && nbrBombPlaced < nbrBombMax) {
            placeBomb = true;
            nbrBombPlaced += 1;
        }
    }

    /**
     * Called when user press ENTER and open {@link Door} in front of the player if the player has a {@link Key}
     */
    public void UseKey(){
        Position p = this.direction.nextPosition(this.getPosition());

        Decor d = game.getWorld().get(p);

        if(d != null && keys > 0 && d.isDoor() && !((Door) d).isState()){
            ((Door) d).setState(true);
            keys --;
        }

    }



    /**
     * Test if player can move in direction
     * if there are a {@link Decor}
     * -> if it's a {@link Door} opened then we go to the next/prev level
     * -> if it's the {@link Princess} then we won
     * else we look at the {@link Entity#canWalkOn()}
     * if it's a {@link GameObject}
     * -> if it's {@link Movable} then move it
     * -> if it's {@link Collectable} then collect it
     * -> if it's {@link Character} then player take 1 damage
     * @param direction
     * @return
     */
    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());
        if(!game.getWorld().isInside(p))
            return false;

        Decor d = game.getWorld().get(p);
        if(d != null){
            if(d.isDoor()){
                Door door = (Door) d;
                if(door.isState()){
                    this.toLevel = door.getToLevel();
                    this.nexLevel = true;
                    return true;
                }
            }else if(d.isPrincess()){
                winner = true;
            }
            return d.canWalkOn();
        }

        // In the next position we have a GameObject
        GameObject g = game.getWorld().getGO(p);
        if(g != null){
            if(g.isMovable()){
                Movable m = (Movable) g;
                if(m.canMove(direction)){
                    m.doMove(direction);
                    return true;
                }
                return false;
            }else if(g.isCollectable()){
                Collectable c = (Collectable) g;
                c.pickUp(this);
            }else{
                return g.canWalkOn();
            }

        }

        return true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    /**
     * Update player move and invicible timer
     * @param now current time in microseconds
     */
    @Override
    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;

        if(isDamageTaken()){
            invicibleTimer.StartTimer(now);
            setDamageTaken(false);
        }

        this.invicibleTimer.update(now);
        setCanTakeDamage(invicibleTimer.isFinished() || !invicibleTimer.isRunnig());

    }

    /**
     * When player reach 0 HP you lose (switch Alive to false)
     */
    @Override
    public void Die() {
        this.setAlive(false);
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isMovable(){
        return true;
    }

}
