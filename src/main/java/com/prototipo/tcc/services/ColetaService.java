package com.prototipo.tcc.services;

import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.repositories.AnaliseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ColetaService {

    private final AnaliseService analiseService;
    private final TratamentoService tratamentoService;

    public ColetaService(AnaliseService analiseService, TratamentoService tratamentoService) {
        this.analiseService = analiseService;
        this.tratamentoService = tratamentoService;
    }

    public void nova() throws InterruptedException {
        nova(null, Boolean.TRUE);
    }

    public void nova(Analise analiseResultado, Boolean tratar) throws InterruptedException {
        Date dataLeitura = new Date();

        if (!tratar) {
            // Intervalo de 10min
            Thread.sleep(600000);
        }

        BigDecimal ph = coletaPh();
        BigDecimal condutividade = coletaCondutividade();
        BigDecimal turbidez = coletaTurbidez();
        BigDecimal temperatura = coletaTemperatura();

        if (analiseResultado == null && tratar) {
            Analise analise = new Analise();
            analise.setPh(ph);
            analise.setCondutividade(condutividade);
            analise.setTurbidez(turbidez);
            analise.setTemperatura(temperatura);
            analise.setDataLeitura(dataLeitura);

            Analise saved = analiseService.insert(analise);

            tratamentoService.processa(saved);
        }

        if (analiseResultado == null) {
            return;
        }

        analiseResultado.setPhNovo(ph);
        analiseResultado.setTurbidezNovo(turbidez);
        analiseResultado.setCondutividadeNovo(condutividade);
        analiseResultado.setTemperaturaNovo(temperatura);
        analiseResultado.setDataLeituraNovo(dataLeitura);

        analiseService.update(analiseResultado, Boolean.TRUE);
    }

    private BigDecimal coletaTemperatura() {
        return BigDecimal.ONE;
    }

    private BigDecimal coletaTurbidez() {
        return BigDecimal.ONE;
    }

    private BigDecimal coletaCondutividade() {
        return BigDecimal.ONE;
    }

    private BigDecimal coletaPh() {
        return BigDecimal.ONE;
    }
}
