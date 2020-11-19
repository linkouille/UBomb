package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.go.Box;
import fr.ubx.poo.model.go.GameObject;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private Map<Position, GameObject> gameObjects = new Hashtable<>();

    private WorldBuilder() {
    }

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

    public static Map<Position, GameObject> buildGameObject(WorldEntity[][] raw, Dimension dimension, Game game) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                GameObject g = processGameObject(raw[y][x], game, new Position(x,y));
                if (g != null)
                    builder.gameObjects.put(pos, g);
            }
        }
        return builder.gameObjects;
    }

    private static Decor processDecor(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            default:
                return null;
        }
    }
    private static GameObject processGameObject(WorldEntity entity, Game game, Position position) {
        switch (entity) {
            case Box:
                return new Box(game,position);
            default:
                return null;
        }
    }
}
