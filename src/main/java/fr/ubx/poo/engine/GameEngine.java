/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Character;
import fr.ubx.poo.model.go.effect.Effect;
import fr.ubx.poo.model.go.effect.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.sprite.*;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.spritegameobject.SpriteGameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;

    //Not Final because gameObject could move;
    private List<Sprite> spritesGO = new ArrayList<>();
    private List<Sprite> effect = new ArrayList<>(); //Explosion

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getWorld().forEachDecor( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        game.getWorld().forEachGameObject((g) -> spritesGO.add(SpriteFactory.createGameObject(layer, g.getPosition(),g)));

        spritePlayer = SpriteFactory.createPlayer(layer, player);

    }

    private void ChangeLevel(int i){
        stage.close();
        game.ChangeLevel(i);
        initialize(stage,game);

        //We need to update player pos

        Position[] playerPos = new Position[1]; // to get pos from lambda call
        game.getWorld().forEachDecor((pos,d) -> {
            if(d.isDoor()){
                Door door = (Door) d;
                if(door.isState()){
                    if(game.getLevel() == 1){ // We are level 1 so we need a Next door
                        if(door.getToLevel() > 0)
                            playerPos[0] = pos;
                    }else{
                        if(door.getToLevel() < 0)
                            playerPos[0] = pos;
                    }
                }
            }
        });
        if(playerPos[0] == null)
            throw new RuntimeException("Can't place player");
        player.setPosition(playerPos[0]);

    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if(input.isBomb()){
            player.Act(now);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);

        game.getWorld().forEachGameObject((g) -> {
            g.update(now);

            if(g.isExplosif()){
                Bomb b = (Bomb) g;
                if(b.isExploded()){
                    //Bomb is dead so we explose
                    game.getPlayer().modiffNbrBombPlaced(-1);
                    PlaceExplosionObj(b,now);
                }

            }
        });

        game.getWorld().forEachEffect((e) -> e.update(now));


        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }

        if(player.isPlaceBomb()){
            createBombGameObject(player.getPosition(), now);
            player.setPlaceBomb(false);
        }
        if(player.isNexLevel()){
            ChangeLevel(player.getToLevel());
            player.setNexLevel(false);
        }
    }
    private void render() {

        Iterator<Sprite> it = spritesGO.iterator();
        while (it.hasNext()){
            Sprite sprite = it.next();
            if(((SpriteGameObject)sprite).getGo().isDead()) {
                game.getWorld().clearGO (((SpriteGameObject)sprite).getGo());
                it.remove();
                sprite.remove();
            }
        }

        it = effect.iterator();
        while (it.hasNext()){
            Sprite sprite = it.next();
            if(((SpriteGameObject)sprite).getGo().isDead()) {
                game.getWorld().clearEffect((Effect) ((SpriteGameObject)sprite).getGo());
                it.remove();
                sprite.remove();
            }
        }

        sprites.forEach(Sprite::render);
        spritesGO.forEach(Sprite::render);

        // last rendering to have player in the foreground
        spritePlayer.render();

        //We suppose Effect is over everything
        effect.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    private void createBombGameObject(Position pos, long now){
        Bomb b = new Bomb(game,pos, game.getPlayer().getRange(),now);
        game.getWorld().setGO(b);
        spritesGO.add(SpriteFactory.createBomb(layer,b));

    }

    private void createExplosionGameObject(Position pos, long now){
        if(game.getWorld().getEffect(pos) != null || !game.getWorld().isInside(pos)){
            return;
        }
        Explosion expl = new Explosion(game, pos, now);
        game.getWorld().setEffect(expl);
        effect.add(SpriteFactory.createExplosion(layer,expl));

    }

    private void PlaceExplosionObj(Bomb b, long now){
        //Screen and then destroy

        createExplosionGameObject(b.getPosition(),now);
        IttDirectionExpl(Direction.E, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.S, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.W, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.N, b.getRange(), b.getPosition(),now);

        b.destroy();

    }
    private void IttDirectionExpl(Direction dir, int itt, Position pos, long now){
        Position p = pos;

        GameObject g;
        Decor decor;

        for (int i = 0; i < itt; i++){
            p = dir.nextPosition(p);

            //If it's a decor we don't screen explosion
            decor = game.getWorld().get(p);
            if(decor != null) {
                break;
            }
            // If it's a decor then we alway display the explosion
            createExplosionGameObject(p,now);
            if(p.equals(player.getPosition())) { // We found the player so he take 1 hp
                player.addLives(-1);
            } else { // Maybe it's a gameobject
                g = game.getWorld().getGO(p);
                if (g != null) {
                    if (g.isDestroyable()) { // We found a destroyable gameobject
                        g.destroy();
                        if (!g.isCollectable()) // If the gameobject isn't a collectable we stop
                            break;
                    } else if (g.isExplosif()) { // We found another bomb
                        Bomb b = (Bomb) g;
                        b.setExploded(true);

                    } else if (g.isCharacter()) { // We found a monster
                        Character c = (Character) g;
                        c.addLives(-1);
                    }

                }
            }
        }

    }

}
