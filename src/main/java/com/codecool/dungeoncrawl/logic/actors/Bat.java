package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.DiagonalDirection;


public class Bat extends Actor {


    public Bat(Cell cell) {
        super(cell);
    }

    public void move(DiagonalDirection direction) {
        int dx = direction.getX();
        int dy = direction.getY();
        super.move(dx, dy);
    }


    @Override
    public String getTileName() {
        return "bat";
    }
}
