package com.prototipo.tcc.domain.enums;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum PinagemGpio {

    // VER A IMAGEM
    //https://pi4j.com/1.2/images/gpio-listener-example.png

    PH_MAIS(1, RaspiPin.GPIO_02, "pH(+) - GPIO 2 - rele 8"),
    PH_MENOS(2, RaspiPin.GPIO_01, "pH(-) - GPIO 1 - rele 7"),
    CLORO(3, RaspiPin.GPIO_03, "Cloro - GPIO 3 - rele 6"),
    DECANTADOR(4, RaspiPin.GPIO_04, "Decantador - GPIO 4 - rele 5"),
    MINIBOMBA(5, RaspiPin.GPIO_05, "Minibomba - GPIO 5 - rele 3"),
    BOMBA_PISCINA(6, null, "Bomba piscina - GPIO ? - rele 2"),
    SENSOR_FLUXO(7, RaspiPin.GPIO_00, "Sensor de fluxo - GPIO 7"),
    SENSOR_TEMPERATURA(8, RaspiPin.GPIO_07, "Sensor de temperatura - GPIO 0");

    PinagemGpio(int id, Pin gpio, String descricao) {
        this.id = id;
        this.gpio = gpio;
        this.descricao = descricao;
    }

    private int id;
    private Pin gpio;
    private String descricao;

    public int getId() {
        return id;
    }

    public Pin getGpio() {
        return gpio;
    }

    public String getDescricao() {
        return descricao;
    }

    public static PinagemGpio toEnum(Pin gpio) {
        if (gpio == null) {
            return null;
        }

        for (PinagemGpio x : PinagemGpio.values()) {
            if (gpio.equals(x.getGpio())) {
                return x;
            }
        }

        throw new IllegalArgumentException("GPIO inválido: " + gpio);
    }

}
