package ru.r5am.utils;

import org.apache.commons.lang3.ArrayUtils;
import ru.r5am.CwjConfig;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.r5am.utils.WavFile.newWavFile;

public class SoundPlay {

    /**
     * Воспроизвести (озвучить) слово
     * @param cwWord CW слово
     * @param config Конфигурационные параметры
     * @param symbolToCw Соответствие символа CW посылкам
     */
    public static void cwWordPlay(String cwWord, CwjConfig config, Map<Character, String> symbolToCw)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException,
            WavFileException {

        int dot = config.caliberCwSpeed() / config.cwSpeed();        // Длительность точки, (минут на знак?)
        int dash = 3 * dot;     // Длительность тире
        int mergedGap = dash;  //  Промежуток между слитными буквами типа <KN>
        List<Double> samples= new ArrayList<>();       // Массив выборок

        // Эти параметры возможно вынести нужно будет выше или в конфиг
        int sampleRate = 44100;          // Частота дискретизации, Гц - Выборок в секунду
        double steepness = 0.15;        // Относительная длительность фронта от длительности всей точечной посылки
        double amplitude = 0.1;       //  Относительная амплитуда от максимальной

        int numChannels = 1;    // МОНО
        int validBits = 8;

        // Рассчитываемые дополнительные параметры

        double dotDuration = dot / 1000F;       // Длительность точки (1000 - в чём???)

        int samplesNumber = (int) (sampleRate * dotDuration);     // Количество выборок посылки


        // Бежать по символам в слове
        for (int i = 0; i < cwWord.length(); i++) {
            char symbol = cwWord.charAt(i);

            // Обработка слитных слов
            switch (symbol) {
                case '<':
                    mergedGap = dot;
                    break;
                case '>':
                    mergedGap = dash;
                    break;
                default:
                    break;
            }
            int mergedRatio = mergedGap / dot;  // Коэффициент для слитных букв ( <AR>, <RN> и т.п.)


            // Создать символ
            if ('<' != symbol && '>' != symbol) {
                symbolPlay(config, symbolToCw.get(symbol), samples, amplitude, sampleRate, samplesNumber, steepness, mergedRatio);
            }

        }

        // Файл для хранения слова
        String resourcePath = "src/main/resources/";
        String wavFile = "temporary.wav";
        Path wavFileFullName = Paths.get(resourcePath + wavFile);

        WavFile writeWavFile =
                newWavFile(new File(wavFileFullName.toString()), numChannels, samplesNumber, validBits, sampleRate);

        // Сколько кадров нужно записать до максимального размера буфера
        long remaining = writeWavFile.getFramesRemaining();
        int toWrite = (remaining > 100000) ? 100000 : (int) remaining;

        Double[] aaa = (Double[]) samples.toArray();    // TODO: В цикле перекидывать
        double[] bbb = ArrayUtils.toPrimitive(aaa);
        writeWavFile.writeFrames(bbb, toWrite);      // Сохранить буфер

        writeWavFile.close();

        // Воспроизвести WAV-файл
        audioFilePlay(wavFileFullName);
    }

    /**
     * Воспроизвести звуковой файл
     * @param audioFile Полное имя звукового файла с путём
     */
    public static void audioFilePlay(Path audioFile)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        File soundFile = new File(audioFile.toString());
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
     * Воспроизвести символ
     * @param config
     * @param cwSymbol символ в CW формате
     * @param samples
     * @param amplitude
     * @param samplesNumber
     * @param steepness
     */
    private static void symbolPlay(
            CwjConfig config, String cwSymbol, List<Double> samples,
            double amplitude, int sampleRate, int samplesNumber, double steepness, int mergedRatio)
            throws IOException, WavFileException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        System.out.println(cwSymbol);

        double realAmplitude = amplitude * Short.MAX_VALUE;     // Реальная амплидуда семпла/выборки

        char[] dotDashMassive = cwSymbol.toCharArray();

        for (char transmitSymbol : dotDashMassive) {

            switch (transmitSymbol) {

                // Создать точку
                case '.':
                    cwMessageCreate(realAmplitude, sampleRate, samplesNumber, config.tone(), steepness, samples);
                    break;

                // Создать тире
                case '-':
                    cwMessageCreate(realAmplitude, sampleRate, 3 * samplesNumber, config.tone(), steepness, samples);
                    break;

            }
        }

        // Создать паузу между посылками
        cwMessageCreate(0, sampleRate, mergedRatio * samplesNumber, config.tone(), steepness, samples);

    }

    /**
     * Создать посылку
     * @param amplitude Амплитуда посылки
     * @param samplesNumber Количество выборок посылки
     * @param tone Тон посылки, Гц
     * @param steepness
     * @param samples
     */
    public static void cwMessageCreate(
            double amplitude, int sampleRate, int samplesNumber, int tone, double steepness, List<Double> samples)
            throws
            IOException, WavFileException, UnsupportedAudioFileException,
            LineUnavailableException, InterruptedException {

        // Буфер на 100 выборок
//        double[] buffer = new double[100];

        // Создать WAV-файл

//        double duration = 1.0;          // Длительность в секундах
//        double duration = (double) samplesNumber / sampleRate;      // Длительность в секундах
//        long numFrames = (long) (duration * sampleRate);      // Количество фреймов для заданной длительности
//        samplesNumber = (int) (duration * sampleRate);          // Количество выборок всей посылки
        int steepnessSamplesNumber = (int) (steepness * samplesNumber);  // Количество выборок на фронт или спад
        int perCycleSamplesNumber = sampleRate / tone;          // Количество выборок на период
        double amplitudeDelta = 1.0 / steepnessSamplesNumber;   // Прирост амплитуды для следующей выборки фронта

//        double[] samples = new double[samplesNumber];       // Массив выборок



        int sampleCounter = 0;      // Локальный счётчик выборок

        while (sampleCounter < samplesNumber) {



            // Заполнить массив выборок
            for (sampleCounter = 0 ; sampleCounter < samplesNumber ; sampleCounter++)
            {
//                buffer[s] = Math.round((amplitude * maxAmplitude) * (Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate)));
//                buffer[s] = Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate);
//                samples[sampleCounter] = (amplitude * Math.sin(2.0 * Math.PI * tone * sampleCounter / sampleRate));
//                samples[sampleCounter] = amplitude * Math.sin(2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber);
                samples.add(amplitude * Math.sin(2.0 * Math.PI * (sampleCounter % perCycleSamplesNumber) / perCycleSamplesNumber));
            }

        }

    }

}
