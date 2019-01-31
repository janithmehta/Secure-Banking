<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
.login-cont {
  height: 100%;
  width: 100%;
  display: flex;
}
.login-cont .row {
	width: 100%;
}
.login-form {
	width: 65%;
	margin-left: 5%;
}
.login-form button{
  text-align: center;
  height: 40px;
  width: 100px;  
}
.key{
  text-align: center;
  height: 40px;
  width: 150px;  
}
.success{
	color: green;
}
.login a {
	color: white;
	text-decoration: none;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="container login-cont">
	  <div class="row">
	    <div class="col-xs-12 login-form" align="center">
	    	<h2 class="success">Registration Successful!</h2>
	    	<hr>
	    	<h3 class="success">
				Welcome <font color="blue">${firstName}</font> to Bank SIX
			</h3>
			<hr>
			<h4 class="success" style="text-decoration: underline">Please save these details and download private key as they are required for credit card and critical transactions</h4>
            <br />
			<h4>
				Your new Checking Account Number is <font color="blue">${checkingAccountNo}</font>
			</h4>
			<br />
			<h4>
				Your new Savings Account Number is <font color="blue">${savingsAccountNo}</font>
			</h4>
			<br />
			<h4>
				Your new Credit Account Number is <font color="blue">${creditAccountNo}</font>
			</h4>
			<h4>
				Your new Credit Card Number is <font color="blue">${creditCardNo}</font>
			</h4>
			<h4>
				Your CVV Number is <font color="blue">${cvv}</font>
			</h4>
			<br />
			<h4>Login to the Bank SIX using your registered email <font
					color="blue">${email}</font> and your password
			</h4>
			<br />
			<h4>Use Private Key for Critical Transactions with Bank SIX</h4>
			<form action="boaprivatekey.key" class="form-signin" method="POST">
				<input type="hidden" name="PrivateKey" value="${pvtKey}" />
				<p align="center">
					<label>Download Private Key&nbsp;&nbsp;</label><button type="submit" class="btn btn-success key" value="Get Private Key">Download</button>
				</p>
			</form>
			<hr>
			<button class="btn btn-primary login"><a href="login">Login</a></button>
	    </div>
	   </div>
	 </div>	
</body>
</html>