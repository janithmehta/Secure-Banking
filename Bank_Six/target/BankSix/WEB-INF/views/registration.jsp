<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src='https://www.google.com/recaptcha/api.js'></script>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<title>Bank SIX</title>
<style type="text/css">
.blank_row {
	height: 10px;
	background-color: #FFFFFF;
}
.error {
	color: red;
	text-align: center;
}
.blank_row {
	height: 10px;
	background-color: #FFFFFF;
}
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
.login-form a{
	color: black;
	text-decoration: none;
	cursor: pointer;
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
			<h3 align="center">REGISTRATION</h3>
			<div class="error">${message}</div>
			<form:form class="form-signin"
				action="${pageContext.request.contextPath}/validation" name="RegisterUser"
				method="post" onsubmit="return registerValid()">
				<div class="row">
					<div class="col-md-6">
		              <div class="form-group">
		                <label>NAME</label>
		                <input type="text" class="form-control border-input" name="name" maxlength="30" value="${name}" />
		              </div>
		            </div>
		            <div class="col-md-6">
		              <div class="form-group">
		                <label>Email</label>
		                <input type="email" class="form-control border-input" name="email" maxlength="50" value="${email}" />
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-6">
		              <div class="form-group">
		                <label>Password</label>
		                <input type="password" class="form-control border-input" name="password" maxlength="30" />
		              </div>
		            </div>
		            <div class="col-md-6">
		              <div class="form-group">
		                <label>Confirm Password</label>
		                <input type="password" class="form-control border-input" name="confirmpassword" maxlength="30" />
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-12">
		              <div class="form-group">
		                <label>Type of Account</label>		                
		                <input style="margin-left: 7%" type="radio" name="AccountType" value="individual"
							<c:if test="${accountType=='individual' || accountType==null}">
									checked="checked"
							</c:if>>Individual
							&nbsp;&nbsp;&nbsp; <input type="radio" name="AccountType"
							value="merchant"
							<c:if test="${accountType=='merchant'}">
									checked="checked"
							</c:if>>Merchant
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-6">
		              <div class="form-group">
		                <label>Organization</label>
		                <input type="text" name="organisationName" class="form-control"
						maxlength="30" value="${organisationName}" /> (Required if Account Type is
						'Merchant')
		              </div>
		            </div>
		            <div class="col-md-6">
		              <div class="form-group">
		                <label>SSN</label>
		                <input type="text" class="form-control" name="SSN"
							maxlength="30" value="${SSN}" />
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-12">
		              <div class="form-group">
		                <label>Address</label>
		                <textarea name="address" class="form-control" rows="4"
						cols="15">${address}</textarea>
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
						<button type="button" class="btn btn-default cancel button-style" onClick="window.location='${pageContext.request.contextPath}/login'">Cancel</button>
		              </div>
		            </div>
				</div>
			</form:form>
	    </div>
	   </div>
	 </div>
	<script language="javascript">
		function registerValid() {
			var name = document.forms["RegisterUser"]["name"].value;
			if (name == null || name == "") {
				alert("Fill out the Name field");
				document.RegisterUser.name.focus();
				return false;
			}
			var email = document.forms["RegisterUser"]["email"].value;
			if (email == null || email == "") {
				alert("Fill out the Email field");
				return false;
			} else {
				var mailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
				if (email.match(mailRegex)) {
					document.RegisterUser.email.focus();
				} else {
					alert("Please Enter Valid Email ID!");
					document.RegisterUser.email.focus();
					return false;
				}
			}
			var password = document.forms["RegisterUser"]["password"].value;
			if (password == null || password == "") {
				alert("Enter Password");
				document.RegisterUser.password.focus();
				return false;
			}
			var confirmpassword = document.forms["RegisterUser"]["confirmpassword"].value;
			if (confirmpassword == null || confirmpassword == "") {
				alert("Enter Confirm password");
				document.RegisterUser.confirmpassword.focus();
				return false;
			}
			if (password != confirmpassword) {
				alert("The password fields don't match");
				document.RegisterUser.password.focus();
				return false;
			}
			var address = document.forms["RegisterUser"]["address"].value;
			if (address == null || address == "") {
				alert("Fill out the Address");
				document.RegisterUser.address.focus();
				return false;
			}
			var SSN = document.forms["RegisterUser"]["SSN"].value;
			if (SSN == null || SSN == "") {
				alert("Enter your SSN");
				document.RegisterUser.SSN.focus();
				return false;
			}
			if (isNaN(SSN) || SSN.indexOf(" ") != -1) {
				alert("Enter a Valid SSN");
				document.RegisterUser.SSN.focus();
				return false;
			}
			if (SSN.length > 10) {
				alert("Max length of SSN is 9");
				document.RegisterUser.SSN.focus();
				return false;
			}
			return true;
		}
	</script>
</body>
</html>
