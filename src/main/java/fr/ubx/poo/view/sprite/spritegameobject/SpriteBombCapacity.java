package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.collectable.BombCapacity;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteBombCapacity extends SpriteGameObject{

    public SpriteBombCapacity(Pane layer, GameObject go) {
        super(layer, null, go);
        Image img = ImageFactory.getInstance().get(ImageResource.BOMB_CAPACITY_INC);
        if(go.isCollectable()){
            BombCapacity bc = (BombCapacity) go;
            if(bc.getValue() < 0)
                img = ImageFactory.getInstance().get(ImageResource.BOMB_CAPACITY_DEC);
        }

        this.setImage(img);

    }

    @Override
    public void updateImage() {

    }
}
