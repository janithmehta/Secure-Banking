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
}
.login-form {
	width: 75%;
	margin-left: 20%;
}
.login-form input{
  margin: 0px 0px 10px 0px;
  height: 30px;
}
.details {
  text-align: center;
}
.login-form button{
  text-align: center;
  height: 40px;
  width: 100px;  
}
.bank{
	margin-top: 3%;
}
.button-style{	
  margin: 20px 20px 0px 0px !important;
}
.form-logout {
	width: 20%;
	display: inline-block;
	margin-left: 35%;
}
.login-form a{
	color: white;
	text-decoration: none;
	cursor: pointer;
}
.table-nonfluid {
	width: auto !important;
}
.table-nonfluid th, .table-non-fluid td {
	width: 8%;
}
</style>
</head>
<body>
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<div class="container login-cont">
	  <div class="row">
		    <div class="col-xs-12 login-form">
		    	<h2 align="center" class="bank">
					Bank SIX
				</h2>
				<hr>
				<h4 align="center">Account Info</h4>
				<table class="table table-nonfluid table-hover" align="center">
					<tr>
						<th>Account Number</th>
						<th>Account Type</th>
						<th>Bank Statement</th>
						<th>Available Balance</th>
						<th>Account Creation Date</th>
						<th>User ID</th>
						<th>Account Status</th>
					</tr>
					<tr>
						<td>${accountnumber}</td>
						<td>${accountType}</td>
						<td><a href="download" style="color: green;">Download your bank
								statement</a></td>
						<td>${balance}</td>
						<td>${acctcreatedate}</td>
						<td>${usrid}</td>
						<td>${accountstatus}</td>
					</tr>
				</table>
				<button class="btn btn-danger"><a href="account">Back</a></button>
				<form action="${logoutUrl}" method="post" class="form-logout"
						id="logoutForm">
						<button class="btn btn-primary button-style" id="tl" type="submit" name="Logout" value="Log out">Logout</button>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>