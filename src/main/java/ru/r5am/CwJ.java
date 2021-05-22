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

import java.io.*;

public class CwJ extends Application {

    static final Logger log = LogManager.getLogger();
    static CwjConfig config = ConfigFactory.create(CwjConfig.class);

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

        if (null == iconFileName) {
            System.out.println("WARNING: Config file 'cwj.config' not found.");
            defaultConfigMake();
            iconFileName = config.iconFileName();
        }

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

    /**
     * Сделать конфиг с дефолтными настройками
     */
    private void defaultConfigMake() throws IOException {

        config.setProperty("rusSymbols", "false");
        config.setProperty("engInterface", "false");
        config.setProperty("notRandomWords", "false");

        config.setProperty("startPause", "3");
        config.setProperty("caliberCwSpeed", "3000");
        config.setProperty("wordQuantity", "10");
        config.setProperty("cwSpeed", "100");
        config.setProperty("tone", "777");
        config.setProperty("interval", "3");

        config.setProperty("startWindowWidth", "730");
        config.setProperty("startWindowHeight", "500");
        config.setProperty("minimumWindowWidth", "730");
        config.setProperty("minimumWindowHeight", "500");
        config.setProperty("maximumWindowWidth", "1500");
        config.setProperty("maximumWindowHeight", "1000");
        config.setProperty("iconFileName", "cwj.png");
        config.setProperty("symbolCwFileName", "symbol-cw.dat");

        File configFile = new File("cwj.config"); // TODO: В трёх местах имя файла - ещё в SettingsController.java и в CwjConfig.java
        config.store(
                new FileOutputStream(configFile), "Default configs parameters - automatically created file from CWJ");

    }

}

