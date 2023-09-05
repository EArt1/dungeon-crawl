package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;

public class Ogre extends Actor {

    boolean ogreMoves = false;

    public Ogre(Cell cell) {
        super(cell);
    }

    public boolean isAbleToMove(Player player) {
        if (player.getX() == 31) {
            ogreMoves = true;
        }
        return ogreMoves;
    }

    public void move(Player player) {
        if (isAbleToMove(player)) {
            int dx = directionToPlayer(player).getX();
            int dy = directionToPlayer(player).getY();
            super.move(dx, dy);
        }
    }

    public Direction directionToPlayer(Player player) {
        if (Math.abs((getCell().getX()) - player.getX()) >
                Math.abs((getCell().getY()) - player.getY())) {
            return getCell().getX() > player.getX() ? Direction.LEFT : Direction.RIGHT;
        } else {
            return getCell().getY() > player.getY() ? Direction.UP : Direction.DOWN;
        }
    }


    @Override
    public String getTileName() {
        return "ogre";
    }
}
