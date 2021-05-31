package ru.r5am.utils;

import org.apache.commons.lang3.ArrayUtils;
import ru.r5am.entities.CwMessage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static ru.r5am.utils.FilesWork.cwToWavSave;

public class SoundPlay {

    /**
     * Воспроизвести (озвучить) слово
     * @param cwWord CW слово
     * @param symbolToCw Соответствие символа CW посылкам
     * @param dotMassive
     * @param dashMassive
     * @param pauseMassive
     */
    public static void cwWordPlay(
            String cwWord, Map<Character, String> symbolToCw,
            short[] dotMassive, short[] dashMassive, short[] pauseMassive)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, WavFileException {

        // Создать массив выборок слова
        short[] cwWordMassive = cwWordCreate(cwWord, symbolToCw, dotMassive, dashMassive, pauseMassive);

        // Сохранить слово в WAV-файл
        String tempWavFileName = "temporary.wav";
        cwToWavSave(cwWordMassive, tempWavFileName);

        // Воспроизвести WAV-файл
        audioFilePlay(tempWavFileName);
    }

    /**
     * Создать массив выборок для всего слова
     * @return Массив выборок слова
     */
    private static short[] cwWordCreate(
            String cwWord, Map<Character, String> symbolToCw,
            short[] dotMassive, short[] dashMassive, short[] pauseMassive) {

        short[] wordMassive = new short[0];

        // Бежать по символам в слове
        for (int i = 0; i < cwWord.length(); i++) {

            char symbol = cwWord.charAt(i);

            // Обработка слитных слов
            int mergedRatio = 3;        // Коэффициент для слитных букв ( <AR>, <SK> и т.п.)
            switch (symbol) {
                case '<':       // Начало "слитности"
                    mergedRatio = 1;
                    break;
                case '>':       // Окончание "слитности"
                    mergedRatio = 3;
                    break;
                default:
                    break;
            }

            // Создать символ
            if ('<' != symbol && '>' != symbol) {
                short[] symbolMassive = symbolCreate(symbolToCw.get(symbol), dotMassive, dashMassive, pauseMassive);
                wordMassive = ArrayUtils.addAll(wordMassive, symbolMassive);    // Добавить символ в слово
            }

            // Добавить паузу между символами. Для последнего символа в слове пауза не нужна.
            if (i < cwWord.length() - 1) {
                for (int j = 0; j < mergedRatio; j++) {
                    wordMassive = ArrayUtils.addAll(wordMassive, pauseMassive);
                }
            }

        }

        return wordMassive;
    }

    /**
     * Воспроизвести звуковой файл
     * @param audioFileName Полное имя звукового файла с путём
     */
    public static void audioFilePlay(String audioFileName)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        String resourcePath = "src/main/resources/";
        Path wavAudioFileName = Paths.get(resourcePath + audioFileName);
        File soundFile = new File(wavAudioFileName.toString());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setFramePosition(0);       // Указатель в начало - на первый фрейм
        clip.start();

        // Ждать окончания воспроизведения
        Thread.sleep(clip.getMicrosecondLength()/1000);     // 1000 - из мкс в мс
        clip.stop();
        clip.close();

    }

    /**
     * Создать символ
     * @param cwSymbol символ в CW формате
     */
    private static short[] symbolCreate(
            String cwSymbol, short[] dotMassive, short[] dashMassive, short[] pauseMassive) {

        System.out.println(cwSymbol);

        char[] dotDashMassive = cwSymbol.toCharArray();     // Последовательность точек и тире символа
        short[] symbolMassive = new short[0];

        // Бежать по точкам и тире символа
        for (int i = 0; i < dotDashMassive.length; i++) {

            switch (dotDashMassive[i]) {

                // Создать точку
                case '.':
                    symbolMassive = ArrayUtils.addAll(symbolMassive, dotMassive);
                    break;

                // Создать тире
                case '-':
                    symbolMassive = ArrayUtils.addAll(symbolMassive, dashMassive);
                    break;

            }

            // Пауза между посылками. Для последней посылки пауза не нужна.
            if (i < dotDashMassive.length -1) {
                symbolMassive = ArrayUtils.addAll(symbolMassive, pauseMassive);
            }

        }

        return symbolMassive;

    }

    /**
     * Создать посылку
     * @param cwMessage Амплитуда посылки
     * @return Массив выборок посылки
     */
    public static short[] cwMessageCreate(CwMessage cwMessage) {

        short[] samples = new short[cwMessage.samplesQuantity];      // Массив выборок

        int steepnessSamplesQuantity = (int) (cwMessage.steepness * cwMessage.samplesQuantity);  // Количество выборок на фронт или спад
        int perCycleSamplesNumber = CwMessage.SAMPLE_RATE / cwMessage.tone;          // Количество выборок на период
        double amplitudeDelta = 1.0 / steepnessSamplesQuantity;   // TODO: под вопросом значение!!! Прирост амплитуды для следующей выборки фронта

        // Заполнить массив выборок
        for (int sampleCounter = 0 ; sampleCounter < cwMessage.samplesQuantity ; sampleCounter++)
        {
//                buffer[s] = Math.round((amplitude * maxAmplitude) * (Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate)));
//                buffer[s] = Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate);
//                samples[sampleCounter] = (amplitude * Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate));
//                samples.add(amplitude * Math.sin(2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber));

            samples[sampleCounter] = (short)
                    (cwMessage.amplitude * Math.sin(
                            2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber
                            )
                    );

        }

        return samples;
    }

}
