package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.collectable.BombCapacity;
import fr.ubx.poo.model.go.collectable.BombRange;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteBombRange extends SpriteGameObject{
    public SpriteBombRange(Pane layer, GameObject go) {
        super(layer, null, go);
        Image img = ImageFactory.getInstance().get(ImageResource.BOMB_RANGE_INC);
        if(go.isCollectable()){
            BombRange bc = (BombRange) go;
            if(bc.getValue() < 0)
                img = ImageFactory.getInstance().get(ImageResource.BOMB_RANGE_DEC);
        }

        this.setImage(img);
    }

    @Override
    public void updateImage() {

    }
}
