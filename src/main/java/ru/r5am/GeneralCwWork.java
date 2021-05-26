package ru.r5am;

import javafx.scene.control.TextArea;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.utils.FilesWork;
import ru.r5am.utils.SoundPlay;
import ru.r5am.utils.WavFileException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Главный класс работы с CW
 */
public class GeneralCwWork {

    /**
     * Стартовать тренировку
     * @param cwWords Список CW слов
     * @param textWindow Часть главной формы для вывода передаваемого текста
     */
    public static void cwStart(List<String> cwWords, TextArea textWindow)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException, WavFileException {

        // Перечитать конфигурационные данные
        CwjConfig config = ConfigFactory.create(CwjConfig.class);

        // Прочитать соответствия символов посылкам
        Map<Character, String> symbolToCw = FilesWork.symbolToCwRead();

        // Очистить зону вывода текста
        textWindow.clear();

        // Главный цикл воспроизведения и вывода в форму
        for (int wordCounter = 0; wordCounter < config.wordQuantity(); wordCounter++) {

            // Выбрать случайное слово
            Collections.shuffle(cwWords);
            String randomWord = Objects.requireNonNull(cwWords.stream().findAny().orElse(null)).toUpperCase();

            // Озвучить слово
            SoundPlay.cwWordPlay(randomWord, config, symbolToCw);

            // Вывести слово в форму
            textWindow.setPrefColumnCount(90);
            textWindow.appendText(String.format("%-8s", randomWord));     // Через 8 символов, выравнивание влево
            textWindow.appendText(" ");

            // После десятого слова - перевод строки
            if (0 == ((wordCounter + 1) % 10)) {
                textWindow.appendText("\n\r");
            }

            // Пауза между словами


        }



    }

}
