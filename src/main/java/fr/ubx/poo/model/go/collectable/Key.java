package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

public class Key extends GameObject implements Collectable {

    public Key(Game game, Position position) {
        super(game, position);
    }

    @Override
    public void pickUp() {
        game.getWorld().clearGO(getPosition());

        Position p = new Position(99999, 99999);
        game.getWorld().setGO(p , this);
        setPosition(p);
    }

    public void effect(Player p) {
        p.addKeys(1);
    }
}
