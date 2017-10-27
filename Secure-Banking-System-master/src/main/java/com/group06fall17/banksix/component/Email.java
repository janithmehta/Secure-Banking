/**
 * 
 */
package com.group06fall17.banksix.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import static com.group06fall17.banksix.constants.Constants.SENDERMAIL;


@Component("email")
public class Email {
	@Autowired
	private MailSender mailClient;

	public void sendEmail(String receiverEmail, String emailMessage, String emailSubject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(SENDERMAIL);
		msg.setTo(receiverEmail);
		msg.setSubject(emailSubject);
		msg.setText(emailMessage);
		mailClient.send(msg);
	}
}