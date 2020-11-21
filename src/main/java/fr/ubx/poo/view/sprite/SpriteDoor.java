package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends SpriteDecor {

    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public SpriteDoor(Pane layer, Image image, Position position, boolean state) {
        super(layer, image, position);
        this.state = state;
    }

    @Override
    public void updateImage() {
        if(state)
            setImage(ImageFactory.getInstance().get(ImageResource.DOOR_OPEN));
        else
            setImage(ImageFactory.getInstance().get(ImageResource.DOOR_CLOSE));

    }
}
