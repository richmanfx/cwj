package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.r5am.utils.FilesWork;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MainController {

    static final Logger log = LogManager.getLogger();
    Map<String, String> symbolToCw;     // Соответствие символов посылкам

    // Хелп
//    OHJHelp cwjHelp = new OHJHelp();

    /**
     * Инициализация
     * Отрабатывает при старте приложения
     */
    @FXML
    private void initialize() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IOException, IllegalAccessException {

        // Прочитать соответствия символов посылкам
        symbolToCw = FilesWork.readSymbolToCw();

        log.info("Отработал initialize()");
    }

    // Привязка переменных к компонентам в cwj.fxml
    @FXML private Button buttonExit;
    @FXML private Button buttonFileSelect;

    /**
     *  Обработка нажатий мышкой на Buttons (клавиатура отдельно обрабатывается!)
     */
    public void buttonProcessing(ActionEvent actionEvent) throws
            InvocationTargetException,
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

                // Кнопка "Выбрать файл слов" на главном окне
            case "buttonFileSelect":
                wordsFileSelect();
                break;

        }

    }

    /**
     * Выбрать файл CW слов
     */
    private void wordsFileSelect() throws IOException {

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
