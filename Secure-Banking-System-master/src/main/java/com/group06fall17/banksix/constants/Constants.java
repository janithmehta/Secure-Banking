package com.group06fall17.banksix.constants;
 
 public final class Constants {
 
     private Constants() {
             // restrict instantiation
     }
     public static final String SENDERMAIL = "banksix.official.mail@gmail.com";
     public static final double PI = 3.14159;
     public static final double PLANCK_CONSTANT = 6.62606896e-34;
     public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET_KEY = "6Lf6kw8TAAAAAABPTWw2ee7bAbnXuVcvmULTusgl";
    public static final String USER_AGENT = "Mozilla/5.0";
    /*public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\ ] (\\.[_A-Za-z0-9-] )*@"
            +  "[A-Za-z0-9-] (\\.[A-Za-z0-9] )*(\\.[A-Za-z]{2,})$";*/
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/infected_db";
 
    public static final String DB_USER = "infected_user";
    public static final String DB_PASSWORD = "InfectedGroup@06";
    public static final int TIMEOUT = 5 * 60 * 1000;
    public static final String DB_CLASSNAME = "com.mysql.jdbc.Driver";
 
 
    /*private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\ ] (\\.[_A-Za-z0-9-] )*@"
              "[A-Za-z0-9-] (\\.[A-Za-z0-9] )*(\\.[A-Za-z]{2,})$";*/
 
}
