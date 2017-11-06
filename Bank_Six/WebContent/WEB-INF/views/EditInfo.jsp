<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
.form-back {
	width: 20%;
	display: inline-block;
}
.form-logout {
	width: 20%;
	display: inline-block;
	margin-left: 35%;
}
.desc{
	margin-left: 16%;
	display: inline;
	align: center;
	font-weight: bold;
}
.form-space {
	margin-top: 5%;
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
				<form:form method="get" class="form-back"
					action="${pageContext.request.contextPath}/employee">
					<button class="btn btn-danger button-style" size="20" value="Back" type="submit">Back</button>
				</form:form>				
				<h4 class="desc">Personal Information</h4>
				<div id="errors" style="color: #ff0000">${errors}</div>				
				<form:form
					action="${pageContext.request.contextPath}/employee/editinfo/save"
					name="EditInfoForm" method="post" onsubmit="return isFormValid()"
					class="form-signin form-space">
					<div class="row">
						<div class="col-md-6">
			              <div class="form-group">
			                <label>First Name</label>
			                <input type="text" class="form-control border-input" name="name" placeholder="upto 30
								characters a-z and A-Z" maxlength="30" value="${user.getName()}" />
			              </div>
			            </div>
			            <div class="col-md-6">
			              <div class="form-group">
			                <label>Email</label>
			                <input type="email" class="form-control border-input" name="Email" maxlength="50" value="${user.getEmail().getUsername()}" disabled />
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-4">
			              <div class="form-group">
			                <label>Password</label>
			                <input type="password" class="form-control border-input" name="Pass" maxlength="30" value="" />
			              </div>
			            </div>
			            <div class="col-md-4">
			              <div class="form-group">
			                <label>Confirm Password</label>
			                <input type="password" class="form-control border-input" name="RPass" maxlength="30" value="" />
			              </div>
			            </div>
			            <div class="col-md-4">
			              <div class="form-group">
			                <label>SSN</label>
			                <label class="form-control">${user.getSsn()}</label>
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-12">
			              <div class="form-group">
			                <label>Address</label>
			                <textarea name="address" class="form-control" rows="4"
							cols="15">${user.getAddress()}</textarea>
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-12" align="center">
			              <div class="form-group">
			                <button class="btn btn-success button-style" size="20" value="Update Info" type="submit">Update</button>
			              </div>
			            </div>
					</div>
				</form:form>							
			</div>
		</div>
	</div>
	<script language="javascript">
		function isFormValid() {
			var a = document.forms["EditInfoForm"]["name"].value;
			if (a == null || a == "") {
				alert("Fill out the First Name");
				return false;
			}
			var f = document.forms["EditInfoForm"]["address"].value;
			if (f == null || f == "") {
				alert("Fill out the Address");
				return false;
			}
			var password = document.forms["EditInfoForm"]["Pass"].value;
			if (password == null || password == "") {
				alert("Enter Password");
				document.EditInfoForm.Pass.focus();
				return false;
			}
			var confirmpassword = document.forms["EditInfoForm"]["RPass"].value;
			if (confirmpassword == null || confirmpassword == "") {
				alert("Enter Confirm password");
				document.EditInfoForm.RPass.focus();
				return false;
			}
			if (password != confirmpassword) {
				alert("The password fields don't match");
				document.EditInfoForm.Pass.focus();
				return false;
			}
			var k = document.forms["EditInfoForm"]["SSN"].value;
			if (k == null || k == "") {
				alert("Enter your SSN");
				return false;
			}
			if (isNaN(k) || k.indexOf(" ") != -1) {
				alert("Enter a Valid SSN");
				return false;
			}
			if (k.length > 10) {
				alert("Max length of SSN is 9");
				return false;
			}
		}
	</script>
</body>
</html>
