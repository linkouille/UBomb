package fr.ubx.poo.model.go;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject{

    private int state;

    private Timer timer;

    private int range;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Bomb(Game game, Position position, long now) {
        super(game, position);

        state = 0;

        this.timer = new Timer (4);
        this.timer.StartTimer(now);

    }

    public void update(long now){

        this.timer.update(now);
        this.state = (int)(this.timer.getCurrentTime());
        if(state >= 4){
            state = 3;
        }

        if(this.timer.isFinished())
            this.destroy();

    }
    public boolean canWalkOn(){
        return true;
    }
}
