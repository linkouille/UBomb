package fr.ubx.poo.engine;

/**
 * Timer class
 * duration is created at startup
 * Timer start when you call {@link #StartTimer(long)}
 */
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

    public void setDuration(float duration) {
        this.duration = duration;
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

    /**
     * Update timer time and check if the timer has ended
     * @param now current time in nanosecond
     */
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
