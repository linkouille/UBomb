package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.Sprite;
import javafx.scene.layout.Pane;

/**
 * {@link Sprite} of a {@link Bomb}
 * Change with the current state {@link Bomb#getState()}
 */
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
