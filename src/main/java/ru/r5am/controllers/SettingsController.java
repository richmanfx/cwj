package ru.r5am.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SettingsController {

    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    @FXML private CheckBox rusSymbolsCheckBox;
    @FXML private CheckBox engInterfaceCheckBox;
    @FXML private CheckBox notRandomWordsCheckBox;
    @FXML private TextField startPauseTextField;
    @FXML private TextField caliberCwSpeedTextField;
    @FXML private TextField wordQuantityTextField;
    @FXML private TextField cwSpeedTextField;
    @FXML private TextField toneTextField;
    @FXML private TextField intervalTextField;

    @FXML private void initialize() {

        // Заполнить поля формы настроек значениями из конфигурационного файла
        rusSymbolsCheckBox.setSelected(config.rusSymbols());
        engInterfaceCheckBox.setSelected(config.engInterface());
        notRandomWordsCheckBox.setSelected(config.notRandomWords());


    }


    public void buttonProcessing(ActionEvent actionEvent)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException,
            InvocationTargetException {

    }


    // Проверка правильности введённых значений
    private String valueValidation() {

        return "";

    }


    private void settingsSave() {

    }

}
