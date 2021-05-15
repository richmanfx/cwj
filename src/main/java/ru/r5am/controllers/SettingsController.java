package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;
import ru.r5am.utils.Message;

import java.io.IOException;

public class SettingsController {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    @FXML
    private CheckBox rusSymbolsCheckBox;
    @FXML
    private CheckBox engInterfaceCheckBox;
    @FXML
    private CheckBox notRandomWordsCheckBox;
    @FXML
    private TextField startPauseTextField;
    @FXML
    private TextField caliberCwSpeedTextField;
    @FXML
    private TextField wordQuantityTextField;
    @FXML
    private TextField cwSpeedTextField;
    @FXML
    private TextField toneTextField;
    @FXML
    private TextField intervalTextField;

    @FXML
    private void initialize() {

        // Заполнить поля формы настроек значениями из конфигурационного файла
        rusSymbolsCheckBox.setSelected(config.rusSymbols());
        engInterfaceCheckBox.setSelected(config.engInterface());
        notRandomWordsCheckBox.setSelected(config.notRandomWords());

        startPauseTextField.setText(Integer.toString(config.startPause()));
        caliberCwSpeedTextField.setText(Integer.toString(config.caliberCwSpeed()));
        wordQuantityTextField.setText(Integer.toString(config.wordQuantity()));
        cwSpeedTextField.setText(Integer.toString(config.cwSpeed()));
        toneTextField.setText(Integer.toString(config.tone()));
        intervalTextField.setText(Integer.toString(config.interval()));

    }


    public void buttonProcessing(ActionEvent actionEvent) throws IOException {

        Object source = actionEvent.getSource();        // Определить источник нажатия

        // Если источник события кнопка, то что-то делать
        if ((source instanceof Button)) {

            Button clickedButton = (Button) source;     // Нисходящее приведение

            // По ID определяем конкретную кнопку
            switch (clickedButton.getId()) {

                case "saveButton":  // Сохранить параметры в конфиг-файл

                    // Проверить правильность введённых значений
                    boolean godValues = valueValidate();

                    // Сохранить параметры в конфиг-файл

                    // Закрыть форму настроек

                    // Перечитать параметры из конфиг-файла ???

                    break;

                case "cancelButton":    // Выйти без сохранения параметров

                    MainController.currentWindowClose(actionEvent);      // Закрыть текущее окно
                    break;
            }
        }
    }

    /**
     * Проверить правильность введённых значений
     * @return true - значения в допустимых пределах
     */
    private boolean valueValidate() throws IOException {

        boolean check1 = startPauseValidate(Integer.parseInt(startPauseTextField.getText()));
        boolean check2 = caliberCwSpeedValidate(Integer.parseInt(caliberCwSpeedTextField.getText()));

        return check1 && check2;
    }

    /**
     * Проверить значение калибра скорости
     * @param caliberCwSpeed Значение калибра скорости CW
     * @return true - пауза в пределах допустимого диапазона
     */
    private boolean caliberCwSpeedValidate(int caliberCwSpeed) throws IOException {
        boolean result = true;
        int minCaliber = 3000;  // Относительные "попугаи"
        int maxCaliber = 5000;
        if (caliberCwSpeed < minCaliber || caliberCwSpeed > maxCaliber) {
            Message.show(
                    "Предупреждение",
                    String.format("Калибр скорости CW может быть в диапазоне от %d до %d.", minCaliber, maxCaliber));
            result = false;
        }
        return result;
    }

    /**
     * Проверить значение стартовой паузы
     * @param startPause Пауза перед стартом, секунды
     * @return true - пауза в пределах допустимого диапазона
     */
    private boolean startPauseValidate(int startPause) throws IOException {
        boolean result = true;
        int minPause = 0;       // Секунды
        int maxPause = 60;
        if (startPause < minPause || startPause > maxPause) {
            Message.show(
                    "Предупреждение",
                    String.format("Пауза на старте может быть в диапазоне от %d до %d секунд.", minPause, maxPause));
            result = false;
        }
        return result;
    }

}
