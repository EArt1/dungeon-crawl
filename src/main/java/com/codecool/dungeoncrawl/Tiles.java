package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static final Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static final Map<String, Tile> tileMapDungeon = new HashMap<>();
    private static final Map<String, Tile> tileMapWood = new HashMap<>();
    private static final Map<String, Tile> tileMapVillage = new HashMap<>();

    private static final Map<String, Tile> tileMapWinner = new HashMap<>();

    static {
        tileMapDungeon.put("empty", new Tile(0, 0));
        tileMapDungeon.put("player", new Tile(27, 0));
        tileMapDungeon.put("wall", new Tile(10, 17));
        tileMapDungeon.put("floor", new Tile(2, 0));
        tileMapDungeon.put("sword", new Tile(0, 29));
        tileMapDungeon.put("key", new Tile(16, 23));
        tileMapDungeon.put("skeleton", new Tile(29, 6));
        tileMapDungeon.put("ogre", new Tile(30, 6));
        tileMapDungeon.put("bat", new Tile(26, 8));
        tileMapDungeon.put("ladder", new Tile(21, 1));
        tileMapDungeon.put("fire", new Tile(28, 11));
        tileMapWood.put("empty", new Tile(0, 0));
        tileMapWood.put("player", new Tile(28, 0));
        tileMapWood.put("wall", new Tile(0, 1));
        tileMapWood.put("floor", new Tile(6, 0));
        tileMapWood.put("tree-wall", new Tile(3, 2));   // two trees
        tileMapWood.put("grass", new Tile(0, 2));
        tileMapWood.put("sword", new Tile(1, 29));
        tileMapWood.put("key", new Tile(18, 23));
        tileMapWood.put("skeleton", new Tile(27, 9));   // spider
        tileMapWood.put("ogre", new Tile(28, 8));       // snake
        tileMapWood.put("bat", new Tile(30, 8));        // bear
        tileMapWood.put("ladder", new Tile(0, 3));      // fence
        tileMapWood.put("fire", new Tile(29, 11));
        tileMapVillage.put("empty", new Tile(0, 0));
        tileMapVillage.put("player", new Tile(29, 0));
        tileMapVillage.put("wall", new Tile(0, 5));
        tileMapVillage.put("floor", new Tile(4, 0));
        tileMapVillage.put("tree-wall", new Tile(4, 2));// two trees
        tileMapVillage.put("grass", new Tile(0, 21));
        tileMapVillage.put("sword", new Tile(2, 29));
        tileMapVillage.put("key", new Tile(17, 23));
        tileMapVillage.put("skeleton", new Tile(26, 7));// chicken
        tileMapVillage.put("ogre", new Tile(29, 8));    // crocodile
        tileMapVillage.put("bat", new Tile(27, 7));     // cow
        tileMapVillage.put("ladder", new Tile(1, 21));  // house
        tileMapVillage.put("fire", new Tile(30, 11));
        tileMapWinner.put("empty", new Tile(0, 0));
        tileMapWinner.put("player", new Tile(27, 0));
        tileMapWinner.put("wall", new Tile(0, 0));
        tileMapWinner.put("floor", new Tile(0, 0));
        tileMapWinner.put("tree-wall", new Tile(28, 31));// W
        tileMapWinner.put("grass", new Tile(27, 30));    // I
        tileMapWinner.put("sword", new Tile(19, 31));    // N
        tileMapWinner.put("key", new Tile(29, 12));      // static firework
        tileMapWinner.put("skeleton", new Tile(28, 12)); // firework
        tileMapWinner.put("ogre", new Tile(0, 0));
        tileMapWinner.put("bat", new Tile(0, 12));
        tileMapWinner.put("ladder", new Tile(0, 0));
    }

    public static void drawTile(int level, GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMapDungeon.get(d.getTileName());
        if (level == 2) {
            tile = tileMapWood.get(d.getTileName());
        }
        if (level == 3) {
            tile = tileMapVillage.get(d.getTileName());
        }
        if (level == 4) {
            tile = tileMapWinner.get(d.getTileName());
        }
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }

    public static class Tile {
        public final int x, y, w, h;

        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }
}
