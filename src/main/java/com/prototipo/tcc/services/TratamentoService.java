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
import java.util.Date;

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
            throw new ValidationException("Capacidade de litros da piscina não foi informada, " +
                    "com isso não será possível fazer o tratamento.");
        }

        Integer capacidadeM3 = (configuracao.getCapacidadeLitros() / 1000);

        BigDecimal temperatura = analise.getTemperatura();
        BigDecimal mlPhPositivo = processaPhPositivo(analise.getPh(), capacidadeM3);
        BigDecimal mlPhNegativo = processaPhNegativo(analise.getPh(), capacidadeM3);
        BigDecimal mlDecantador = processaTurbidez(analise.getTurbidez(), capacidadeM3);
        BigDecimal mlCloro = processaCondutividade(analise.getCondutividade(), capacidadeM3);

        analise.setDataTratamento(new Date());
        analise.setPhP(mlPhPositivo);
        analise.setPhN(mlPhNegativo);
        analise.setDecantador(mlDecantador);
        analise.setCloro(mlCloro);

        return analise;
    }

    private BigDecimal processaTurbidez(BigDecimal turbidez, Integer capacidadeLitros) throws InterruptedException {
        //TODO calcular qtde de ml's
        BigDecimal mlDecantador = BigDecimal.ZERO;

        // entre 0 a 5

        if (turbidez.compareTo(BigDecimal.ZERO) > 0 || turbidez.compareTo(BigDecimal.valueOf(5)) < 0) {
            mlDecantador = BigDecimal.valueOf(capacidadeLitros * 4.2);
        }

        if (mlDecantador.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.DECANTADOR, mlDecantador);
        }

        return mlDecantador;
    }

    private BigDecimal processaCondutividade(BigDecimal condutividade, Integer capacidadeLitros) throws InterruptedException {
        BigDecimal mlCloro = BigDecimal.TEN;
        // 1 a 3

        if (condutividade.compareTo(BigDecimal.valueOf(1)) < 0 || condutividade.compareTo(BigDecimal.valueOf(3)) > 0) {
            mlCloro = BigDecimal.valueOf(capacidadeLitros * 4.2);
        }

        if (mlCloro.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.CLORO, mlCloro);
        }

        return mlCloro;
    }

    private BigDecimal processaPhPositivo(BigDecimal ph, Integer capacidadeLitros) throws InterruptedException {
        BigDecimal mlPhPositivo = BigDecimal.ZERO;

        // ex: 7 > 7.6  = false
        if (ph.compareTo(BigDecimal.valueOf(7.6)) > 0) {
            mlPhPositivo = BigDecimal.valueOf(capacidadeLitros * 4.2);
        }

        if (mlPhPositivo.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.PH_MAIS, mlPhPositivo);
        }

        return mlPhPositivo;
    }

    private BigDecimal processaPhNegativo(BigDecimal ph, Integer capacidadeLitros) throws InterruptedException {
        BigDecimal mlPhNegativo = BigDecimal.ZERO;

        // ex: 7 < 7.2  = true
        if (ph.compareTo(BigDecimal.valueOf(7.2)) < 0) {
            mlPhNegativo = BigDecimal.valueOf(capacidadeLitros * 4.2);
        }

        if (mlPhNegativo.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.PH_MENOS, mlPhNegativo);
        }

        return mlPhNegativo;
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

            //TODO waterFlow += 1.0 / 5880.0;
            //TODO BigDecimal.valueOf((count / 5880.0));

            leituraFluxo = BigDecimal.valueOf((count * 60 * 2.25 / 1000));
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
