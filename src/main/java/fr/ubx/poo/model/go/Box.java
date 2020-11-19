package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;

public class Box extends GameObject implements Movable {
    public Box(Game game, Position position) {
        super(game, position);
    }

    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());
        return game.getWorld().isInside(p) && game.getWorld().get(p) == null;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        game.getWorld().clearGO(getPosition());
        game.getWorld().setGO(nextPos, this);
        setPosition(nextPos);
    }
}
