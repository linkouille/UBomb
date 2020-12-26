package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject{
    public SpriteExplosion(Pane layer, GameObject go) {
        super(layer, ImageFactory.getInstance().get(ImageResource.EXPLOSION), go);
    }

    @Override
    public void updateImage() {

    }
}
