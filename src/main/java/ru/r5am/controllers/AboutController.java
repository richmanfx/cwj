package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Objects;

public class AboutController {

    @FXML public Button buttonOkAbout;

    public void buttonProcessing(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();        // Определить источник нажатия

        // Если источник события кнопка, то что-то делать
        if ((source instanceof Button)) {

            Button clickedButton = (Button) source;     // Нисходящее приведение

            if (Objects.equals(clickedButton.getId(), "buttonOkAbout")) {
                MainController.currentWindowClose(actionEvent);      // Закрыть текущее окно
            }

        }

    }

}
