package ru.r5am;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CwJ extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String programTitle = "CwJ";

        Group group = new Group();
        Scene scene = new Scene(group, 600, 500);

        primaryStage.setTitle(programTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

