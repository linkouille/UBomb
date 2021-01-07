package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.collectable.Collectable;

import java.lang.reflect.Array;
import java.util.*;

public class Monster extends Character implements Movable {

    private Direction direction;

    private Timer moveTimer;

    private boolean moveRequested;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /** Monster constructor
     * @param game current game
     * @param position the position of the Monster
     * @param speed the speed of the monster (seconds) it's the frequency for the Monster Movement
     */
    public Monster(Game game, Position position, float speed) {
        super(game, position);
        this.direction = Direction.W;
        setCanTakeDamage(true);
        setAlive(true);
        setLives(1);
        moveTimer = new Timer(1);
    }

    /** Check if the Monster can walk on the position in the direction
     * @param direction
     * @return true if Monster can walk and false otherwise
     * Monster can't walk on Decor
     * if Monster walk on Player then player take damage
     * Monster can walk on other gameObjects based on there canWalkOn() method return
     */
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
            if(g.isCharacter()){ //Can't walk on other Monster
                return false;
            }
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
        if(direction == null)
            return;

        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public boolean canWalkOn(){
        return true;
    }

    /** Update Monster movement and timer
     * When the moveTimer is finshed then we try a random direction if we can't walk on it we try another one
     * Then we start again the timer
     * @param now current time in nanoseconds
     */
    @Override
    public void update(long now) {
        if(!isAlive())
            this.Die();

        if (moveRequested) {
            doMove(direction);
        }
        moveRequested = false;

        moveTimer.update(now);

        if(moveTimer.isFinished() || !moveTimer.isRunnig()){
            /*
            Direction newDirection;
            do{
                newDirection = Direction.random();
            }while(!canMove(newDirection));

             */
            requestMove(randomPostion());
            moveTimer.StartTimer(now);
        }
    }

    /**
    * Return a Random Direction in the Direction Monster canWalkOn (call canMove)
    * return null if the monster can't move in any direction
    */
    private Direction randomPostion(){
        Random rand = new Random();
        List<Direction> possibleDir = new ArrayList<>(Arrays.asList(Direction.N,Direction.S,Direction.E,Direction.W));

        Direction[] out =  possibleDir.stream().filter(d->canMove(d)).toArray(Direction[]::new);

        return out.length == 0 ? null : out[rand.nextInt(out.length)];

    }

}
