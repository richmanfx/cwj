package ru.r5am.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;
import ru.r5am.controllers.MessageController;

import java.io.IOException;
import java.io.InputStream;

public class Message {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    public static void show(String title, String message) throws IOException {

        Parent root;
        Stage stage = new Stage();
        MessageController.messageText = message;
        String fxmlMessageForm = "/fxml/message.fxml";
        InputStream messageFxmlStream = Message.class.getResourceAsStream(fxmlMessageForm);

        if (null != messageFxmlStream) {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(messageFxmlStream);
            stage.setScene(new Scene(root));
        } else {
            System.err.printf("Couldn't find file: '%s'", fxmlMessageForm);
        }

        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);

        // Иконка окна
        String iconFileName = config.iconFileName();
        stage.getIcons().add(new Image("/images/" + iconFileName));

        // Заголовок
        stage.setTitle(title);

        // Родительское окно по событию
        stage.show();

    }

}
