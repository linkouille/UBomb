package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class BombRange extends Collectable{

    private final int value;

    public float getValue() {
        return value;
    }

    public BombRange(Game game, Position position, int value) {
        super(game, position);
        this.value = value;
    }

    @Override
    public void pickUp(Player p) {
        System.out.println("Pickup " + this);
        p.addRange(this.value);
        this.destroy();

    }
}
