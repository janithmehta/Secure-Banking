/**
 * 
 */
package com.group06fall17.banksix.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("email")
public class Email {
	@Autowired
	private MailSender mailSender;

	public void SendMailToCustomer(String receiverEmail, String emailMessage, String emailSubject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("banksix.official.mail@gmail.com");
		msg.setTo(receiverEmail);
		msg.setSubject(emailSubject);
		msg.setText(emailMessage);
		mailSender.send(msg);
	}
}