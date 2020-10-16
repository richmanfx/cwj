package ru.r5am;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class CwJ extends Application {

    static final Logger log = LogManager.getLogger();
    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    public static void main(String[] args) {
        log.info("Start!");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        String programTitle = "CWJ";
        String mainFxmlFileName = "cwj.fxml";

        // Иконка
        String iconFileName = config.iconFileName();
        primaryStage.getIcons().add(new Image("/images/" + iconFileName));

        // Установка размеров главной формы
        primaryStage.setMinWidth(config.minimumWindowWidth());
        primaryStage.setMinHeight(config.minimumWindowHeight());
        primaryStage.setMaxWidth(config.maximumWindowWidth());
        primaryStage.setMaxHeight(config.maximumWindowHeight());

        // Загрузка FXML
        InputStream mainFxmlStream = getClass().getResourceAsStream("/fxml/" + mainFxmlFileName);
        Parent root = new FXMLLoader().load(mainFxmlStream);
        Scene scene = new Scene(root, config.startWindowWidth(), config.startWindowHeight());
        scene.getStylesheets().add(getClass().getResource("/css/cwj.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(programTitle);
        primaryStage.show();
    }

}

