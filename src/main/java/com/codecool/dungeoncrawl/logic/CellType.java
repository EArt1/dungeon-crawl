package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    GRASS("grass"),
    WALL("wall"),
    TREE_WALL("tree-wall"),
    SWORD("sword"),
    KEY("key"),
    FIRE("fire"),
    LADDER("ladder");


    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
