/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.collectable.Key;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if(decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        if(decor instanceof Door)
            return new SpriteDoor(layer,null, position, ((Door)decor));

        return null;
    }

    //TODO
    public static Sprite createGameObject(Pane layer, Position position, GameObject gameObject){
        ImageFactory factory = ImageFactory.getInstance();

        if(gameObject instanceof Box)
            return new SpriteBox(layer,factory.get(BOX),gameObject);
        if(gameObject instanceof Monster)
            return new SpriteMonster(layer,factory.get(MONSTER_LEFT), gameObject);
        if(gameObject instanceof Key)
            return new SpriteKey(layer, factory.get(KEY), gameObject);

        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }
}
