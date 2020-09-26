package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainController {

    static final Logger log = LogManager.getLogger();

    // Хелп
//    OHJHelp cwjHelp = new OHJHelp();

    /**
     * Инициализация TODO: Дописать!
     */
    @FXML
    private void initialize() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IOException, IllegalAccessException {

        // Чтение файла соответствия символов посылкам
//        Map<String, String> symbolToCw = FilesWork.readSymbolToCw();
//        log.info("symbolToCw: {}", symbolToCw);

    }

    // Привязка переменных к компонентам в cwj.fxml
    @FXML private Button buttonExit;

    /**
     *  Обработка нажатий мышкой на Buttons (клавиатура отдельно обрабатывается!)
     */
    public void buttonProcessing(ActionEvent actionEvent) throws InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IOException,
            IllegalAccessException {

        Object source = actionEvent.getSource();

        // Если источник события не кнопка, то ничего не делать и выйти
        if (!(source instanceof Button)) return;

        // Нисходящее приведение
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {

            // Кнопка "Выход" на главном окне
            case "buttonExit":
                currentWindowClose(actionEvent);    // Закрыть текущее окно
                break;

        }

    }

    /**
     * Закрыть текущее окно
     */
    public static void currentWindowClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
