package ru.r5am.utils;

import ru.r5am.CwjConfig;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static ru.r5am.utils.WavFile.newWavFile;

public class SoundPlay {

    /**
     * Воспроизвести слово
     * @param cwWord CW слово
     * @param config Конфигурационные параметры
     * @param symbolToCw Соответствие символа CW посылкам
     */
    public static void cwWordPlay(String cwWord, CwjConfig config, Map<Character, String> symbolToCw)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, WavFileException {

        int dot = config.caliberCwSpeed() / config.cwSpeed();        // Длительность точки, (в чём?)
        int dash = 3 * dot;     // Длительность тире
        int mergedGap = dash;  //  Промежуток между слитными буквами типа <KN>

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

            // Воспроизвести символ
            if ('<' != symbol && '>' != symbol) {
                symbolPlay(symbolToCw.get(symbol), dot, dash, mergedGap);
            }

        }

        String wavFile = "temporary.wav";
        String resourcePath = "src/main/resources/";
        Path wavFileFullName = Paths.get(resourcePath + wavFile);

        // Создать WAV-файл
        int numChannels = 2;    // TODO: Потом на МОНО переделать
        int validBits = 16;
        long sampleRate = 44100L;       // Выборок в секунду
        double duration = 1.0;      // Длительность в секундах
        long numFrames = (long)(duration * sampleRate);     // Количество фреймов для заданной длительности
        WavFile writeWavFile =
                newWavFile(new File(wavFileFullName.toString()), numChannels, numFrames, validBits, sampleRate);
        double[][] buffer = new double[2][100];     // Буфер на 100 фреймов
        long frameCounter = 0;      // Локальный счётчик фреймов

        while (frameCounter < numFrames) {

            // Сколько кадров нужно записать до максимального размера буфера
            long remaining = writeWavFile.getFramesRemaining();
            int toWrite = (remaining > 100) ? 100 : (int) remaining;

            // Заполнить буфер одним тоном на канал
            for (int s=0 ; s < toWrite ; s++, frameCounter++)
            {
                buffer[0][s] = Math.sin(2.0 * Math.PI * 400 * frameCounter / sampleRate);
                buffer[1][s] = Math.sin(2.0 * Math.PI * 500 * frameCounter / sampleRate);
            }

            writeWavFile.writeFrames(buffer, toWrite);      // Сохранить буфер

        }
        writeWavFile.close();

        // Воспроизвести WAV-файл
//        String wavFile = "example.wav";
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
     * Воспроизвести сивол
     * @param cwSymbol символ в CW формате
     * @param dot
     * @param dash
     * @param mergedGap
     */
    private static void symbolPlay(String cwSymbol, int dot, int dash, int mergedGap) {

        int sampleRate = 44100;          // частота дискретизации, Гц

        System.out.println(cwSymbol);

        char[] dotDashMassive = cwSymbol.toCharArray();

        for (char transmitSymbol : dotDashMassive) {

            switch (transmitSymbol) {

                // Воспроизводим тире
                case '-':

                    break;

                // Воспроизводим точку
                case '.':

                    break;

//                case '_':
//                    break;

            }

        }

    }

}
