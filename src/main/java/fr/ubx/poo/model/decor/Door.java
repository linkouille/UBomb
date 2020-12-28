package fr.ubx.poo.model.decor;

public class Door extends Decor {

    private boolean state;

    private final int toLevel;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getToLevel() {
        return toLevel;
    }

    public Door(boolean state, int toLevel) {
        this.toLevel = toLevel;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Princess";
    }

    @Override
    public boolean canWalkOn() {
        return state;
    }


    public boolean isDoor(){
        return true;
    }
}
