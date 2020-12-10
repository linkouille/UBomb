/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;

/***
 * A GameObject can acces the game and knows its position in the grid.
 */
public abstract class GameObject extends Entity {
    protected final Game game;
    private Position position;

    private boolean dead;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public GameObject(Game game, Position position) {
        this.game = game;
        this.position = position;
    }

    public void destroy(){
        // game.getWorld().clearGO(getPosition());
        dead = true;
    }

    public void update(long now){

    }

    public boolean isMovable(){
        //Can be pushed or not (Box)
        return false;
    }

    public boolean isCollectable(){
        //Can e collected (Key, bonus, Malus)
        return false;
    }

}
