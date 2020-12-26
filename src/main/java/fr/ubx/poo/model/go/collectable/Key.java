package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;

public class Key extends Collectable {

    public Key(Game game, Position position) {
        super(game, position);
    }

    @Override
    public void pickUp(Player p) {
        p.addKeys(1);
        destroy();
    }
}
