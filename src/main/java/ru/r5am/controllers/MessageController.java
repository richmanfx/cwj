package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Objects;

public class MessageController {

    @FXML private Label label;
    public static String messageText;

    @FXML private void initialize() {
        label.setText(messageText);         // Вывод сообщения
    }

    public void buttonProcessing(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();        // Определить источник нажатия

        // Если источник события кнопка, то что-то делать
        if ((source instanceof Button)) {
            Button clickedButton = (Button) source;     // Нисходящее приведение

            if (Objects.equals(clickedButton.getId(), "okButton")) {
                MainController.currentWindowClose(actionEvent);      // Закрыть текущее окно
            }
        }
    }

}
