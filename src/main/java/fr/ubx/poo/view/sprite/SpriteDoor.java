package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * {@link Sprite} of a {@link Door}
 * Can be open or not {@link Door#isState()}
 */
public class SpriteDoor extends SpriteDecor {

    private boolean state;

    private Door door;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public SpriteDoor(Pane layer, Image image, Position position, Door door) {
        super(layer, image, position);
        this.state = door.isState();
        this.door = door;
    }

    @Override
    public void updateImage() {
        this.state = door.isState();

        if(state)
            setImage(ImageFactory.getInstance().get(ImageResource.DOOR_OPEN));
        else
            setImage(ImageFactory.getInstance().get(ImageResource.DOOR_CLOSE));

    }
}
