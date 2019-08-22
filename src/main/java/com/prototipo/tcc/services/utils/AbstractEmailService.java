package com.prototipo.tcc.services.utils;

import java.util.Date;

import com.prototipo.tcc.domain.Analise;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.prototipo.tcc.domain.Usuario;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Analise obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromAnalise(obj);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromAnalise(Analise obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getUsuario().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}
	
	@Override
	public void sendNewPasswordEmail(Usuario usuario, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(usuario, newPass);
		sendEmail(sm);
	}
	
	protected SimpleMailMessage prepareNewPasswordEmail(Usuario usuario, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(usuario.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);
		return sm;
	}
}
