package ru.r5am.utils;

import ru.r5am.CwjConfig;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SoundPlay {

    /**
     * Воспроизвести слово
     * @param cwWord CW слово
     * @param config Конфигурационные параметры
     * @param symbolToCw Соответствие символа CW посылкам
     */
    public static void cwWordPlay(String cwWord, CwjConfig config, Map<Character, String> symbolToCw)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {

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

        String wavFile = "example.wav";
        String resourcePath = "src/main/resources/";
        Path wavFileFullName = Paths.get(resourcePath + wavFile);
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
