package fr.ubx.poo.model.go.collectable;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

/**
 * {@link Collectable} that add live to {@link Player}
 * @see Player#addLives(int)
 */
public class Life extends Collectable{

    public Life(Game game, Position position) {
        super(game, position);
    }

    @Override
    public void pickUp(Player p) {
        p.addLives(1);
        this.destroy();
    }
}
