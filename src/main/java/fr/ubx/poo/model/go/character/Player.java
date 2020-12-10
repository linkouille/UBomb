/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.collectable.Collectable;
import fr.ubx.poo.model.go.collectable.Key;

public class Player extends GameObject implements Movable {

    private boolean alive = true;

    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;

    private int keys;

    private int nbrBombMax;
    private int nbrBombPlaced;
    private int range;

    private boolean isNearDoor;
    private Door prevDoor;

    private boolean placeBomb;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.placeBomb = false;
        this.nbrBombMax = 3;
        this.nbrBombPlaced = 0;
        this.range = 1;

    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getKeys() {
        return keys;
    }

    public void addKeys(int keys) {
        this.keys += keys;
    }

    public boolean isNearDoor() {
        return isNearDoor;
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

    public void setRange(int range) {
        this.range = range;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public void Act(long now){
        //Called if space is pressed
        Position p = this.direction.nextPosition(this.getPosition());

        Decor d = game.getWorld().get(p);

        if(keys > 0 && d instanceof Door && !((Door) d).isState()){
            ((Door) d).setState(true);
            keys --;
        }else{
            if(game.getWorld().getGO(this.getPosition()) == null && nbrBombPlaced < nbrBombMax) {
                placeBomb = true;
                nbrBombPlaced += 1;
            }

        }

    }

    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());
        if(!game.getWorld().isInside(p))
            return false;

        Decor d = game.getWorld().get(p);
        if(d != null){
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
            }
            else{
                return g.canWalkOn();
            }

        }

        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isMovable(){
        return true;
    }

}
