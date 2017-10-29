<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	width: 55%;
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
				<h3 align="center">Welcome ${firstName} ${lastName} for ${displayOperation} your account</h3>
				<form class="form-signin" id="${operation}Form" action="do${operation}" method="POST">
					<input type="hidden" name="operation" value="${operation}" />					
					<div id="errors" style="color: #ff0000">${errors}</div>
					<div class="row">
						<div class="col-md-4">
			              <div class="form-group">
			                <label>Account Number</label>
			                <c:out value="${accountNo}"/><input type="hidden" class="form-control border-input" name="accountnumber" value="${accountNo}" />
			              </div>
			            </div>
			            <div class="col-md-4">
			              <div class="form-group">
			                <label>Amount</label>
			                <input type="text" name="Amount" class="form-control border-input" maxlength="30" />
			              </div>
			            </div>
			            <div class="col-md-4">
			              <div class="form-group">
			                <label>Description</label>
			                <input type="text" name="Description" class="form-control border-input" maxlength="45" value="${description}" />
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-12" align="center">
			              <div class="form-group">
			                <button class="btn btn-success button-style" size="20" value="Submit" type="submit">Submit</button>
			                <button class="btn btn-danger"><a href="account">Cancel</a></button>
			              </div>
			            </div>
					</div>
					<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
						value="<c:out value="${_csrf.token}"/>" />
				</form>
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