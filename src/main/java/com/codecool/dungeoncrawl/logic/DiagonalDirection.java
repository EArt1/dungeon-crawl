package com.codecool.dungeoncrawl.logic;

import java.util.Random;

public enum DiagonalDirection {
    TOP_RIGHT(1, -1),
    TOP_LEFT(-1, -1),
    BOTTOM_RIGHT(1, 1),
    BOTTOM_LEFT(-1, 1);

    static Random random = new Random();
    private final int x;
    private final int y;

    DiagonalDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static DiagonalDirection getRandomDiagonalDirection() {
        DiagonalDirection[] values = DiagonalDirection.values();
        return values[random.nextInt(values.length)];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
