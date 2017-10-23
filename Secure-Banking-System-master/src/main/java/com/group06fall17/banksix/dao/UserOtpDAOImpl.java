package com.group06fall17.banksix.dao;

import java.util.List;
import java.util.Date;
import java.sql.*;

import com.warrenstrange.googleauth.ICredentialRepository;

import com.group06fall17.banksix.model.UserOtp;

public class UserOtpDAOImpl implements UserOtpDAO, ICredentialRepository {

//	static final String DB_URL = "jdbc:mysql://localhost:3306/bankofazdb";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankofazdb";

	static final String USER = "infected_user";
	static final String PASS = "InfectedGroup@06";
	static final int tolerance = 5 * 60 * 1000;

	private UserOtp userotp;

	@Override
	public void add(UserOtp userotp) {
		int validationCode = userotp.getOtpcode();
		String secretkey = userotp.getSecretKey();
		String email = userotp.getEmail();
		long otpvalidity = userotp.getOtpvalidity();

		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Query
			String sql;

			sql = "INSERT INTO userotp (email, secretkey, otpcode, otpvalidity)  VALUES (" + "'" + email + "','"
					+ secretkey + "'," + validationCode + "," + otpvalidity + ") "
					+ "ON DUPLICATE KEY UPDATE otpcode=VALUES(otpcode), otpvalidity= VALUES(otpvalidity)";

			// Create statement
			stmt = conn.createStatement();

			// Execute Statement
			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
	}

	@Override
	public UserOtp get(String email) {
		Connection conn = null;
		Statement stmt = null;
		userotp = new UserOtp();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Creating statement...");

			stmt = conn.createStatement();

			String sql;
			sql = "SELECT * FROM userotp WHERE email ='" + email + "' ";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				userotp.setOtpcode(rs.getInt("otpcode"));
				userotp.setEmail(rs.getString("email"));
				userotp.setSecretKey(rs.getString("secretkey"));
				userotp.setOtpvalidity(rs.getLong("otpvalidity"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		return userotp;
	}

	@Override
	public String getSecretKey(String userName) {
		UserOtp user = new UserOtp();
		user = get(userName);
		return user.getSecretKey();
	}

	@Override
	public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
		UserOtp userOtp = new UserOtp();
		userOtp.setEmail(userName);
		userOtp.setSecretKey(secretKey);
		userOtp.setOtpcode(validationCode);
		userOtp.setOtpvalidity(new Date().getTime() + tolerance);
		add(userOtp);
	}

}