/**
 * @author Sirish
 */
package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;
import com.group06fall17.banksix.model.UserOTP;
import java.sql.*;
import com.warrenstrange.googleauth.ICredentialRepository;
import static com.group06fall17.banksix.constants.Constants.DB_URL;
import static com.group06fall17.banksix.constants.Constants.DB_USER;
import static com.group06fall17.banksix.constants.Constants.DB_PASSWORD;
import static com.group06fall17.banksix.constants.Constants.TIMEOUT;
import static com.group06fall17.banksix.constants.Constants.DB_CLASSNAME;


public class UserOTPDAOImplementation implements UserOTPDAO, ICredentialRepository {

//	static final String url_database = "jdbc:mysql://localhost:3306/infected_db";
	/*static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/infected_db";

	static final String DB_USER = "infected_user";
	static final String DB_PASSWORD = "InfectedGroup@06";
	static final int TIMEOUT = 5 * 60 * 1000;
*/	private UserOTP userotp;

	@Override
	public void add(UserOTP userotp) {
		int valkey = userotp.getCode();
		String secretkey = userotp.getSecretKey();
		String email = userotp.getEmail();
		long otpvalidity = userotp.getValidity();

		Statement statement = null;
		Connection connection = null;
		
		try {

//			Class.forName("com.mysql.jdbc.Driver");
			Class.forName(DB_CLASSNAME);

			System.out.println("Connecting to database......");
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			String query;

			query = "INSERT INTO userotp (email, secretkey, otpcode, otpvalidity)  VALUES (" + "'" + email + "','"
					+ secretkey + "'," + valkey + "," + otpvalidity + ") "
					+ "ON DUPLICATE KEY UPDATE otpcode=VALUES(otpcode), otpvalidity= VALUES(otpvalidity)";

			// Create statement
			statement = connection.createStatement();

			// Execute Statement
			statement.executeUpdate(query);

			statement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException sqlException) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			} // end finally try
		} // end try
	}

	@Override
	public UserOTP get(String email) {
		Statement statement = null;
		Connection connection = null;
		userotp = new UserOTP();
		try {
			Class.forName(DB_CLASSNAME);
			System.out.println("Connecting to database......");
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Creating statement......");

			statement = connection.createStatement();

			String sql_stmt;
			sql_stmt = "SELECT * FROM userotp WHERE email ='" + email + "' ";
			ResultSet resultSet = statement.executeQuery(sql_stmt);

			while (resultSet.next()) {
				userotp.setCode(resultSet.getInt("otpcode"));
				userotp.setEmail(resultSet.getString("email"));
				userotp.setSecretKey(resultSet.getString("secretkey"));
				userotp.setValidity(resultSet.getLong("otpvalidity"));
			}

			resultSet.close();
			statement.close();
			connection.close();

		} catch (SQLException sqlException) {
			// Handle errors for JDBC
			sqlException.printStackTrace();
		} catch (Exception ex) {
			// Handle errors for Class.forName
			ex.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException var_se) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
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
		usr_otp.setValidity(new Date().getTime() + TIMEOUT);
		add(usr_otp);
	}

}