package fr.ubx.poo.model.go.effect;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;

/**
 * {@link GameObject} that are render above everything to add effect like smoke,explosion...
 */
public class Effect extends GameObject {
    public Effect(Game game, Position position) {
        super(game, position);
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }// Technicly useless because we don't check collision
}
