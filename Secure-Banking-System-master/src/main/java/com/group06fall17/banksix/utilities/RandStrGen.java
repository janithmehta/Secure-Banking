package com.group06fall17.banksix.utilities;
import java.security.SecureRandom;

public final class RandStrGen {
	static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+*#@!^%";
	static SecureRandom rnd = new SecureRandom();

	public String randomString(int len) {
		StringBuilder randString = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			randString.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		return randString.toString();
	}

}
