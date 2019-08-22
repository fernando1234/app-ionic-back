package com.prototipo.tcc.config;

import com.prototipo.tcc.services.MockEmailService;
import com.prototipo.tcc.services.utils.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

//	@Autowired
//	private DBService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
//		dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}
