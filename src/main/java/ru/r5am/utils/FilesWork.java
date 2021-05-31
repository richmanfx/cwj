package ru.r5am.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.CwjConfig;
import ru.r5am.entities.CwMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.r5am.utils.WavFile.newWavFile;

public class FilesWork {

    static String resourcePath = "src/main/resources/";
    static final CwjConfig config = ConfigFactory.create(CwjConfig.class);

    /**
     * Прочитать файл соответствия символов посылкам
     * @return Словарь "Символ": "посылки"
     */
    public static Map<Character, String> symbolToCwRead() throws IOException {

        Map<Character, String> symbolToCw = new HashMap<>();
        String symbolCwFileName = config.symbolCwFileName();
        Path fullPath = Paths.get(resourcePath + symbolCwFileName);
        List<String> symbolCwLines = Files.readAllLines(fullPath);

        for (String oneLine: symbolCwLines) {
            if (oneLine.contains(" ") && oneLine.length() > 2) {    // Не обрабатывать пустые строки
                symbolToCw.put(oneLine.split(" ")[0].charAt(0), oneLine.split(" ")[1]);
            }
        }

        return symbolToCw;
    }

    /**
     * Выбрать файл CW слов, прочитать его
     *
     * @param vBox Вертикальный бокс из главного окна
     * @param cwWordsFileNameLabel Лейбл с именем файла CW слов
     * @return Список CW слов
     */
    public static List<String> wordsFileRead(VBox vBox, Label cwWordsFileNameLabel) throws IOException {

        List<String> cwWords = new ArrayList<>();
        final FileChooser fileChooser = new FileChooser();

        // Фильтры расширений файлов
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        Stage stage = (Stage) vBox.getScene().getWindow();      // Сцена главного окна программы
        File file = fileChooser.showOpenDialog(stage);

        if (null != file) {

            // Читать слова
            cwWords = Files.readAllLines(file.toPath());
            cwWords.replaceAll(String::trim);

            // Вывести имя файла на главную форму
            String cwWordsFileName = file.getName();
            cwWordsFileNameLabel.setText(cwWordsFileName);

        } else {

            // Сообщение об отсутствии файла в окно сообщений
            Message.show("Ошибка", "Выбранный файл не найден");

        }

        return cwWords;
    }

    /**
     * Сохранить массив выборок CW слова в WAV-файл
     * @param cwWordMassive Массив выборок слова
     * @param wavFileName Имя WAV-файла
     */
    public static void cwToWavSave(short[] cwWordMassive, String wavFileName) throws IOException, WavFileException {

        Path wavFileFullPath = Paths.get(resourcePath + wavFileName);
        int numChannels = 1;    // МОНО
        int validBits = 16;

        WavFile writeWavFile = newWavFile(
                new File(wavFileFullPath.toString()),
                numChannels,
                cwWordMassive.length,
                validBits,
                CwMessage.SAMPLE_RATE);

        int[] intCwWordMassive = new int[cwWordMassive.length];
        for (int i = 0; i < cwWordMassive.length; i++) {
            intCwWordMassive[i] = cwWordMassive[i];
        }

        writeWavFile.writeFrames(intCwWordMassive, cwWordMassive.length);      // Сохранить буфер

        writeWavFile.close();

    }

}
