<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page session="true"%>
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
.login-form a{
	color: white;
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
				<h4 style="color: green">OTP Validated! Successful Login</h4>
				<br />
				<button class="btn btn-success"><a href="${pageContext.request.contextPath}/customer"><b>Home</b></a></button>
			</div>
		</div>
	</div>
</body>
</html>