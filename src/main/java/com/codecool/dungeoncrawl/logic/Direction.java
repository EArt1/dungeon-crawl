package com.codecool.dungeoncrawl.logic;

import java.util.Random;

public enum Direction {
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1);

    static Random random = new Random();
    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getRandomDirection() {
        Direction[] values = Direction.values();
        return values[random.nextInt(values.length)];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
