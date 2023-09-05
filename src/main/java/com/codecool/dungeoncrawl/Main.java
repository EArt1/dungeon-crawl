package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Bat;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class Main extends Application {
    Alert loose = new Alert(Alert.AlertType.NONE);
    Label healthLabel = new Label();
    Label inventory = new Label();
    Label nameLabel = new Label();
    GameDatabaseManager dbManager;
    BorderPane borderPane = new BorderPane();
    GridPane ui = new GridPane();
    Scene scene;
    private int level = 1;
    GameMap map = MapLoader.loadMap(level);
    Canvas canvas = new Canvas(
            Math.min(map.getWidth(), 30) * Tiles.TILE_WIDTH,
            Math.min(map.getHeight(), 22) * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    private String player = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Please enter your name adventurer!");


        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.add(new Label("Name: "), 0, 0);
        ui.add(nameLabel, 1, 0);
        ui.add(new Label(" "), 0, 2);
        ui.add(new Label("Health: "), 0, 1);
        ui.add(healthLabel, 1, 1);
        ui.add(new Label(" "), 1, 2);
        ui.add(new Label("Items: "), 0, 3);
        ui.add(inventory, 1, 3);


        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        scene = new Scene(borderPane);
        GameInformation gameInformation = new GameInformation(primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWING, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Optional<String> result = td.showAndWait();
                player = td.getEditor().getText();
                result.ifPresent(name -> {
                    player = name;
                    nameLabel.setText(player);
                });
            }
        });
        refresh();

        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        Timeline monsterMove = new Timeline(new KeyFrame(Duration.seconds(0.25), this::gameEnemyMove));
        monsterMove.setCycleCount(Animation.INDEFINITE);
        monsterMove.play();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void gameEnemyMove(ActionEvent actionEvent) {
        for (Skeleton skeleton : map.getSkeletonList()) {
            skeleton.move(Direction.getRandomDirection());
        }
        for (Bat bat : map.getBatList()) {
            bat.move(DiagonalDirection.getRandomDiagonalDirection());
        }
        if (map.getOgre() != null) {
            map.getOgre().move(map.getPlayer());
        }
        refresh();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), this::swordPunch));
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                changeLevel(map.getPlayer());
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                changeLevel(map.getPlayer());
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                changeLevel(map.getPlayer());
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
                changeLevel(map.getPlayer());
                refresh();
                break;
            case E:
                if (map.getPlayer().getCell().getTileName().equals(CellType.KEY.getTileName()) || map.getPlayer().getCell().getTileName().equals(CellType.SWORD.getTileName())) {
                    map.getPlayer().addItems(map.getPlayer().getCell().getType().getTileName());
                    map.getPlayer().getCell().setType(CellType.FLOOR);
                }
                refresh();
                break;
            case W:
                if (map.getPlayer().getCell().getNeighbor(0, -1).getType().equals(CellType.FLOOR)) {
                    map.getPlayer().getCell().getNeighbor(0, -1).setType(CellType.FIRE);
                    if (map.getPlayer().getCell().getNeighbor(0, -1).getActor() != null) {
                        removeEnemy(map.getPlayer().getCell().getNeighbor(0, -1).getActor());
                        map.getPlayer().getCell().getNeighbor(0, -1).setActor(null);
                    }
                }
                if (map.getPlayer().getCell().getNeighbor(0, -2).getType().equals(CellType.FLOOR)) {
                    map.getPlayer().getCell().getNeighbor(0, -2).setType(CellType.FIRE);
                    if (map.getPlayer().getCell().getNeighbor(0, -2).getActor() != null) {
                        removeEnemy(map.getPlayer().getCell().getNeighbor(0, -2).getActor());
                        map.getPlayer().getCell().getNeighbor(0, -2).setActor(null);
                    }
                }
                refresh();
                timeline.setCycleCount(1);
                timeline.play();
                break;
            case A:
                if (map.getPlayer().getCell().getNeighbor(-1, 0).getType().equals(CellType.FLOOR)) {
                    map.getPlayer().getCell().getNeighbor(-1, 0).setType(CellType.FIRE);
                    if (map.getPlayer().getCell().getNeighbor(-1, 0).getActor() != null) {
                        removeEnemy(map.getPlayer().getCell().getNeighbor(-1, 0).getActor());
                        map.getPlayer().getCell().getNeighbor(-1, 0).setActor(null);
                    }
                    if (map.getPlayer().getCell().getNeighbor(-2, 0).getType().equals(CellType.FLOOR)) {
                        map.getPlayer().getCell().getNeighbor(-2, 0).setType(CellType.FIRE);
                        if (map.getPlayer().getCell().getNeighbor(-2, 0).getActor() != null) {
                            removeEnemy(map.getPlayer().getCell().getNeighbor(-2, 0).getActor());
                            map.getPlayer().getCell().getNeighbor(-2, 0).setActor(null);
                        }
                    }
                }
                refresh();
                timeline.setCycleCount(1);
                timeline.play();
                break;
            case S:
                if (map.getPlayer().getCell().getNeighbor(0, +1).getType().equals(CellType.FLOOR)) {
                    map.getPlayer().getCell().getNeighbor(0, +1).setType(CellType.FIRE);
                    if (map.getPlayer().getCell().getNeighbor(0, +1).getActor() != null) {
                        removeEnemy(map.getPlayer().getCell().getNeighbor(0, +1).getActor());
                        map.getPlayer().getCell().getNeighbor(0, +1).setActor(null);
                    }
                    if (map.getPlayer().getCell().getNeighbor(0, +2).getType().equals(CellType.FLOOR)) {
                        map.getPlayer().getCell().getNeighbor(0, +2).setType(CellType.FIRE);
                        if (map.getPlayer().getCell().getNeighbor(0, +2).getActor() != null) {
                            removeEnemy(map.getPlayer().getCell().getNeighbor(0, +2).getActor());
                            map.getPlayer().getCell().getNeighbor(0, +2).setActor(null);
                        }
                    }

                }
                refresh();
                timeline.setCycleCount(1);
                timeline.play();
                break;
            case D:
                if (map.getPlayer().getCell().getNeighbor(+1, 0).getType().equals(CellType.FLOOR)) {
                    map.getPlayer().getCell().getNeighbor(+1, 0).setType(CellType.FIRE);
                    if (map.getPlayer().getCell().getNeighbor(+1, 0).getActor() != null) {
                        removeEnemy(map.getPlayer().getCell().getNeighbor(+1, 0).getActor());
                        map.getPlayer().getCell().getNeighbor(+1, 0).setActor(null);
                    }
                    if (map.getPlayer().getCell().getNeighbor(+2, 0).getType().equals(CellType.FLOOR)) {
                        map.getPlayer().getCell().getNeighbor(+2, 0).setType(CellType.FIRE);
                        if (map.getPlayer().getCell().getNeighbor(+2, 0).getActor() != null) {
                            removeEnemy(map.getPlayer().getCell().getNeighbor(+2, 0).getActor());
                            map.getPlayer().getCell().getNeighbor(+2, 0).setActor(null);
                        }
                    }
                }
                refresh();
                timeline.setCycleCount(1);
                timeline.play();
                break;
            case Q:
                Player player = map.getPlayer();
                dbManager.savePlayer(player);
                break;

        }
    }

    private void swordPunch(ActionEvent actionEvent) {
        for (Cell[] i : map.getCells()) {
            for (Cell j : i) {
                if (j.getType().equals(CellType.FIRE)) {
                    j.setType(CellType.FLOOR);
                }
            }

        }
        refresh();
    }

    private void removeEnemy(Actor i) {
        if (i.getTileName() == "skeleton") {
            map.getSkeletonList().remove(i);
        } else if (i.getTileName() == "bat") {
            map.getBatList().remove(i);
        } else if (i.getTileName() == "ogre") {
            map.ogre = null;
        }
    }


    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int centerX = (int) (canvas.getWidth() / (Tiles.TILE_WIDTH * 2));
        int centerY = (int) (canvas.getHeight() / (Tiles.TILE_WIDTH * 2)) - 1;
        int[] focus = new int[2];
        if (map.getPlayer().getX() > centerX) {
            focus[0] = map.getPlayer().getX() - centerX;
        }
        if (map.getPlayer().getY() > centerY) {
            focus[1] = map.getPlayer().getY() - centerY;
        }
        for (int x = 0; x + focus[0] < map.getWidth(); x++) {
            for (int y = 0; y + focus[1] < map.getHeight(); y++) {
                Cell cell = map.getCell(x + focus[0], y + focus[1]);
                if (cell.getActor() != null) {
                    Tiles.drawTile(level, context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(level, context, cell, x, y);
                }
            }
        }

        healthLabel.setText("" + map.getPlayer().getHealth());
        String items = "";
        for (String item : map.getPlayer().getItems()) {
            items += item + "\n";
        }
        inventory.setText(items);
    }

    public void changeLevel(Player player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert gate = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Escape!");
        alert.setHeaderText("Etched on the wall reads following text:");
        alert.setContentText("You need all keys to escape the dungeon!");
        gate.setTitle("");
        gate.setHeaderText("Welcome to Lockwood Village");
        gate.setContentText("");

        int keys = Collections.frequency(map.getPlayer().getItems(), "key");
        if (!(keys == 2) && player.getX() == 59 && level == 1) {
            alert.showAndWait();
        }
        if (keys == 2 && player.getX() == 59 && level == 1) {
            level++;
            map = MapLoader.loadMap(level);
            canvas = new Canvas(
                    Math.min(map.getWidth(), 30) * Tiles.TILE_WIDTH,
                    Math.min(map.getHeight(), 22) * Tiles.TILE_WIDTH);
            context = canvas.getGraphicsContext2D();
            ui = new GridPane();
            ui.setPrefWidth(200);
            ui.setPadding(new Insets(10));

            ui.add(new Label("Health: "), 0, 0);
            ui.add(healthLabel, 1, 0);
            ui.add(new Label(" "), 0, 1);
            ui.add(new Label("Items: "), 0, 2);
            ui.add(inventory, 1, 2);
            borderPane = new BorderPane();
            borderPane.setCenter(canvas);
            borderPane.setRight(ui);

            scene = new Scene(borderPane);
            refresh();
            GameInformation.primaryStage.setScene(scene);

            scene.setOnKeyPressed(this::onKeyPressed);

            GameInformation.primaryStage.setTitle("Dungeon Crawl");
            GameInformation.primaryStage.show();
            refresh();
        }
        if (level == 2 && player.getX() == 53 && player.getY() == 10) {
            gate.show();
        }
        if (level == 2 && player.getX() == 53 && player.getY() == 11) {
            level++;
            map = MapLoader.loadMap(level);
            canvas = new Canvas(
                    Math.min(map.getWidth(), 30) * Tiles.TILE_WIDTH,
                    Math.min(map.getHeight(), 22) * Tiles.TILE_WIDTH);
            context = canvas.getGraphicsContext2D();
            ui = new GridPane();
            ui.setPrefWidth(200);
            ui.setPadding(new Insets(10));

            ui.add(new Label("Health: "), 0, 0);
            ui.add(healthLabel, 1, 0);
            ui.add(new Label(" "), 0, 1);
            ui.add(new Label("Items: "), 0, 2);
            ui.add(inventory, 1, 2);
            borderPane = new BorderPane();
            borderPane.setCenter(canvas);
            borderPane.setRight(ui);

            scene = new Scene(borderPane);
            refresh();
            GameInformation.primaryStage.setScene(scene);

            scene.setOnKeyPressed(this::onKeyPressed);

            GameInformation.primaryStage.setTitle("Dungeon Crawl");
            GameInformation.primaryStage.show();
            refresh();
        }

        if (level == 3 && player.getX() == 29 && player.getY() == 16) {
            level++;
            map = MapLoader.loadMap(level);
            canvas = new Canvas(
                    Math.min(map.getWidth(), 30) * Tiles.TILE_WIDTH,
                    Math.min(map.getHeight(), 22) * Tiles.TILE_WIDTH);
            context = canvas.getGraphicsContext2D();
            ui = new GridPane();
            ui.setPrefWidth(200);
            ui.setPadding(new Insets(10));

            ui.add(new Label("Health: "), 0, 0);
            ui.add(healthLabel, 1, 0);
            ui.add(new Label(" "), 0, 1);
            ui.add(new Label("Items: "), 0, 2);
            ui.add(inventory, 1, 2);
            borderPane = new BorderPane();
            borderPane.setCenter(canvas);


            scene = new Scene(borderPane);
            refresh();
            GameInformation.primaryStage.setScene(scene);

            scene.setOnKeyPressed(this::onKeyPressed);

            GameInformation.primaryStage.setTitle("Dungeon Crawl");
            GameInformation.primaryStage.show();
            refresh();
        }

        for (Cell[] i : map.getCells()) {
            for (Cell j : i) {
                if (j.getActor() != null && j.getActor().getTileName() == "player" && j.getActor().getHealth() < 1) {
                    loose.setAlertType(Alert.AlertType.INFORMATION);
                    loose.setContentText("You loose!!");
                    loose.showAndWait();
                    System.exit(0);
                }
            }
        }
    }
    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }


}
