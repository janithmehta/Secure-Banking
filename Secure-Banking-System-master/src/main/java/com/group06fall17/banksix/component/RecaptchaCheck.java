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

public class RecaptchaCheck {

	public static final String top_secret = "6Lf6kw8TAAAAAABPTWw2ee7bAbnXuVcvmULTusgl";
	public static final String url_path = "https://www.google.com/recaptcha/api/siteverify";
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean captchaVerification(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try {
			URL obj = new URL(url_path);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String postParams = "top_secret=" + top_secret + "&response=" + gRecaptchaResponse;
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wrstrm = new DataOutputStream(con.getOutputStream());
			wrstrm.writeBytes(postParams);
			wrstrm.flush();
			wrstrm.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending the 'POST' request to the URL : " + url_path);
			System.out.println("The Post parameters are : " + postParams);
			System.out.println("The Response Code is : " + responseCode);

			BufferedReader bfin = new BufferedReader(new InputStreamReader(con.getInputStream()));
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