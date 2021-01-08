package fr.ubx.poo.model.go.character;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.collectable.Collectable;

import java.lang.reflect.Array;
import java.util.*;

public class Monster extends Character implements Movable {

    private Direction direction;

    private Timer moveTimer;

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
     * @param direction direction we want to move in
     * @return true if Monster can walk and false otherwise
     * Monster can't walk on {@link Decor}
     * if Monster walk on {@link Player}
     * Monster can walk on other gameObjects based on there {@link Entity#canWalkOn()} method return
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
            //game.getPlayer().addLives(-1);
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

    @Override
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

        moveTimer.update(now);

        if(getPosition().equals(game.getPlayer().getPosition()))
            game.getPlayer().addLives(-1);

        if(moveTimer.isFinished() || !moveTimer.isRunnig()){
            doMove(randomPostion());
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
