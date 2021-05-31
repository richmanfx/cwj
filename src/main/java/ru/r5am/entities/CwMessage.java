package ru.r5am.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CW посылка
 */
@ToString
public class CwMessage {

    public static final int SAMPLE_RATE = 44100;          // Частота дискретизации, Гц - Выборок в секунду

    @Getter @Setter     public double amplitude;
    @Getter @Setter     public int tone;
    @Getter @Setter     public double steepness;
    @Getter @Setter     public int samplesQuantity;     // Количество выборок всей посылки

    public CwMessage(double amplitude, int tone, double steepness, int samplesQuantity) {
        this.amplitude = amplitude;
        this.tone = tone;
        this.steepness = steepness;
        this.samplesQuantity = samplesQuantity;
    }

}
