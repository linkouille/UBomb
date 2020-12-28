/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.*;

import fr.ubx.poo.model.go.character.Character;
import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final World[] worlds;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;

    private String prefix;

    private int currentLevel;
    private int nbrOfLevel;

    public Game(String worldPath) {
        // world = new WorldStatic(this);
        this.worldPath = worldPath;
        loadConfig(worldPath);
        worlds = LoadLevels();
        currentLevel = 1;
        Position positionPlayer = null;
        try {
            positionPlayer = worlds[currentLevel - 1].findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private World[] LoadLevels(){
        World[] out = new World[nbrOfLevel];

        for (int i = 0; i < nbrOfLevel; i++){
            out[i] = LoadLevel(i+1);
        }
        return out;
    }

    private World LoadLevel(int id){
        String pathtoLevel = this.worldPath + "/" + this.prefix + id + ".txt";

        try(BufferedReader input = new BufferedReader(new FileReader(pathtoLevel))){
            List<String> stringfile = new ArrayList<String>();
            int xSize = 0, ySize = 0;
            String buf  = input.readLine();

            xSize = buf.length();

            do{
                stringfile.add(buf);
                ySize++;
                buf  = input.readLine();
            }while(buf != null);
            input.close();

            WorldEntity[][] raw = new WorldEntity[ySize][xSize];
            int x = 0, y = 0;

            for (String s : stringfile){
                for(x = 0; x < xSize; x++){
                    WorldEntity e = WorldEntity.fromCode(s.charAt(x)).get();
                    if(e == null)
                        throw new WorldEntityNotFoundException(e + " is an Invalid entity");
                    raw[y][x] = e;
                }
                y++;
            }

            return new World(raw,this);

        } catch (FileNotFoundException e) {
            System.err.println(pathtoLevel + "is unreachable or doesn't exist");
        } catch (IOException e) {
            System.err.println("Error Loading level " + id + " : " + e.getMessage());
        } catch (WorldEntityNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return null;

    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));

            prefix = prop.getProperty("prefix", "level");

            nbrOfLevel = Integer.parseInt(prop.getProperty("levels", "1"));

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return worlds[currentLevel-1];
    }
    public World getWorld(int level) {
        return worlds[level];
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getLevel() {
        return currentLevel;
    }

    public void ChangeLevel(int newLevel){
        currentLevel = newLevel;
    }

    public int getNbrOfLevel() {
        return nbrOfLevel;
    }
}
