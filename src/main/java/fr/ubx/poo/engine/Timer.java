package fr.ubx.poo.engine;

public class Timer {

    private boolean runnig;
    private boolean finished;

    private float initTime;
    private float duration;
    private float currentTime;

    public boolean isRunnig() {
        return runnig;
    }

    public boolean isFinished() {
        return finished;
    }

    public float getInitTime() {
        return initTime;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public Timer(float duration) {
        this.duration = duration;
        this.runnig = false;
        this.finished = true;
    }

    public void StartTimer(long now){
        this.initTime = now;
        this.runnig = true;
        this.finished = false;

    }

    public void update(long now) {
        if(this.runnig){
            currentTime = (now - this.initTime) / 1000000000;
            if( currentTime >= this.duration){
                this.runnig = false;
                this.finished = true;
                return;
            }
        }else{
            currentTime = duration;
        }

    }

}
