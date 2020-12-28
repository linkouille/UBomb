package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject {

    private final ColorAdjust effect = new ColorAdjust();
    public SpriteMonster(Pane layer, Image image, GameObject go) {
        super(layer, image, go);
    }

    @Override
    public void updateImage() {

        Monster m = (Monster) go;
        setImage(ImageFactory.getInstance().getMonster(m.getDirection()));

    }
}
