package ru.r5am;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import ru.r5am.utils.WavFileException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.r5am.utils.SoundPlay.cwMessageCreate;

public class CwMessageCreate {

    protected static final Logger log = LogManager.getLogger();

    @Test
    public static void cwMessageCreatePositive() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, WavFileException {

        // TODO: В логе пока не отображается ничего, ошибок нет...
        log.info("=--> Start method: {}", Thread.currentThread().getStackTrace()[1].getMethodName());

        System.out.printf("=--> Start method: %s", Thread.currentThread().getStackTrace()[1].getMethodName());

        // Подготовить данные
        double amplitude = 1.0; double realAmplitude = amplitude * Short.MAX_VALUE;
        int sampleRate = 44100;
        int samplesNumber = 1000;
        int tone = 700;
        double steepness = 0.15;
        List<Double> samples = new ArrayList<>();

        // Ожидаемый результат
        Double expectedResult = 0.0;

        // Реальный результат
        cwMessageCreate(realAmplitude, sampleRate, samplesNumber, tone, steepness, samples);
        Double actualResult = samples.get(0);

        // Сверка
        assertThat(expectedResult)
                .withFailMessage(
                        String.format("Ожидаемый результат '%s' не соответствует реальному '%s'",
                        expectedResult, actualResult))
                .isEqualTo(actualResult);
    }

}
