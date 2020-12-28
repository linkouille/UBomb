package fr.ubx.poo.model.decor;

public class Princess extends Decor {
    @Override
    public String toString() {
        return "Princess";
    }
    public boolean canWalkOn(){
        return true;
    }

    public boolean isPrincess(){
        return true;
    }
}
