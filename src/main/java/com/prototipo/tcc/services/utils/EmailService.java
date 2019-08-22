package com.prototipo.tcc.services.utils;

import com.prototipo.tcc.domain.Analise;
import org.springframework.mail.SimpleMailMessage;

import com.prototipo.tcc.domain.Usuario;

public interface EmailService {

	void sendOrderConfirmationEmail(Analise obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Usuario usuario, String newPass);
}
