package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

/**
 * {@link Collectable} that add or remove Maximum Bomb from {@link Player}
 * @see Player#addNbrBombMax(int)
 */
public class BombCapacity extends Collectable{

    private final int value;

    public float getValue() {
        return value;
    }

    public BombCapacity(Game game, Position position, int value) {
        super(game, position);
        this.value = value;
    }

    @Override
    public void pickUp(Player p) {
        p.addNbrBombMax(this.value);
        this.destroy();
    }
}
