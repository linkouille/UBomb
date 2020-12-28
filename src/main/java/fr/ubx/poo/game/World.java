/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.effect.Effect;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    //Not Final cause Some Gameobject like Box could be moving
    // private Map<Position, GameObject> gameObjects;
    // private Map<Position, Effect> effectObject;

    private List<GameObject> gameObjects;
    private List<Effect> effectObjects;

    public World(WorldEntity[][] raw, Game game) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.buildDecor(raw, dimension);
        gameObjects = WorldBuilder.buildGameObject(raw,dimension,game);
        effectObjects = new ArrayList<>();
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public GameObject getGO(Position position) {
        for (GameObject go : gameObjects) {
            if(go.getPosition().equals(position))
                return go;

        }
        return null;
    }

    public Effect getEffect(Position position) {

        for (Effect e: effectObjects) {
            if(e.getPosition().equals(position))
                return e;
        }
        return null;
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void setGO(GameObject go){ gameObjects.add(go); }

    public void setEffect(Effect effect){ effectObjects.add(effect); }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void clearGO(GameObject go) {
        gameObjects.remove(go);
    }

    public void clearEffect(Effect effect) {
        effectObjects.remove(effect);
    }

    public void forEachDecor(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public void forEachGameObject(Consumer<GameObject> fn) {
        gameObjects.forEach(fn);
    }

    public void forEachEffect(Consumer<Effect> fn) {
        effectObjects.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return position.inside(this.dimension); // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }
}
