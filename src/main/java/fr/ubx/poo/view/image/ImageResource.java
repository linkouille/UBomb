package fr.ubx.poo.view.image;

import javafx.scene.image.Image;

public enum ImageResource {
    BANNER_BOMB ("banner_bomb.png"),
    BANNER_RANGE ("banner_range.png"),
    HEART("heart.png"),
    KEY("key.png"),
    DIGIT_0 ("banner_0.jpg"),
    DIGIT_1 ("banner_1.jpg"),
    DIGIT_2 ("banner_2.jpg"),
    DIGIT_3 ("banner_3.jpg"),
    DIGIT_4 ("banner_4.jpg"),
    DIGIT_5 ("banner_5.jpg"),
    DIGIT_6 ("banner_6.jpg"),
    DIGIT_7 ("banner_7.jpg"),
    DIGIT_8 ("banner_8.jpg"),
    DIGIT_9 ("banner_9.jpg"),
    PLAYER_DOWN("player_down.png"),
    PLAYER_LEFT("player_left.png"),
    PLAYER_RIGHT("player_right.png"),
    PLAYER_UP("player_up.png"),
    STONE("stone.png"),
    TREE("tree.png"),
    BOX("box.png"),
    PRINCESS("bomberwoman.png"),
    MONSTER_DOWN("monster_down.png"),
    MONSTER_LEFT("monster_left.png"),
    MONSTER_RIGHT("monster_right.png"),
    MONSTER_UP("monster_up.png"),
    DOOR_OPEN("door_opened.png"),
    DOOR_CLOSE("door_closed.png"),
    BOMB_1("bomb4.png"),
    BOMB_2("bomb3.png"),
    BOMB_3("bomb2.png"),
    BOMB_4("bomb1.png"),
    EXPLOSION("explosion.png"),
    BOMB_RANGE_INC("bonus_bomb_range_inc.png"),
    BOMB_RANGE_DEC("bonus_bomb_range_dec.png"),
    BOMB_CAPACITY_INC("bonus_bomb_nb_inc.png"),
    BOMB_CAPACITY_DEC("bonus_bomb_nb_dec.png")
    ;

    private final String FileName;

    ImageResource(String fileName) {
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }
}
