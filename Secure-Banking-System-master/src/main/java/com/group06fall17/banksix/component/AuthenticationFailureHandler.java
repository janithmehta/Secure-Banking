// Author : Shubham

package com.group06fall17.banksix.component;

import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;

@Component
public class AuthenticationFailureHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	@Autowired
	SessionDetails sessionObj;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		Object usr = event.getAuthentication().getPrincipal();
		System.out.println("######### Inside Authentication ############");
		System.out.println("inside object "+sessionObj.getUsername());
		System.out.println("inside the form : "+usr.toString());
		if (sessionObj.getUsername() == null) {
			sessionObj.setUsername(usr.toString());
			sessionObj.setUserDownAttempts(1);
		} else if (!sessionObj.getUsername().equals(usr.toString())) {
			sessionObj.setUsername(usr.toString());
			sessionObj.setUserDownAttempts(1);
		} else
			sessionObj.setUserDownAttempts(sessionObj.getUserDownAttempts() + 1);
		System.out.println(" Inside the Authentication handler"+" Username  : "+ sessionObj.getUsername()+" Number of failed attempts : " + sessionObj.getUserDownAttempts());
	}
}