package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

public abstract class Collectable extends GameObject {
    public Collectable(Game game, Position position) {
        super(game, position);
    }

    public abstract void pickUp(Player p);

    public boolean isCollectable(){
        return true;
    }

    public boolean isDestroyable(){
        return true;
    }
}

