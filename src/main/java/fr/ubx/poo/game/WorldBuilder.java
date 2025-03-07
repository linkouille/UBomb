package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.collectable.BombCapacity;
import fr.ubx.poo.model.go.collectable.BombRange;
import fr.ubx.poo.model.go.collectable.Key;
import fr.ubx.poo.model.go.collectable.Life;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private Map<Position, GameObject> gameObjects = new Hashtable<>();

    private WorldBuilder() {
    }

    /** Create all Decor of the World
     * @param raw 2D WorldEntity value of the world
     * @param dimension dimention of the world
     * @return Map that link Decor to his position
     * @see #processDecor(WorldEntity)
     */
    public static Map<Position, Decor> buildDecor(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processDecor(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    /** Create all Decor of the World
     * @param raw 2D WorldEntity value of the world
     * @param dimension dimention of the world
     * @param game current game
     * @return List of GameObjects of the world
     * @see #processGameObject(WorldEntity, Game, Position)
     */
    public static List<GameObject> buildGameObject(WorldEntity[][] raw, Dimension dimension, Game game) {
        List<GameObject> out = new ArrayList<>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                GameObject g = processGameObject(raw[y][x], game, new Position(x,y));
                if (g != null)
                    out.add(g);
            }
        }
        return out;
    }

    /** Transform a WorldEntity to a Decor
     * @param entity the world Entity
     * @return Decor object
     */
    private static Decor processDecor(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Princess:
                return new Princess();
            case DoorNextClosed:
                return new Door(false,1);
            case DoorNextOpened:
                return  new Door(true,1);
            case DoorPrevOpened:
                return  new Door(true,-1);

            default:
                return null;
        }
    }

    /** Transform a WorldEntity to a GameObject
     * @param entity the world Entity
     * @param game current game
     * @param position position of the WorldEntity in the world
     * @return GameObjects object
     */
    private static GameObject processGameObject(WorldEntity entity, Game game, Position position) {
        switch (entity) {
            case Box:
                return new Box(game,position);
            case Monster:
                return new Monster(game, position, 1);
            case Key:
                return new Key(game, position);
            case Heart:
                return new Life(game,position);
            case BombRangeDec:
                return new BombRange(game,position,-1);
            case BombRangeInc:
                return new BombRange(game,position,1);
            case BombNumberDec:
                return new BombCapacity(game, position, -1);
            case BombNumberInc:
                return  new BombCapacity(game, position, 1);
            default:
                return null;
        }
    }
}
