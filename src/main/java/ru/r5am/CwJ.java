package ru.r5am;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.application.Application;
import org.apache.logging.log4j.Logger;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;

public class CwJ extends Application {

    protected static final Logger log = LogManager.getLogger();
    protected static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    public static void main(String[] args) {
        log.info("Start!");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        String programTitle = "CWJ";
        String mainFxmlFileName = "cwj.fxml";

        // Иконка
        primaryStage.getIcons().add(new Image("/images/cwj.png"));

        // Установка размеров главной формы
        primaryStage.setMinWidth(config.minimumWindowWidth());
        primaryStage.setMinHeight(config.minimumWindowHeight());
        primaryStage.setMaxWidth(config.maximumWindowWidth());
        primaryStage.setMaxHeight(config.maximumWindowHeight());

        // Загрузка FXML
        InputStream mainFxmlStream = getClass().getResourceAsStream("/fxml/" + mainFxmlFileName);
        Parent root = new FXMLLoader().load(mainFxmlStream);

        primaryStage.setScene(new Scene(root, config.startWindowWidth(), config.startWindowHeight()));
        primaryStage.setTitle(programTitle);
        primaryStage.show();
    }

}

