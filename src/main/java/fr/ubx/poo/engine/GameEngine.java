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

    /** GameEngine constructor
     * @param windowTitle The title of the window
     * @param game The game the engine is running containing the world, the player and other elements
     * @param stage JavaFX stage
     * @see Game
     * @see #initialize(Stage, Game)
     * @see #buildAndSetGameLoop()
     */
    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    /** initialisation and loading of the game world, sprites and the window with JavaFX
     * The size of the window depends of the world dimension
     * We load the sprite of all of the sprites we will use in this world/level (decor, gameobject, and effect)
     * @param stage JavaFX stage
     * @param game current game
     */
    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        //Creation of the window
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

        //Creation of the input system
        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create decor sprites
        game.getWorld().forEachDecor((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        // Create GameObject with itterator because we will delete gameobject if he is dead before level is loading
        Iterator<GameObject> itGO = game.getWorld().itterateGameObjects();
        while(itGO.hasNext()){
            GameObject g = itGO.next();
            if(!g.isDead()) {
                spritesGO.add(SpriteFactory.createGameObject(layer, g.getPosition(), g));
            }else{
                itGO.remove();
            }
        }

        spritePlayer = SpriteFactory.createPlayer(layer, player);

    }

    /** Method called when the player collide with a door and want to change level
     * @param i offset for the next level 1: the next level -1: the previous level
     */
    private void ChangeLevel(int i){
        stage.close();

        game.ChangeLevel(i + game.getLevel());
        initialize(stage,game);

        //We need to update player pos

        player.setPosition(findPlayerPosition(i));


    }

    /** Find the position of the player during a level changement
     * @param toLvl offset for the next level 1: the next level -1: the previous level
     * @return Position of the player for the currently loaded world
     */
    private Position findPlayerPosition(int toLvl){
        Position[] playerPos = new Position[1]; // to get pos from lambda call

        game.getWorld().forEachDecor((pos,d) -> {
            if(d.isDoor()){
                Door door = (Door) d;
                if(door.isState()){
                    if(toLvl < 0){
                        if(door.getToLevel() > 0){
                            System.out.println(d);
                            playerPos[0] = pos;
                        }

                    }else {
                        if(door.getToLevel() < 0){
                            playerPos[0] = pos;
                        }

                    }
                }
            }
        });
        if(playerPos[0] == null)
            throw new RuntimeException("Can't place player");

        return playerPos[0];
    }

    /**
     * Build and Set gameLoop with Input, Update and Render
     * @see #processInput(long)
     * @see #update(long)
     * @see #render()
     */
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

    /** Process input from the keyboard Z, Q, S, D, ESC and SPACE
     * @param now current time in microseconds
     * @see Input
     */
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
            player.Placebomb(now);
        }
        if(input.isKey()){
            player.UseKey();
        }
        input.clear();
    }

    /** Display a message with a new Window
     * used to Display when the player is winning or for a gameover
     * @param msg The message we want to display
     * @param color The color of the message
     */
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


    /** Update Action of the gameobjects
     * Check if player win, lose, place a bomb or goes to the next level
     * @param now current time in ms
     * @see GameObject
     * @see Effect
     * @see Player
     */
    private void update(long now) {
        player.update(now);

        game.getWorld().forEachGameObject((g) -> {
            g.update(now);

            if(g.isExplosif()){
                Bomb b = (Bomb) g;
                if(b.isExploded()){
                    //Bomb is dead so we explode
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

    /**
     * render all the sprites in Arrays sprite, spritesGO, spritePlayer and effect (order of render)
     * @see Sprite
     */
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

    /**
     * Start gameLoop
     */
    public void start() {
        gameLoop.start();
    }

    /** Create a new Bomb at pos and add the sprite in spriteGO to render him
     * @param pos the position of the Bomb
     * @param now current time in microseconds
     * @see Bomb
     */
    private void createBombGameObject(Position pos, long now){
        Bomb b = new Bomb(game,pos, game.getPlayer().getRange(),now);
        game.getWorld().setGO(b);
        spritesGO.add(SpriteFactory.createBomb(layer,b));

    }

    /** Create a new Explosion effect at pos and add the sprite in effect to render him
     * @param pos The position of the Explosion
     * @param now current time in microseconds
     * @see Explosion
     */
    private void createExplosionGameObject(Position pos, long now){
        if(game.getWorld().getEffect(pos) != null || !game.getWorld().isInside(pos)){
            return;
        }
        Explosion expl = new Explosion(game, pos, now);
        game.getWorld().setEffect(expl);
        effect.add(SpriteFactory.createExplosion(layer,expl));

    }

    /** Place Explosions b when the Bomb explode
     * It places Explosion in the position of b and then call IttDirectionExpl in the 4 directions.
     * @param b Bomb object that exploded
     * @param now current time in microseconds
     * @see #IttDirectionExpl(Direction, int, Position, long)
     * @see #createExplosionGameObject(Position, long)
     */
    private void PlaceExplosionObj(Bomb b, long now){
        //Screen and then destroy
        createExplosionGameObject(b.getPosition(),now);

        //Check the actual pos
        CheckPos(b.getPosition(),now); // We don't need to check the return of this call

        IttDirectionExpl(Direction.E, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.S, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.W, b.getRange(), b.getPosition(),now);
        IttDirectionExpl(Direction.N, b.getRange(), b.getPosition(),now);

        b.destroy();

    }

    /** Itter range time in the direction dir from pos (excluded) and call CheckPos
     * @param dir direction
     * @param range the range of the Bomb
     * @param pos the original position
     * @param now current time in microseconds
     */
    private void IttDirectionExpl(Direction dir, int range, Position pos, long now){
        Position p = pos;
        int out;


        for (int i = 0; i < range; i++){
            p = dir.nextPosition(p);

            out = CheckPos(p, now);
            if(out == -1)
                break;
        }

    }

    /** Check for the position p if there is a decor, a destructible object, the player or a monster
     * @param p possition we want to check
     * @param now current time in microseconds
     * @return -1 if we need to stop the explosion 0 otherwise
     */
    private int CheckPos(Position p, long now){
        GameObject g;
        Decor decor;

        //If it's a decor we don't screen explosion
        decor = game.getWorld().get(p);
        if(decor != null) {
            return -1;
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
                        return -1;
                } else if (g.isExplosif()) { // We found another bomb
                    Bomb b = (Bomb) g;
                    b.setExploded(true);

                } else if (g.isCharacter()) { // We found a monster
                    Character c = (Character) g;
                    c.addLives(-1);
                }

            }
        }
        return 0;
    }

}
