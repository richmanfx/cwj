package ru.r5am.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;

import java.io.IOException;
import java.io.InputStream;

public class Settings {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    /**
     * Редактировать настройки
     * @param actionEvent Событие
     */
    public static void edit(ActionEvent actionEvent) throws IOException {

        // В пикселях
        int minimumWindowHeight = config.minimumWindowHeight();
        int startWindowWidth = config.startWindowWidth();

        Parent root;
        Stage stage = new Stage();
        String fxmlSettingsForm = "/fxml/settings.fxml";
        InputStream inputStream = About.class.getResourceAsStream(fxmlSettingsForm);

        if (null == inputStream) {
            System.err.printf("Couldn't find file: '%s'", fxmlSettingsForm);
        } else {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(inputStream);
            stage.setScene(new Scene(root));
        }

        stage.setMinHeight(minimumWindowHeight);
        stage.setMinWidth(startWindowWidth);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        // Иконка окна
        String iconFileName = config.iconFileName();
        stage.getIcons().add(new Image("/images/" + iconFileName));

        // Заголовок
        stage.setTitle("Настройки");

        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());   // Родительское окно по событию
        stage.showAndWait();    // Ожидать закрытия настроек

    }

}
