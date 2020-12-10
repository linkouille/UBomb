/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.effect.Effect;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    //Not Final cause Some Gameobject like Box could be moving
    private Map<Position, GameObject> gameObjects;
    private Map<Position, Effect> effectObject;

    public World(WorldEntity[][] raw, Game game) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.buildDecor(raw, dimension);
        gameObjects = WorldBuilder.buildGameObject(raw,dimension,game);
        effectObject = new Hashtable<>();
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
        return gameObjects.get(position);
    }

    public Effect getEffect(Position position) {
        return effectObject.get(position);
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void setGO(Position position, GameObject g) {
        gameObjects.put(position, g);
    }

    public void setEffect(Position position, Effect g) {
        effectObject.put(position, g);
    }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void clearGO(Position position) {
        gameObjects.remove(position);
    }

    public void clearEffect(Position position) {
        effectObject.remove(position);
    }

    public void forEachDecor(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public void forEachGameObject(BiConsumer<Position, GameObject> fn) {
        gameObjects.forEach(fn);
    }

    public void forEachEffect(BiConsumer<Position, Effect> fn) {
        effectObject.forEach(fn);
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
