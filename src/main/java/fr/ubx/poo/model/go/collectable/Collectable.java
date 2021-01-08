package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

/**
 * {@link Collectable} is GameObject That are ignored by {@link fr.ubx.poo.model.go.character.Monster}, Can be Collected by {@link Player}
 * and destroyed by {@link fr.ubx.poo.model.go.Bomb}
 */
public abstract class Collectable extends GameObject {

    public Collectable(Game game, Position position) {
        super(game, position);
    }

    public abstract void pickUp(Player p);

    @Override
    public boolean isCollectable(){
        return true;
    }

    @Override
    public boolean isDestroyable(){
        return true;
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }
}

