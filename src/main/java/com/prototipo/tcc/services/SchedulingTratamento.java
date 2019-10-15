package com.prototipo.tcc.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingTratamento {

    // Executa duas vezes ao dia (9AM - 9PM)
    @Scheduled(cron = "0 0/1 * * * *")
    public void executar() {
        System.out.println("Executou o Scheduled com cron");
    }

    // Uma vez por dia todos os dias (9AM)
    public void executar1() {
    }

    // Duas vezes por dia todos os dias (9AM - 9PM)
    public void executar2() {
    }

    // Uma vez por dia a cada dois dias (9AM)
    public void executar3() {
    }

    // Duas vezes por dia a cada dois dias (9AM - 9PM)
    public void executar4() {
    }

}
