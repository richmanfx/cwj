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
     * @param dotMassive Массив выборок точки
     * @param dashMassive Массив выборок тире
     * @param pauseMassive Массив выборок паузы
     */
    public static void cwWordPlay(
            String cwWord, Map<Character, String> symbolToCw,
            short[] dotMassive, short[] dashMassive, short[] pauseMassive)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, WavFileException {

        // Создать массив выборок слова
        short[] cwWordMassive = cwWordCreate(cwWord, symbolToCw, dotMassive, dashMassive, pauseMassive);

        // Сохранить слово в WAV-файл
        String tempWavFileName = "cw_word.wav";
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
        int mergedRatio = 3;        // Коэффициент для слитных букв ( <AR>, <SK> и т.п.)
        for (int i = 0; i < cwWord.length(); i++) {

            char symbol = cwWord.charAt(i);

            // Обработка слитных слов
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

                // Добавить паузу между символами. Для последнего символа в слове пауза не нужна.
                if (
                        (
                                (i < cwWord.length() - 1) && (mergedRatio == 3)    // Последний символ без слитности
                        )
                                ||                                                 // или
                        (
                                (i < cwWord.length() - 2) && (mergedRatio == 1)    // Предпоследний символ со слитностью
                        )
                ) {
                    for (int j = 0; j < mergedRatio; j++) {
                        wordMassive = ArrayUtils.addAll(wordMassive, pauseMassive);
                    }
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

        // Массив выборок
        short[] samples = new short[cwMessage.samplesQuantity];

        // Количество выборок на фронт или спад
        int steepnessSamplesQuantity = (int) (cwMessage.steepness * cwMessage.samplesQuantity);

        // Количество выборок на период
        int perCycleSamplesNumber = CwMessage.SAMPLE_RATE / cwMessage.tone;

        // Фронт посылки
        for (int sampleCounter = 0 ; sampleCounter < steepnessSamplesQuantity ; sampleCounter++) {

            double realAmplitude = envelope(cwMessage, sampleCounter);

            samples[sampleCounter] = (short)
                    (realAmplitude  * Math.sin(
                            2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber
                            )
                    );
        }

        // Центральная часть посылки
        for (
                int sampleCounter = steepnessSamplesQuantity;
                sampleCounter < cwMessage.samplesQuantity - steepnessSamplesQuantity;
                sampleCounter++)
        {
            samples[sampleCounter] = (short)
                    (cwMessage.amplitude * Math.sin(
                            2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber
                            )
                    );
        }

        // Спад посылки
        for (
                int sampleCounter = cwMessage.samplesQuantity - steepnessSamplesQuantity;
                sampleCounter < cwMessage.samplesQuantity;
                sampleCounter++) {

            double realAmplitude = envelope(cwMessage, cwMessage.samplesQuantity - sampleCounter);

            samples[sampleCounter] = (short)
                    (realAmplitude  * Math.sin(
                            2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber
                            )
                    );
        }

        return samples;

    }

    /**
     * Огибающая
     * @param sampleCounter
     * @return
     */

    private static double envelope(CwMessage cwMessage, int sampleCounter) {

        // Линейная - трапеция
        double envelope1 = cwMessage.amplitude * sampleCounter / (cwMessage.steepness * cwMessage.samplesQuantity);

        // На слух трапеция гораздо лучше любой сигмообразной огибающей - оставил для будущего :-)
        /*
        // Сигмообразная - колоколовидная
        int sigma = 3;
        int mu = cwMessage.samplesQuantity / 2;
        double n1 = cwMessage.samplesQuantity / 80.0;   // 0...5
        double n2 = (sampleCounter / 1.7 / n1) - 10;     // -2,5...2,5
        double n3 = (n2 / Math.sqrt(1 + Math.pow(n2, 2)));      //  -1...1
//        double n3 = (n2 / (1 + Math.abs(n2)));      //  -1...1
        double n4 = (n3 + 1) / 2;   // 0...1


        double a2 = cwMessage.amplitude * n4;
//                cwMessage.amplitude *
                // (1/(sigma * Math.sqrt(2 * Math.PI)))
//                Math.pow(Math.E,
//                        (-1.0/2.0) *
//                                Math.pow((sampleCounter - (double) mu) / sigma, 2);
//        Math.pow(Math.exp( -3 * Math.pow((double) (mu - sampleCounter) / mu, 2)), 2);

        double a3 = a1 * n4;

        System.out.printf("%f ---> %f ---> %f ---> %f ---> %f\n", a1, n2, n3, n4, a2);

         */

        return envelope1;

    }

}
