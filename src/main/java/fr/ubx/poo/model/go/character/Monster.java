package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.collectable.Collectable;

public class Monster extends Character implements Movable {

    private Direction direction;

    private Timer movetimer;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Monster(Game game, Position position, float speed) {
        super(game, position);
        this.direction = Direction.W;
        setCanTakeDamage(true);
        setAlive(true);
        setLives(1);
        movetimer = new Timer(1);
    }

    @Override
    public boolean canMove(Direction direction) {
        Position p = direction.nextPosition(this.getPosition());
        if(!game.getWorld().isInside(p))
            return false;

        Decor d = game.getWorld().get(p);
        if(d != null){
            return d.canWalkOn();
        }

        // In the next position we have a GameObject
        GameObject g = game.getWorld().getGO(p);
        if(g != null){
            return g.canWalkOn();
        }

        return true;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);

        setDirection(direction);
    }

    public boolean canWalkOn(){
        return true;
    }

    @Override
    public void update(long now) {
        if(!isAlive())
            this.Die();

        movetimer.update(now);

        if(movetimer.isFinished() || !movetimer.isRunnig()){
            Direction newdir;
            do{
                newdir = Direction.random();
            }while(!canMove(newdir));
            doMove(newdir);
            movetimer.StartTimer(now);
        }
    }
}
