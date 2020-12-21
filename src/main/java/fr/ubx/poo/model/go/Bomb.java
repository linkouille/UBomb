package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.decor.Decor;

public class Bomb extends GameObject{

    private int state;

    private Timer timer;

    private int range;

    private boolean exploded;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public Bomb(Game game, Position position, int range, long now) {
        super(game, position);

        state = 0;

        this.timer = new Timer (4);
        this.timer.StartTimer(now);
        this.range = range;
        this.exploded = false;

    }

    public void update(long now){

        this.timer.update(now);
        this.state = (int)(this.timer.getCurrentTime());
        if(state >= 4){
            state = 3;
        }

        if(this.timer.isFinished()) {
            exploded = true;
        }
    }
    public boolean canWalkOn(){
        return true;
    }

    public boolean isExplosif(){
        //Can Explose
        return true;
    }
}
