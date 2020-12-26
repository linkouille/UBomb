package fr.ubx.poo.view.sprite.spritegameobject;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.sprite.Sprite;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class SpriteGameObject extends Sprite {
    protected final GameObject go;

    public GameObject getGo() {
        return go;
    }

    public SpriteGameObject(Pane layer, Image image, GameObject go) {
        super(layer, image);
        this.go = go;
    }

    @Override
    public Position getPosition() {
        return go.getPosition();
    }
}
