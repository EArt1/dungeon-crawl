package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;

public abstract class Actor implements Drawable {
    public ArrayList<String> items = new ArrayList<>();
    private Cell cell;
    private int health = 10;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (!nextCell.getTileName().equals(CellType.WALL.getTileName()) &&
                !nextCell.getTileName().equals(CellType.TREE_WALL.getTileName())
                && nextCell.getActor() == null) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else if (nextCell.getActor() != null && nextCell.getActor().getTileName() == "player") {
            nextCell.getActor().setHealth(nextCell.getActor().getHealth() - 2);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void addItems(String item) {
        items.add(item);
    }
}
