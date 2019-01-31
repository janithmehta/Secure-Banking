<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
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
<title>Bank SIX | PII</title>
<style type="text/css">
.form-nonfluid {
	width: auto !important;
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
.form-back {
	width: 20%;
	display: inline-block;
	margin-left: 10%;
}
.form-logout {
	width: 20%;
	display: inline-block;
	margin-left: 35%;
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
				<h3 align="center">Transaction Lookup/PII Access</h3>
				<form:form name="form" align="center"
					action="${pageContext.request.contextPath}/employee/pii"
					class="form-inline" onsubmit="return isValid()" method="GET">
					SSN : <input type="text" name="ssn" class="form-control" />&nbsp;
					 <input value="Get PII" type="submit" class="form-control" />
				</form:form>
				<div class="row">
					<div class="col-md-4">
		              <div class="form-group">
		                <label>PII</label>
		                <c:out value="${message}"/>
		              </div>
		            </div>
		            <div class="col-md-4">
		              <div class="form-group">
		                <label>SSN</label>
		                <c:out value="${ssn}" />
		              </div>
		            </div>
		            <div class="col-md-4">
		              <div class="form-group">
		                <label>Visa Status</label>
		                <c:out value="${stateID}"/>
		              </div>
		            </div>
				</div>
				<form:form method="get" class="form-back"
					action="${pageContext.request.contextPath}/employee">
					<button class="btn btn-danger button-style" size="20" value="Back" type="submit">Back</button>
				</form:form>	
				<form:form action="${logoutUrl}" method="post" class="form-logout"
						id="logoutForm">
						<button class="btn btn-primary button-style" id="tl" type="submit" name="Logout" value="Log out">Logout</button>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
				</form:form>
			</div>
		</div>
	</div>
	
</body>
</html>