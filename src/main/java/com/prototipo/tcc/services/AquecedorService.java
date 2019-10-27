package com.prototipo.tcc.services;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Configuracao;
import com.prototipo.tcc.domain.enums.PinagemGpio;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.repositories.ConfiguracaoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;

@Service
public class AquecedorService {

    private static GpioController gpio;

    private final ConfiguracaoRepository configuracaoRepository;
    private final AnaliseRepository analiseRepository;

    public AquecedorService(ConfiguracaoRepository configuracaoRepository, AnaliseRepository analiseRepository) {
        this.configuracaoRepository = configuracaoRepository;
        this.analiseRepository = analiseRepository;
    }

    @Async
    public void aquecer() throws InterruptedException {
        Configuracao configuracao = configuracaoRepository.findById(1).orElse(null);

        if (configuracao == null) {
            throw new ValidationException("Você ainda não definiu as configurações da sua piscina no aplicativo.");
        }

        if (!configuracao.isTemAquecedor()) {
            return;
        }

        iniciar(BigDecimal.valueOf(configuracao.getTemperaturaIdeal()));
        BigDecimal temperaturaNova = coletaTemperatura();

        Analise ultimaAnalise = analiseRepository.findTopByOrderByIdDesc();
        ultimaAnalise.setTemperaturaNovo(temperaturaNova);
        analiseRepository.save(ultimaAnalise);
    }

    public BigDecimal coletaTemperatura() {
        W1Master w1Master = new W1Master();

        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            System.out.printf("Temperatura: %3.1f°C", device.getTemperature(TemperatureScale.CELSIUS));
            return BigDecimal.valueOf(device.getTemperature(TemperatureScale.CELSIUS));
        }

        return BigDecimal.ZERO;
    }

    private void iniciar(BigDecimal temperaturaIdeal) throws InterruptedException {
        BigDecimal temperaturaAtual = coletaTemperatura();

        if (temperaturaAtual.compareTo(temperaturaIdeal) >= 0) {
            return;
        }

        PinagemGpio aquecedor = PinagemGpio.AQUECEDOR;
        gpio = GpioFactory.getInstance();

        GpioPinDigitalOutput aquecedorRele = gpio.provisionDigitalOutputPin(aquecedor.getGpio(), aquecedor.getDescricao(), PinState.HIGH);

        //Ativa aquecedor
        aquecedorRele.low();

        while (temperaturaAtual.compareTo(temperaturaIdeal) < 0) {
            Thread.sleep(60000);
            temperaturaAtual = coletaTemperatura();
        }

        //Desliga aquecedor
        aquecedorRele.high();

        gpio.unprovisionPin(aquecedorRele);
    }

}
