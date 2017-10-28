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
table.inner {
	border: 0px
}

.table-nonfluid {
	width: auto !important;
}

.blank_row {
	height: 10px;
	background-color: #FFFFFF;
}
</style>
</head>

<body>
	<h3 align="center">REGISTRATION</h3>
	<form:form class="form-signin"
		action="${pageContext.request.contextPath}/validation" name="Registration"
		method="post" onsubmit="return isValid()">		
		<table align="center" class="table table-nonfluid" cellpadding="10"
			width="80%">
			<tr>
				<td colspan="2"><div id="errors" style="color: #ff0000">${errors}</div></td>
			</tr>
			<tr>
				<td>NAME *</td>
				<td><input type="text" class="form-control" name="name"
					maxlength="30" value="${name}" /> (max 30 characters a-z and
					A-Z)</td>
			</tr>
			<tr>
				<td>EMAIL ID *</td>
				<td><input type="email" class="form-control" name="email"
					maxlength="30" value="${email}" /></td>
			</tr>

			<tr>
				<td>Password *</td>
				<td><input type="password" class="form-control" name="password"
					maxlength="30" /></td>
			</tr>

			<tr>
				<td>Re-enter Password *</td>
				<td><input type="password" class="form-control"
					name="confirmpassword" maxlength="30" /></td>
			</tr>

			<tr>
				<td>Account Type *</td>
				<td><input type="radio" name="AccountType" value="individual"
					<c:if test="${accountType=='individual' || accountType==null}">
							checked="checked"
					</c:if>>Individual
					&nbsp;&nbsp;&nbsp; <input type="radio" name="AccountType"
					value="merchant"
					<c:if test="${accountType=='merchant'}">
							checked="checked"
					</c:if>>Merchant
			</tr>

			<tr>
				<td>Organization Name</td>
				<td><input type="text" name="organisationName" class="form-control"
					maxlength="30" value="${organisationName}" /> (Required if Account Type is
					'Merchant')</td>
			</tr>

			<tr>
				<td>ADDRESS *<br /> <br /> <br /></td>
				<td><textarea name="address" class="form-control" rows="4"
						cols="15">${address}</textarea></td>
			</tr>
			<tr>
				<td>SSN *</td>
				<td><input type="text" class="form-control" name="SSN"
					maxlength="30" value="${SSN}" /></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><div class="g-recaptcha"
						data-sitekey="6Lf6kw8TAAAAAMosmegdJlwFmUbqoi41K9IBdXVt"></div></td>
			</tr>
			<tr>
				<td align="center"><input type="submit" class="btn btn-default"
					value="Submit"></td>
				<td><input class="btn btn-default" type="reset" value="Reset"></td>
			</tr>
		</table>

	</form:form>

	<script language="javascript">
		function isValid() {
			var name = document.forms["Registration"]["name"].value;
			if (name == null || name == "") {
				alert("Enter Name");
				document.Registration.name.focus();
				return false;
			}
			var email = document.forms["Registration"]["email"].value;
			if (email == null || email == "") {
				alert("Enter Email");
				return false;
			} else {
				var mailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
				if (email.match(mailRegex)) {
					document.Registration.email.focus();
				} else {
					alert("Invalid email address!");
					document.Registration.email.focus();
					return false;
				}
			}

			var password = document.forms["Registration"]["password"].value;
			if (password == null || password == "") {
				alert("Enter password");
				document.Registration.password.focus();
				return false;
			}
			var confirmpassword = document.forms["Registration"]["confirmpassword"].value;
			if (confirmpassword == null || confirmpassword == "") {
				alert("Re-enter password");
				document.Registration.confirmpassword.focus();
				return false;
			}
			if (password != confirmpassword) {
				alert("The password fields don't match");
				document.Registration.password.focus();
				return false;
			}

			var address = document.forms["Registration"]["address"].value;
			if (address == null || address == "") {
				alert("Enter Address");
				document.Registration.address.focus();
				return false;
			}
			var SSN = document.forms["Registration"]["SSN"].value;
			if (SSN == null || SSN == "") {
				alert("Enter your SSN number");
				document.Registration.SSN.focus();
				return false;

			}

			if (isNaN(SSN) || SSN.indexOf(" ") != -1) {
				alert("SSN should be a numeric value");
				document.Registration.SSN.focus();
				return false;
			}
			if (SSN.length > 10) {
				alert("SSN should be 9 characters ONLY");
				document.Registration.SSN.focus();
				return false;
			}
			return true;
		}
	</script>
</body>
</html>