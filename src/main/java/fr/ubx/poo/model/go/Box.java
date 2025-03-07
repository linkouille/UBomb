package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;

public class Box extends GameObject implements Movable {
    public Box(Game game, Position position) {
        super(game, position);
    }

    /**
     * Test if box can move in this direction with {@link Entity#canWalkOn()} ({@link GameObject} is always false)
     * @param direction
     * @return
     */
    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());
        if(!game.getWorld().isInside(p))
            return false;

        Decor d = game.getWorld().get(p);
        if(d != null){
            return d.canWalkOn();
        }

        GameObject g = game.getWorld().getGO(p);
        if(g != null){
            return false;
        }

        return true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    @Override
    public boolean canWalkOn() {
        return false;
    }

    public boolean isMovable(){
        return true;
    }

    public boolean isDestroyable(){
        return true;
    }
}
