package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;
import ru.r5am.utils.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsController {

    static CwjConfig config = ConfigFactory.create(CwjConfig.class);

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
                    if (godValues) {
                        // Сохранить параметры в конфиг-файл
                        configParametersSave();

                        // Закрыть форму настроек
                        MainController.currentWindowClose(actionEvent);      // Закрыть текущее окно

                    }
                    break;

                case "cancelButton":    // Выйти без сохранения параметров

                    MainController.currentWindowClose(actionEvent);      // Закрыть текущее окно
                    break;
            }
        }
    }

    /**
     * Сохранить параметры из формы настроек в конфигурационном файле
     */
    private void configParametersSave() throws IOException {

        config.setProperty("rusSymbols", String.valueOf(rusSymbolsCheckBox.isSelected()));
        config.setProperty("engInterface", String.valueOf(engInterfaceCheckBox.isSelected()));
        config.setProperty("notRandomWords", String.valueOf(notRandomWordsCheckBox.isSelected()));

        config.setProperty("startPause", startPauseTextField.getText());
        config.setProperty("caliberCwSpeed", caliberCwSpeedTextField.getText());
        config.setProperty("wordQuantity", wordQuantityTextField.getText());
        config.setProperty("cwSpeed", cwSpeedTextField.getText());
        config.setProperty("tone", toneTextField.getText());
        config.setProperty("interval", intervalTextField.getText());

        File configFile = new File("cwj.config");  // TODO: В трёх местах имя файла - ещё в CwJ.java и в CwjConfig.java
        config.store(new FileOutputStream(configFile), "Automatically created file from CWJ");

    }

    /**
     * Проверить допустимость введённых значений
     * @return true - значения в допустимых пределах
     */
    private boolean valueValidate() throws IOException {

        boolean check1 = valueValidate(
                Integer.parseInt(startPauseTextField.getText()),
                0,
                60,
                "Пауза на старте может быть в диапазоне от %d до %d секунд.");

        boolean check2 = valueValidate(
                Integer.parseInt(caliberCwSpeedTextField.getText()),
                3000,
                5000,
                "Калибр скорости CW может быть в диапазоне от %d до %d.");

        boolean check3 = valueValidate(
                Integer.parseInt(wordQuantityTextField.getText()),
                1,
                200,
                "Количество слов CW может быть в диапазоне от %d до %d."
        );

        boolean check4 = valueValidate(
                Integer.parseInt(cwSpeedTextField.getText()),
                50,
                200,
                "Скорость CW может быть в диапазоне от %d до %d знаков в минуту."
        );

        boolean check5 = valueValidate(
                Integer.parseInt(toneTextField.getText()),
                400,
                2000,
                "Высота тона может быть в диапазоне от %d до %d Гц."
        );

        boolean check6 = valueValidate(
                Integer.parseInt(intervalTextField.getText()),
                3,
                20,
                "Интервал может быть в диапазоне от %d до %d длительностей тире."
        );

        return check1 && check2 && check3 && check4 && check5 && check6;
    }

    /**
     * Проверить значение параметра в поле формы
     * @param actualValue Реальное значение из поля формы
     * @param minValue Минимально допустимое значение
     * @param maxValue Максимально допустимое значение
     * @param message Сообщение при недопустимом значении
     * @return true - значение в пределах допустимого диапазона
     */
    private boolean valueValidate(int actualValue, int minValue, int maxValue, String message)
            throws IOException {
        boolean result = true;
        if (actualValue < minValue || actualValue > maxValue) {
            Message.show("Информация", String.format(message, minValue, maxValue));
            result = false;
        }
        return result;
    }

}
