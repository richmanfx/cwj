package ru.r5am.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CW посылка
 */
@ToString
public class CwMessage {

    public static final int SAMPLE_RATE = 22050;          // Частота дискретизации, Гц - Выборок в секунду

    @Getter @Setter     public double amplitude;        // Максимальная амплитуда выборок
    @Getter @Setter     public int tone;                // Тон сигнала в Гц
    @Getter @Setter     public double steepness;        // Относительная длительность фронта от всей посылки
    @Getter @Setter     public int samplesQuantity;     // Количество выборок всей посылки

    public CwMessage(double amplitude, int tone, double steepness, int samplesQuantity) {
        this.amplitude = amplitude;
        this.tone = tone;
        this.steepness = steepness;
        this.samplesQuantity = samplesQuantity;
    }

}
