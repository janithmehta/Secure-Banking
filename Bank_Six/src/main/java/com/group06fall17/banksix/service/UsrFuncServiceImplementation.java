// Author : Abhilash

package com.group06fall17.banksix.service;

import javax.crypto.Cipher;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.PrivateKey;
import com.group06fall17.banksix.model.ExternalUser;
import org.springframework.stereotype.Service;
import java.nio.file.Paths;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.Security;
import java.io.FileOutputStream;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.util.Random;
import java.io.BufferedOutputStream;
import java.security.spec.X509EncodedKeySpec;
import java.io.IOException;
import java.security.PublicKey;
import java.security.KeyFactory;

@Service
public class UsrFuncServiceImplementation implements UsrFuncService {

	@Override
	@Transactional
	public String uploadFileLoc() 
	{
		try {
			Random randGen = new Random();
			int rand = randGen.nextInt();
			if (rand < 0)
				rand *= -1;
			File temp = File.createTempFile(rand + "", ".tmp");
			return temp.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	@Transactional
	public boolean toUploadFile(String location, MultipartFile file) {
		try {
			byte[] bytes = file.getBytes();
			BufferedOutputStream bufstrm = new BufferedOutputStream(new FileOutputStream(new File(location)));
			bufstrm.write(bytes);
			bufstrm.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean diffKeys(ExternalUser user, String privateKeyFileLocation) {
		try {
			// set the Security Provider & context
			Security.addProvider(new BouncyCastleProvider());
			Cipher rsa;
			rsa = Cipher.getInstance("RSA");

			// generate plaintext
			Random rand = new Random();
			String plaintext = rand.nextInt() + "";

			// get private and public keys
			byte[] privateKeyBite = Files.readAllBytes(Paths.get(privateKeyFileLocation));
			byte[] publicKeyBite = user.getPublickey().getBytes(1, (int) user.getPublickey().length());
			PrivateKey pvtKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBite));
			PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBite));
			

			// encrypt with public key
			rsa.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] crypted = rsa.doFinal(plaintext.getBytes());

			// decrypt with private key
			rsa.init(Cipher.DECRYPT_MODE, pvtKey);
			byte[] utf8 = rsa.doFinal(crypted);
			String decrypt = new String(utf8, "UTF8");

			// return result
			return plaintext.equals(decrypt);
		} catch (Exception e) {
			// the decryption is not successful
			return false;
		}
	}
}
