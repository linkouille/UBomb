package fr.ubx.poo.model.decor;

public class Door extends Decor {

    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Door(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Princess";
    }
}
