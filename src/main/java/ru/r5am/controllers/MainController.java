package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oracle.help.library.helpset.HelpSetParseException;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.r5am.CwjConfig;
import ru.r5am.GeneralCwWork;
import ru.r5am.entities.CwWordFile;
import ru.r5am.help.CwjHelp;
import ru.r5am.utils.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    static String resourcePath = "src/main/resources/";
    static final Logger log = LogManager.getLogger();
    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    List<String> cwWords = new ArrayList<>();

    /**
     * Инициализация
     * Отрабатывает при старте приложения
     */
    @FXML
    private void initialize() throws IOException {

        // Обработать слайдеры
        slidersHandle();

        // Использовать файл слов по умолчанию
        String cwWordFileName = config.cwWordFileName();
        Path cwWordFileFullName = Paths.get(resourcePath + cwWordFileName);
        File cwWordFile = new File(cwWordFileFullName.toString());
        cwWords = Files.readAllLines(cwWordFile.toPath());
        cwWords.replaceAll(String::trim);

        // Вывести имя файла на главную форму
        setCwWordsFileNameLabel(cwWordFileName, cwWordsFileNameLabel);

    }

    /**
     * Обработать слайдеры
     */
    private void slidersHandle() {

        // Слайдер скорости передачи CW
        speedSlider.setMin(50);
        speedSlider.setMax(200);
        speedSlider.setValue(100);
        speedLabel.setText(String.format("%.0f", speedSlider.getValue()));
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            speedLabel.setText(String.format("%.0f", speedSlider.getValue()));
//            config.setProperty("cwSpeed", String.format("%.0f", speedSlider.getValue()));
        });
        speedSlider.setSnapToTicks(true);       // Движок притягивется к ближайшей метке
        speedSlider.setMajorTickUnit(50);       // Единичное расстояние между основними метками
        speedSlider.setMinorTickCount(4);       // Тиков между основными метками
        speedSlider.setBlockIncrement(10);      // Шаг перемещения ползунка

        // Слайдер высоты тона
        toneSlider.setMin(300);
        toneSlider.setMax(900);
        toneSlider.setValue(700);
        toneLabel.setText(String.format("%.0f", toneSlider.getValue()));
        toneSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            toneLabel.setText(String.format("%.0f", toneSlider.getValue()));
//            config.setProperty("tone", String.format("%.0f", toneSlider.getValue()));
        });
        toneSlider.setSnapToTicks(true);
        toneSlider.setMajorTickUnit(100);
        toneSlider.setMinorTickCount(1);
        toneSlider.setBlockIncrement(50);

        // Слайдер ...

        // Слайдер ...

    }

    // Привязка переменных к компонентам в cwj.fxml
    @FXML private Button exitButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;
    @FXML private Button aboutButton;
    @FXML private Button fileSelectButton;
    @FXML private Button startButton;

    @FXML private Slider speedSlider;
    @FXML private Label speedLabel;

    @FXML private Slider toneSlider;
    @FXML private Label toneLabel;

    @FXML private VBox mainVBox;
    @FXML private Label cwWordsFileNameLabel;

    @FXML private TextArea textWindow;


    /**
     *  Обработать нажатия мышкой на Buttons (клавиатура отдельно обрабатывается!)
     */
    public void buttonProcessing(ActionEvent actionEvent)
            throws IOException, HelpSetParseException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException, WavFileException {

        Object source = actionEvent.getSource();

        // Если источник события не кнопка, то ничего не делать и выйти
        if (!(source instanceof Button)) return;

        // Нисходящее приведение
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {

            // Кнопка "Выход" на главном окне
            case "exitButton":
                CwjHelp.destroy();
                currentWindowClose(actionEvent);    // Закрыть текущее окно
                break;

            // Кнопка "Выбрать файл слов" на главном окне
            case "fileSelectButton":

                // Прочитать слова из файла
                CwWordFile cwWordsFile = FilesWork.wordsFileRead(mainVBox);
                cwWords = cwWordsFile.cwWords;

                // Вывести имя файла на главную форму
                setCwWordsFileNameLabel(cwWordsFile.Name, cwWordsFileNameLabel);

                break;

            // Кнопка "Настройки"
            case "settingsButton":
                Settings.edit(actionEvent);      // Редактировать настройки
                initialize();               // Перечитать новые настройки после редактирования
                break;

            // Кнопка "Помощь"
            case "helpButton":
                CwjHelp.show();
                break;

            // Кнопка "О программе" на главном окне
            case "aboutButton":
                About.show();
                break;

            // Кнопка "Старт"
            case "startButton":
                if (0 == cwWords.size()) {
                    Message.show("Ошибка", "Файл с CW словами ещё не выбран");
                } else {
                    GeneralCwWork.cwStart(cwWords, textWindow);

                }
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

    /**
     * Вывести имя файла на главную форму
     * @param cwWordsFileName Имя файла CW слов
     * @param cwWordsFileNameLabel Лейбл на форме для имени файла CW слов
     */
    private static void setCwWordsFileNameLabel(String cwWordsFileName, Label cwWordsFileNameLabel) {
        cwWordsFileNameLabel.setText(cwWordsFileName);
    }

}
