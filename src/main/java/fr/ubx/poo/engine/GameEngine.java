/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.effect.Effect;
import fr.ubx.poo.model.go.effect.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.sprite.*;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
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

        game.getWorld().forEachGameObject((pos,g) -> spritesGO.add(SpriteFactory.createGameObject(layer, pos,g)));

        spritePlayer = SpriteFactory.createPlayer(layer, player);

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

        game.getWorld().forEachGameObject((pos,g) -> {
            g.update(now);
            if(g.isExplosif()){
                Bomb b = (Bomb) g;
                if(b.isExploded()){
                    //Bomb is dead so we explose
                    PlaceExplosionObj(b,now);
                }

            }
        });

        game.getWorld().forEachEffect((pos,g) -> g.update(now));


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
    }
    private void render() {

        Iterator<Sprite> it = spritesGO.iterator();
        while (it.hasNext()){
            Sprite sprite = it.next();
            if(((SpriteGameObject)sprite).getGo().isDead()) {
                game.getWorld().clearGO (((SpriteGameObject)sprite).getGo().getPosition());
                it.remove();
                sprite.remove();
            }
        }
        it = effect.iterator();
        while (it.hasNext()){
            Sprite sprite = it.next();
            if(((SpriteGameObject)sprite).getGo().isDead()) {
                game.getWorld().clearEffect(((SpriteGameObject)sprite).getGo().getPosition());
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
        game.getWorld().setGO(pos, b);
        spritesGO.add(SpriteFactory.createBomb(layer,b));

    }

    private void createExplosionGameObject(Position pos, long now){
        if(game.getWorld().getEffect(pos) != null || !game.getWorld().isInside(pos)){
            return;
        }
        Explosion expl = new Explosion(game, pos, now);
        game.getWorld().setEffect(pos,expl);
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

            //If it's a decor we don't scree explosion
            decor = game.getWorld().get(p);
            if(decor != null)
                break;

            createExplosionGameObject(p,now);
            g = game.getWorld().getGO(p);
            if(g != null){
                if(g.isDestroyable()){
                    g.destroy();
                    if(!g.isCollectable())
                        break;
                }else if(g.isExplosif()){
                    Bomb b = (Bomb) g;
                    b.setExploded(true);

                }


            }
        }

    }

}
