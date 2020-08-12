package ru.r5am;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import org.apache.logging.log4j.Logger;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;

public class CwJ extends Application {

    protected static final Logger log = LogManager.getLogger();
    protected static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    public static void main(String[] args) {
        log.info("Start!");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        String programTitle = "CWJ";

        // Установка размеров главной формы
        primaryStage.setMinWidth(config.minimumWindowWidth());
        primaryStage.setMinHeight(config.minimumWindowHeight());
        primaryStage.setMaxWidth(config.maximumWindowWidth());
        primaryStage.setMaxHeight(config.maximumWindowHeight());

        Group group = new Group();
        Scene scene = new Scene(group, config.startWindowWidth(), config.startWindowHeight());

        primaryStage.setTitle(programTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

