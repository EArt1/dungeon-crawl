package com.codecool.dungeoncrawl.logic;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameInformation {
    public static Stage primaryStage;
    public static Scene scene;

    public GameInformation(Stage primaryStage, Scene scene) {
        GameInformation.primaryStage = primaryStage;
        GameInformation.scene = scene;
    }
}
