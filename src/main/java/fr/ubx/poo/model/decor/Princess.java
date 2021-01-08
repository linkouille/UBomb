package fr.ubx.poo.model.decor;

public class Princess extends Decor {

    public boolean canWalkOn(){
        return true;
    }

    public boolean isPrincess(){
        return true;
    }
    @Override
    public String toString() {
        return "Princess";
    }
}
