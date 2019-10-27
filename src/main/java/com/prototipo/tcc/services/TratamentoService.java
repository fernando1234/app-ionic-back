package com.prototipo.tcc.services;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Configuracao;
import com.prototipo.tcc.domain.enums.PinagemGpio;
import com.prototipo.tcc.repositories.ConfiguracaoRepository;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TratamentoService {

    private static int count = 0;
    private static int start_counter = 0;
    private static GpioController gpio;

    private final ConfiguracaoRepository configuracaoRepository;

    public TratamentoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    public Analise processa(Analise analise) throws InterruptedException {
        gpio = GpioFactory.getInstance();
        Configuracao configuracao = configuracaoRepository.findById(1).orElse(null);

        if (configuracao == null) {
            throw new ValidationException("Você ainda não definiu as configurações da sua piscina no aplicativo.");
        }

        Integer capacidadeM3 = (configuracao.getCapacidadeLitros() / 1000);

        BigDecimal mlPhPositivo = processaPhPositivo(analise.getPh(), capacidadeM3);
        BigDecimal mlPhNegativo = processaPhNegativo(analise.getPh(), capacidadeM3);
        BigDecimal mlCloro = processaCondutividade(analise.getCondutividade(), capacidadeM3);

        //TODO rever tempo
        circularAgua(5000);

        BigDecimal mlDecantador = processaTurbidez(analise.getTurbidez(), capacidadeM3, configuracao.getFatorDecantadorClarificante());

        //TODO rever tempo
        circularAgua(10000);

        analise.setDataTratamento(LocalDateTime.now());
        analise.setPhP(mlPhPositivo);
        analise.setPhN(mlPhNegativo);
        analise.setDecantador(mlDecantador);
        analise.setCloro(mlCloro);

        return analise;
    }

    private BigDecimal processaTurbidez(BigDecimal turbidez, Integer capacidadeM3, BigDecimal fatorDecantadorClarificante) throws InterruptedException {
        BigDecimal mlDecantador = BigDecimal.ZERO;
        BigDecimal capacidadeLitrosDecimal = BigDecimal.valueOf(capacidadeM3);

        if (turbidez.compareTo(BigDecimal.valueOf(5)) > 0 && turbidez.compareTo(BigDecimal.valueOf(10)) < 0) {
            mlDecantador = fatorDecantadorClarificante.multiply(capacidadeLitrosDecimal);
        }

        if (turbidez.compareTo(BigDecimal.valueOf(10)) > 0) {
            mlDecantador = (fatorDecantadorClarificante.multiply(BigDecimal.valueOf(3))).multiply(capacidadeLitrosDecimal);
        }

        if (mlDecantador.compareTo(BigDecimal.ZERO) > 0) {
            iniciaTratamento(PinagemGpio.DECANTADOR, mlDecantador);
        }

        return mlDecantador;
    }

    private BigDecimal processaCondutividade(BigDecimal condutividade, Integer capacidadeM3) throws InterruptedException {
        BigDecimal mlCloro = BigDecimal.ZERO;

        if (condutividade.compareTo(BigDecimal.valueOf(1)) > 0 && condutividade.compareTo(BigDecimal.valueOf(3)) < 0) {
            return mlCloro;
        }

        //TODO 4g ?
        if (condutividade.compareTo(BigDecimal.valueOf(1)) < 0) {
            mlCloro = BigDecimal.valueOf(capacidadeM3 * 4);
        }

        if (mlCloro.compareTo(BigDecimal.ZERO) > 0) {
            iniciaTratamento(PinagemGpio.CLORO, mlCloro);
        }

        return mlCloro;
    }

    private BigDecimal processaPhPositivo(BigDecimal ph, Integer capacidadeM3) throws InterruptedException {
        BigDecimal mlRedutor = BigDecimal.ZERO;

        if (ph.compareTo(BigDecimal.valueOf(7.6)) > 0 && ph.compareTo(BigDecimal.valueOf(8)) < 0) {
            mlRedutor = BigDecimal.valueOf(capacidadeM3 * 10);
        } else if (ph.compareTo(BigDecimal.valueOf(8)) > 0) {
            mlRedutor = BigDecimal.valueOf(capacidadeM3 * 20);
        }

        if (mlRedutor.compareTo(BigDecimal.ZERO) > 0) {
            iniciaTratamento(PinagemGpio.PH_MENOS, mlRedutor);
        }

        return mlRedutor;
    }

    private BigDecimal processaPhNegativo(BigDecimal ph, Integer capacidadeM3) throws InterruptedException {
        BigDecimal mlElevador = BigDecimal.ZERO;

        if (ph.compareTo(BigDecimal.valueOf(6.8)) > 0 && ph.compareTo(BigDecimal.valueOf(7.2)) < 0) {
            mlElevador = BigDecimal.valueOf(capacidadeM3 * 10);
        } else if (ph.compareTo(BigDecimal.valueOf(6.2)) > 0 && ph.compareTo(BigDecimal.valueOf(6.8)) < 0) {
            mlElevador = BigDecimal.valueOf(capacidadeM3 * 15);
        } else if (ph.compareTo(BigDecimal.valueOf(6.2)) < 0) {
            mlElevador = BigDecimal.valueOf(capacidadeM3 * 20);
        }

        if (mlElevador.compareTo(BigDecimal.ZERO) > 0) {
            iniciaTratamento(PinagemGpio.PH_MAIS, mlElevador);
        }

        return mlElevador;
    }

    private void circularAgua(int tempo) throws InterruptedException {
        PinagemGpio releBomba = PinagemGpio.BOMBA_PISCINA;
        GpioPinDigitalOutput relePino = gpio.provisionDigitalOutputPin(releBomba.getGpio(), releBomba.getDescricao(), PinState.HIGH);

        //Ativa bomba
        relePino.low();

        Thread.sleep(tempo);

        //Desliga bomba
        relePino.high();

        gpio.unprovisionPin(relePino);
    }

    private void iniciaTratamento(PinagemGpio solenoide, BigDecimal ml) throws InterruptedException {
        BigDecimal leituraFluxo = BigDecimal.ZERO;
        start_counter = 0;
        count = 0;

        PinagemGpio releMinibomba = PinagemGpio.MINIBOMBA;
        PinagemGpio sensorFluxoGpio = PinagemGpio.SENSOR_FLUXO;

        GpioPinDigitalOutput releSolenoide = gpio.provisionDigitalOutputPin(solenoide.getGpio(), solenoide.getDescricao(), PinState.HIGH);
        GpioPinDigitalOutput minibomba = gpio.provisionDigitalOutputPin(releMinibomba.getGpio(), releMinibomba.getDescricao(), PinState.HIGH);
        GpioPinDigitalInput sensorFluxo = gpio.provisionDigitalInputPin(sensorFluxoGpio.getGpio(), sensorFluxoGpio.getDescricao(), PinPullResistance.PULL_DOWN);
        sensorFluxo.setShutdownOptions(true);

        //Ativa solenoide e bomba
        releSolenoide.low();
        minibomba.low();

        // Listener
        sensorFluxo.addListener((GpioPinListenerDigital) event -> setPulso());

        while (ml.compareTo(leituraFluxo) >= 0) {
            start_counter = 1;
            Thread.sleep(1);
            start_counter = 0;

            leituraFluxo = BigDecimal.valueOf((count * 60 * 11.5 / 1000));
            System.out.printf("Litros por minuto:  %.2f %n", leituraFluxo);
            Thread.sleep(5);
        }

        //Desliga solenoide e bomba
        releSolenoide.high();
        minibomba.high();

        sensorFluxo.removeAllListeners();
        gpio.unprovisionPin(sensorFluxo);
        gpio.unprovisionPin(minibomba);
        gpio.unprovisionPin(releSolenoide);
    }

    private static void setPulso() {
        if (start_counter == 1) {
            count++;
        }
    }

}
