package ru.r5am;

import javafx.scene.control.TextArea;
import org.aeonbits.owner.ConfigFactory;
import ru.r5am.entities.CwMessage;
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

import static ru.r5am.utils.SoundPlay.cwMessageCreate;

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
        textWindow.clear();             // TODO: Не работает  :-(

        // Рассчитать дополнительные параметры
        int dot = config.caliberCwSpeed() / config.cwSpeed();        // Длительность точки, (минут на знак?)
        double dotDuration = dot / 1000F;       // Длительность точки (1000 - в чём???)
        int samplesQuantity = (int) (CwMessage.SAMPLE_RATE * dotDuration);     // Количество выборок посылки
        double amplitude = 0.1;       //  Относительная амплитуда от максимальной   TODO: Вынести в конфиг???
        short realMaxAmplitude = (short) (amplitude * Short.MAX_VALUE);     // Реальная максимальная амплидуда выборки
        double steepness = 0.15;        // Относительная длительность фронта от длительности всей точечной посылки  TODO: Вынести в конфиг???
        short[] samples = new short[samplesQuantity];      // Массив выборок

        // Создать массив выборок точки
        CwMessage dotMessage = new CwMessage(realMaxAmplitude, config.tone(), steepness, samples.length);
        short[] dotMassive = cwMessageCreate(dotMessage);

        // Создать массив выборок тире
        CwMessage dashMessage = new CwMessage(realMaxAmplitude, config.tone(), steepness, 3 * samples.length);
        short[] dashMassive = cwMessageCreate(dashMessage);

        // Создать массив выборок паузы между посылками - длительность как и у точки
        short[] pauseMassive = new short[samples.length];

        // Главный цикл воспроизведения и вывода в форму
        for (int wordCounter = 0; wordCounter < config.wordQuantity(); wordCounter++) {

            // Выбрать случайное слово
            Collections.shuffle(cwWords);
            String randomWord = Objects.requireNonNull(cwWords.stream().findAny().orElse(null)).toUpperCase();

            // Озвучить слово
            SoundPlay.cwWordPlay(randomWord, symbolToCw, dotMassive, dashMassive, pauseMassive);

            // Вывести слово в форму
            byte columnNumber = 90;
            textWindow.setPrefColumnCount(columnNumber);
            textWindow.appendText(String.format("%-8s", randomWord));     // Через 8 символов, выравнивание влево
            textWindow.appendText(" ");

            // После десятого слова - перевод строки
            if (0 == ((wordCounter + 1) % 10)) {
                textWindow.appendText("\n\r");
            }

            // Пауза между словами


        }

        Thread.sleep(1000);

    }

}
