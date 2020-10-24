package ru.r5am.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;

import java.io.IOException;
import java.io.InputStream;

public class About {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    public static void show() throws IOException {

        Parent root;
        Stage stage = new Stage();
        String fxmlAboutForm = "/fxml/about.fxml";
        InputStream aboutFxmlStream = About.class.getResourceAsStream(fxmlAboutForm);

        if (null == aboutFxmlStream) {
            System.err.printf("Couldn't find file: '%s'", fxmlAboutForm);
        } else {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(aboutFxmlStream);
            stage.setScene(new Scene(root));
        }

        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);

        // Иконка окна
        String iconFileName = config.iconFileName();
        stage.getIcons().add(new Image("/images/" + iconFileName));

        // Заголовок
        stage.setTitle("О программе");

        stage.show();

    }

}
