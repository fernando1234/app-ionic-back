package com.prototipo.tcc.services;

import com.pi4j.io.i2c.I2CFactory;
import com.prototipo.tcc.domain.Analise;
import com.prototipo.tcc.domain.Configuracao;
import com.prototipo.tcc.domain.enums.PeriodoRepeticao;
import com.prototipo.tcc.repositories.AnaliseRepository;
import com.prototipo.tcc.repositories.ConfiguracaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.prototipo.tcc.domain.enums.PeriodoRepeticao.*;

@Component
public class SchedulingTratamento {

    private final ConfiguracaoRepository repo;
    private final ColetaService coletaService;
    private final AnaliseRepository analiseRepository;

    public SchedulingTratamento(ConfiguracaoRepository repo, ColetaService coletaService, AnaliseRepository analiseRepository) {
        this.repo = repo;
        this.coletaService = coletaService;
        this.analiseRepository = analiseRepository;
    }

    // Executa duas vezes ao dia (9AM - 9PM)
    @Scheduled(cron = "0 0 9,21 * * *")
    public void executar() throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException {
        System.out.println("Executou o Scheduled com cron");

        Configuracao configuracao = repo.findById(1).orElse(null);

        if (configuracao == null) {
            return;
        }

        int horaAtual = LocalTime.now().getHour();
        PeriodoRepeticao periodoRepeticao = configuracao.getPeriodoRepeticao();

        // Uma vez por dia todos os dias (9AM)
        if (UM_AO_DIA.equals(periodoRepeticao) && horaAtual == 9) {
            coletaService.nova();
            return;
        }

        // Duas vezes por dia todos os dias (9AM - 9PM)
        if (DOIS_AO_DIA.equals(periodoRepeticao) && (horaAtual == 9 || horaAtual == 21)) {
            coletaService.nova();
            return;
        }

        // Uma vez por dia a cada dois dias (9AM)
        if (UM_CADA_DOIS_DIAS.equals(periodoRepeticao) && horaAtual == 9) {
            iniciaAnaliseCadaDoisDias();
            return;
        }

        // Duas vezes por dia a cada dois dias (9AM - 9PM)
        if (DOIS_CADA_DOIS_DIAS.equals(periodoRepeticao) && (horaAtual == 9 || horaAtual == 21)) {
            iniciaAnaliseCadaDoisDias();
        }
    }

    private void iniciaAnaliseCadaDoisDias() throws InterruptedException, IOException, I2CFactory.UnsupportedBusNumberException {
        Analise ultimaAnalise = analiseRepository.findTopByOrderByIdDesc();

        int diaAnalise = ultimaAnalise.getDataLeitura().plusDays(2).getDayOfMonth();
        int diaAtual = LocalDate.now().getDayOfMonth();

        if (diaAnalise == diaAtual) {
            coletaService.nova();
        }
    }

}
