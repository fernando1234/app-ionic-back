package com.prototipo.tcc.services;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.enums.PinagemGpio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TratamentoService {

    private static int count = 0;
    private static int start_counter = 0;

    private final AnaliseService analiseService;
    private final ColetaService coletaService;

    public TratamentoService(AnaliseService analiseService, ColetaService coletaService) {
        this.analiseService = analiseService;
        this.coletaService = coletaService;
    }

    public void processa(Analise analise) throws InterruptedException {
        BigDecimal temperatura = analise.getTemperatura();

        BigDecimal mlPhPositivo = processaPhPositivo(analise.getPh());
        BigDecimal mlPhNegativo = processaPhNegativo(analise.getPh());
        BigDecimal mlDecantador = processaTurbidez(analise.getTurbidez());
        BigDecimal mlCloro = processaCondutividade(analise.getCondutividade());

        analise.setDataTratamento(new Date());
        analise.setPhP(mlPhPositivo);
        analise.setPhN(mlPhNegativo);
        analise.setDecantador(mlDecantador);
        analise.setCloro(mlCloro);

        analiseService.update(analise);

        coletaService.nova(analise, Boolean.FALSE);
    }

    private BigDecimal processaTurbidez(BigDecimal turbidez) throws InterruptedException {
        //TODO calcular qtde de ml's
        BigDecimal mlDecantador = BigDecimal.TEN;

        if (mlDecantador.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.DECANTADOR, mlDecantador);
        }

        return mlDecantador;
    }

    private BigDecimal processaCondutividade(BigDecimal condutividade) throws InterruptedException {
        //TODO calcular qtde de ml's
        BigDecimal mlCloro = BigDecimal.TEN;

        if (mlCloro.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.CLORO, mlCloro);
        }

        return mlCloro;
    }

    private BigDecimal processaPhPositivo(BigDecimal ph) throws InterruptedException {
        //TODO calcular qtde de ml's
        BigDecimal mlPhPositivo = BigDecimal.TEN;

        if (mlPhPositivo.compareTo(BigDecimal.ZERO) >= 0) {
            iniciaTratamento(PinagemGpio.PH_MAIS, mlPhPositivo);
        }

        return mlPhPositivo;
    }

    private BigDecimal processaPhNegativo(BigDecimal ph) throws InterruptedException {
        //TODO calcular qtde de ml's
        BigDecimal mlPhNegativo = BigDecimal.TEN;

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

        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput releSolenoide = gpio.provisionDigitalOutputPin(solenoide.getGpio(), solenoide.getDescricao(), PinState.LOW);
        GpioPinDigitalOutput minibomba = gpio.provisionDigitalOutputPin(releMinibomba.getGpio(), releMinibomba.getDescricao(), PinState.LOW);
        GpioPinDigitalInput sensorFluxo = gpio.provisionDigitalInputPin(sensorFluxoGpio.getGpio(), sensorFluxoGpio.getDescricao(), PinPullResistance.PULL_DOWN);
        sensorFluxo.setShutdownOptions(true);

        //Ativa solenoide e bomba
        releSolenoide.high();
        minibomba.high();

        // Listener
        sensorFluxo.addListener((GpioPinListenerDigital) event -> setPulso());

        while (ml.compareTo(leituraFluxo) >= 0) {
            start_counter = 1;
            Thread.sleep(1);
            start_counter = 0;

            // waterFlow += 1.0 / 5880.0;
            //BigDecimal.valueOf((count / 5880.0));

            leituraFluxo = BigDecimal.valueOf((count * 60 * 2.25 / 1000));
            System.out.printf("Litros por minuto:  %.2f %n", leituraFluxo);
            Thread.sleep(5);
        }

        //Desliga solenoide e bomba
        releSolenoide.low();
        minibomba.low();

        gpio.shutdown();
    }

    private static void setPulso() {
        if (start_counter == 1) {
            count++;
        }
    }

}
