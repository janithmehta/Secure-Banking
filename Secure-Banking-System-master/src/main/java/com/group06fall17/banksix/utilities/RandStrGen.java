package com.group06fall17.banksix.utilities;
import java.security.SecureRandom;

/*
 * 
 @Author = Shubham 
 * 
 */

public final class RandStrGen {
	static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+*#@!^%";
	static final String numeric="0123456789";
	static SecureRandom rnd = new SecureRandom();

	public String randomString(int len) {
		StringBuilder randString = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			randString.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		return randString.toString();
	}

	public String randomCVV() {
		StringBuilder randCVV = new StringBuilder();
		for (int i = 0; i < 3; i++)
			randCVV.append(numeric.charAt(rnd.nextInt(numeric.length())));
		return randCVV.toString();
	}

	public String randomCard() {
		StringBuilder randCard = new StringBuilder();
		randCard.append("666");
		for (int i = 0; i < 13; i++)
			randCard.append(numeric.charAt(rnd.nextInt(numeric.length())));
		return randCard.toString();
	}
	
}
