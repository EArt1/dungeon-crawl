package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;

public class Skeleton extends Actor {


    public Skeleton(Cell cell) {
        super(cell);
    }

    public void move(Direction direction) {
        int dx = direction.getX();
        int dy = direction.getY();
        super.move(dx, dy);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }


}
