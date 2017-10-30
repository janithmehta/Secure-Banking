<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OTP verification Page</title>
<style type="text/css">
.error {
	color: red;
	text-align: center;
}
.table-nonfluid {
	width: auto !important;
}
.bank{
	margin-top: 3%;
}
.user{
	color: green;
}
.otp-val{
	width: 25%;
}
.cancel a {
	text-decoration: none;
	color: white;
}
.login-cont {
  height: 100%;
  width: 100%;
  display: flex;
  position: fixed;
  align-items: center;
  justify-content: center;
}

.login-form {
  width: 400px;
  height: 400px;
  margin-left: 10%;
}

.login-form input{
  height: 40px;
  width: 75%;
  margin: 0px 0px 25px 0px;
}

.details {
  text-align: center;
}

.login-form button{
  text-align: center;
  height: 40px;
  width: 100px;
  margin: 0px 25px;
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
			<c:if test="${pageContext.request.userPrincipal.name != null}">
				
			</c:if>
			<c:url value="/j_spring_security_logout" var="logoutUrl" />
			<form action="${logoutUrl}" method="post" id="logoutForm">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>
			<div class="error">${message}</div>
	    		<form:form name="OTPform"
				onsubmit="return isNumberOTP(document.OTPform.password);"
				class="form-signin"
				action="${pageContext.request.contextPath}/otpverification"
				method="post">
				<label for="otp">Enter OTP sent to registered email</label>
				<input type="text" name="password" class="form-control otp-val">
				<button class="btn btn-success" name="validate" size="20" value="Validate" type="submit">Submit</button>
				<button type="button" class="btn btn-danger cancel" onClick="window.location='${pageContext.request.contextPath}/login'">Cancel</button>	
			</form:form>	
			
			<script>
				function isNumberOTP(otp) {
					var numbers = /^[0-9]+$/;
					if (otp.value.match(numbers)) {
						document.OTPform.password.focus();
						return true;
					} else {
						alert('Please enter a valid OTP');
						document.OTPform.password.focus();
						return false;
					}
				}
				function formSubmit() {
					document.getElementById("logoutForm").submit();
				}
			</script>
	    </div>
	   </div>
	 </div>
</body>
</html>