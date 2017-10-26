/**
 * @author Sirish
 */
package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;
import com.group06fall17.banksix.model.UserOTP;
import java.sql.*;
import com.warrenstrange.googleauth.ICredentialRepository;

public class UserOTPDAOImplementation implements UserOTPDAO, ICredentialRepository {

//	static final String url_database = "jdbc:mysql://localhost:3306/infected_db";
	static final String url_database = "jdbc:mysql://127.0.0.1:3306/infected_db";

	static final String USER = "infected_user";
	static final String PASS = "InfectedGroup@06";
	static final int tolrnce = 5 * 60 * 1000;
	private UserOTP userotp;

	@Override
	public void add(UserOTP userotp) {
		int valkey = userotp.getCode();
		String secretkey = userotp.getSecretKey();
		String email = userotp.getEmail();
		long otpvalidity = userotp.getValidity();

		Statement var_stmt = null;
		Connection var_conn = null;
		
		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database......");
			var_conn = DriverManager.getConnection(url_database, USER, PASS);

			// Query
			String sql_stmt;

			sql_stmt = "INSERT INTO userotp (email, secretkey, otpcode, otpvalidity)  VALUES (" + "'" + email + "','"
					+ secretkey + "'," + valkey + "," + otpvalidity + ") "
					+ "ON DUPLICATE KEY UPDATE otpcode=VALUES(otpcode), otpvalidity= VALUES(otpvalidity)";

			// Create statement
			var_stmt = var_conn.createStatement();

			// Execute Statement
			var_stmt.executeUpdate(sql_stmt);

			var_stmt.close();
			var_conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (var_stmt != null)
					var_stmt.close();
			} catch (SQLException var_se) {
			} // nothing we can do
			try {
				if (var_conn != null)
					var_conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
	}

	@Override
	public UserOTP get(String email) {
		Statement var_stmt = null;
		Connection var_conn = null;
		userotp = new UserOTP();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database......");
			var_conn = DriverManager.getConnection(url_database, USER, PASS);
			System.out.println("Creating statement......");

			var_stmt = var_conn.createStatement();

			String sql_stmt;
			sql_stmt = "SELECT * FROM userotp WHERE email ='" + email + "' ";
			ResultSet var_rs = var_stmt.executeQuery(sql_stmt);

			while (var_rs.next()) {
				userotp.setCode(var_rs.getInt("otpcode"));
				userotp.setEmail(var_rs.getString("email"));
				userotp.setSecretKey(var_rs.getString("secretkey"));
				userotp.setValidity(var_rs.getLong("otpvalidity"));
			}

			var_rs.close();
			var_stmt.close();
			var_conn.close();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (var_stmt != null)
					var_stmt.close();
			} catch (SQLException var_se) {
			} // nothing we can do
			try {
				if (var_conn != null)
					var_conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		return userotp;
	}

	@Override
	public String getSecretKey(String userName) {
		UserOTP usr = new UserOTP();
		usr = get(userName);
		return usr.getSecretKey();
	}

	@Override
	public void saveUserCredentials(String userName, String secretKey, int valkey, List<Integer> scratchCodes) {
		UserOTP usr_otp = new UserOTP();
		usr_otp.setEmail(userName);
		usr_otp.setSecretKey(secretKey);
		usr_otp.setCode(valkey);
		usr_otp.setValidity(new Date().getTime() + tolrnce);
		add(usr_otp);
	}

}