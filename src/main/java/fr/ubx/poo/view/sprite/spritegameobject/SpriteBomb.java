package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject {
    public SpriteBomb(Pane layer, GameObject go) {
        super(layer, null, go);
        state = 0;
    }

    private int state;

    @Override
    public void updateImage() {
        state = ((Bomb)go).getState();
        setImage(ImageFactory.getInstance().getBomb(state));

    }
}
