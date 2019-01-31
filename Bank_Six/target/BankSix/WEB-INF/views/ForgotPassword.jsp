<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank SIX - Forgot Password</title>
<style type="text/css">
.error {
	color: red;
	text-align: center;
}

.table-nonfluid {
	width: auto !important;
}
</style>
<script src='https://www.google.com/recaptcha/api.js'></script>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
<style type="text/css">
.login-cont {
  height: 100%;
  width: 100%;
  display: flex;
}
.login-cont .row {
	width: 100%;
	margin-top: 5%;
}
.login-form {
	width: 40%;
	margin-left: 30%;
}
.login-form button{
  text-align: center;
  height: 40px;
  width: 100px;  
  margin: 20px 10px 0px 0px;
}
.success{
	color: green;
}
.login-form a{
	color: black;
	text-decoration: none;
	cursor: pointer;
}
.bank{
	margin-top: 3%;
}
</style>
</head>
<body>
	<div class="container login-cont">
	  <div class="row">
	    <div class="col-xs-12 login-form">
	    	<h2 align="center" class="bank">
				Bank SIX
			</h2>
			<hr>
			<h3 align="center">Forgot Password</h3>
			<div class="error">${message}</div>
			<form:form class="form-signin" action="forgotPassword" method="post">
				<div class="row">
					<div class="col-md-12">
		              <div class="form-group">
		                <label>Enter your registered Email</label>
		                <input type="email" class="form-control" name="email"
							maxlength="30" />
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-12" align="center">
		              <div class="g-recaptcha"
						data-sitekey="6Lf6kw8TAAAAAMosmegdJlwFmUbqoi41K9IBdXVt"></div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-12" align="center">
		              <div class="form-group">
		                <button class="btn btn-success button-style" size="20" value="Submit" type="submit">Submit</button>
						<button class="btn btn-danger button-style cancel" value="Reset" type="reset">Reset</button>
						<button class="btn btn-default"><a href="login.html">Cancel</a></button>
		              </div>
		            </div>
				</div>
			</form:form>
	    </div>
	  </div>
	</div>
</body>
</html>
