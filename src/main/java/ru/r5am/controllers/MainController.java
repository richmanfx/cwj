package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oracle.help.library.helpset.HelpSetParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.r5am.help.CwjHelp;
import ru.r5am.utils.About;
import ru.r5am.utils.FilesWork;
import ru.r5am.utils.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainController {

    static final Logger log = LogManager.getLogger();
    Map<String, String> symbolToCw;                     // Соответствие символов посылкам
    List<String> cwWords = new ArrayList<>();

    /**
     * Инициализация
     * Отрабатывает при старте приложения
     */
    @FXML
    private void initialize() throws IOException {

        // Прочитать соответствия символов посылкам
        symbolToCw = FilesWork.readSymbolToCw();

        log.info("Отработал initialize()");
    }

    // Привязка переменных к компонентам в cwj.fxml
    @FXML private Button buttonExit;
    @FXML private Button buttonSettings;
    @FXML private Button buttonHelp;
    @FXML private Button buttonAbout;
    @FXML private Button buttonFileSelect;
    @FXML private VBox mainVBox;
    @FXML private Label cwWordsFileNameLabel;


    /**
     *  Обработка нажатий мышкой на Buttons (клавиатура отдельно обрабатывается!)
     */
    public void buttonProcessing(ActionEvent actionEvent) throws IOException, HelpSetParseException {

        Object source = actionEvent.getSource();

        // Если источник события не кнопка, то ничего не делать и выйти
        if (!(source instanceof Button)) return;

        // Нисходящее приведение
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {

            // Кнопка "Выход" на главном окне
            case "buttonExit":
                CwjHelp.destroy();
                currentWindowClose(actionEvent);    // Закрыть текущее окно
                break;

            // Кнопка "Выбрать файл слов" на главном окне
            case "buttonFileSelect":
                cwWords = FilesWork.wordsFileRead(mainVBox, cwWordsFileNameLabel);
                break;

            // Кнопка "Настройки"
            case "buttonSettings":
                System.out.println("Button 'buttonSettings' clicked");
                Settings.edit(actionEvent);      // Редактировать настройки
//                initialize();               // Перечитать новые настройки после редактирования
                break;

            // Кнопка "Помощь"
            case "buttonHelp":
                CwjHelp.show();
                break;

            // Кнопка "О программе" на главном окне
            case "buttonAbout":
                About.show();
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
