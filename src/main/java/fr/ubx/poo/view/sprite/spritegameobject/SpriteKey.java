package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.sprite.Sprite;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * {@link Sprite} of a {@link fr.ubx.poo.model.go.collectable.Key}
 */
public class SpriteKey extends SpriteGameObject {
    public SpriteKey(Pane layer, Image image, GameObject go) {
        super(layer, image, go);
    }

    @Override
    public void updateImage() {

    }
}
