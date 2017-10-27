// Author : Shubham
package com.group06fall17.banksix.component;

import javax.json.Json;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.BufferedReader;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import static com.group06fall17.banksix.constants.Constants.URL;
import static com.group06fall17.banksix.constants.Constants.SECRET_KEY;
import static com.group06fall17.banksix.constants.Constants.USER_AGENT;

public class RecaptchaCheck {

	public static boolean captchaVerification(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try {
			URL url = new URL(URL);
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			// add request header
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpsURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String postParams = "top_secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;
			// Send post request
			httpsURLConnection.setDoOutput(true);
			DataOutputStream wrstrm = new DataOutputStream(httpsURLConnection.getOutputStream());
			wrstrm.writeBytes(postParams);
			wrstrm.flush();
			wrstrm.close();

			int responseCode = httpsURLConnection.getResponseCode();
			System.out.println("\nSending the 'POST' request to the URL : " + URL);
			System.out.println("The Post parameters are : " + postParams);
			System.out.println("The Response Code is : " + responseCode);

			BufferedReader bfin = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
			String inpLine;
			StringBuffer resp = new StringBuffer();

			while ((inpLine = bfin.readLine()) != null) {
				resp.append(inpLine);
			}
			bfin.close();

			System.out.println(resp.toString());
			
			JsonReader json_read = Json.createReader(new StringReader(resp.toString()));
			JsonObject json_obj = json_read.readObject();
			json_read.close();

			return json_obj.getBoolean("success");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}