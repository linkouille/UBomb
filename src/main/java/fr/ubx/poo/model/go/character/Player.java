/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.collectable.Collectable;
import fr.ubx.poo.model.go.collectable.Key;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;

    private int keys;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
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

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());

        if(!game.getWorld().isInside(p))
            return false;

        Decor d = game.getWorld().get(p);
        if(d != null && !(d instanceof Princess) && !(d instanceof Door))
            return false;

        // In the next position we have a GameObject
        GameObject g = game.getWorld().getGO(p);
        if(g != null){
            if(g instanceof Box)
                if(((Box)g).canMove(direction))
                    ((Box) g).doMove(direction);
                else
                    return false;
            if(g instanceof Collectable)
            {
                ((Collectable) g).pickUp();
                System.out.println("Picked up Collectable" + g);
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

}
