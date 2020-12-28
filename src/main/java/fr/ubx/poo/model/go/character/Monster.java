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

    private boolean moveRequested;

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
            return false;
        }

        if(game.getPlayer().getPosition().equals(p)){
            game.getPlayer().addLives(-1);
            return true;
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
        setDirection(direction);
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);

    }
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public boolean canWalkOn(){
        return true;
    }

    @Override
    public void update(long now) {
        if(!isAlive())
            this.Die();

        if (moveRequested) {
            doMove(direction);
        }
        moveRequested = false;

        movetimer.update(now);

        if(movetimer.isFinished() || !movetimer.isRunnig()){
            Direction newDirection;
            do{
                newDirection = Direction.random();
            }while(!canMove(newDirection));
            requestMove(newDirection);
            movetimer.StartTimer(now);
        }
    }
}
