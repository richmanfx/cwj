package ru.r5am;

import org.aeonbits.owner.ConfigFactory;
import ru.r5am.utils.FilesWork;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Главный класс работы с CW
 */
public class GeneralCwWork {

    static CwjConfig config = ConfigFactory.create(CwjConfig.class);

    /**
     * Стартовать тренировку
     * @param cwWords Список CW слов
     */
    public static void cwStart(List<String> cwWords) throws IOException {

        // Прочитать соответствия символов посылкам
        Map<String, String> symbolToCw = FilesWork.symbolToCwRead();

        // Главный цикл воспроизведения и вывода в форму
        for (int wordCounter = 0; wordCounter < config.wordQuantity(); wordCounter++) {

            // Выбрать случайное слово
            Collections.shuffle(cwWords);
            String randomWord = cwWords.stream().findAny().orElse(null);
//            System.out.println(randomWord);



        }



    }

}
