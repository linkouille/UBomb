package fr.ubx.poo.model.go.effect;

import fr.ubx.poo.engine.Timer;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Explosion extends Effect {

    private Timer timer;

    public Explosion(Game game, Position position, long now) {
        super(game, position);
        this.timer = new Timer(1);

        this.timer.StartTimer(now);
    }

    public void update(long now){

        this.timer.update(now);

        if(this.timer.isFinished()) {
            this.destroy();
        }
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }
}
