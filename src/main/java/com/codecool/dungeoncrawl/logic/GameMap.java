package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Bat;
import com.codecool.dungeoncrawl.logic.actors.Ogre;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public Ogre ogre;
    private final int width;
    private final int height;
    private final Cell[][] cells;
    private Player player;
    private Skeleton skeleton;
    private final List<Skeleton> skeletonList = new ArrayList<>();
    private Bat bat;
    private final List<Bat> batList = new ArrayList<>();

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
        skeletonList.add(this.skeleton);
    }

    public List<Skeleton> getSkeletonList() {
        return skeletonList;
    }

    public Ogre getOgre() {
        return ogre;
    }

    public void setOgre(Ogre ogre) {
        this.ogre = ogre;
    }

    public Bat getBat() {
        return bat;
    }

    public void setBat(Bat bat) {
        this.bat = bat;
        batList.add(this.bat);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<Bat> getBatList() {
        return batList;
    }
}
