<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page session="true"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank SIX | Payment Page</title>
<style type="text/css">
table.inner {
	border: 0px
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
	width: 55%;
	margin-left: 22%;
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
	<div class="container login-cont">
	  <div class="row">
		    <div class="col-xs-12 login-form">
		    	<h2 align="center" class="bank">
					Bank SIX
				</h2>
				<hr>
				<h3 align="center">Welcome ${firstName} ${lastName}</h3>
				<h4 align="center" style="margin-top: 5%"><b>Payment</b></h4>
				<div id="errors" style="color: #ff0000">${errors}</div>
				<form method="post" style="margin-top: 5%" action="dopayment?${_csrf.parameterName}=${_csrf.token}" class="form-signin"
					enctype="multipart/form-data">
					<div class="row">
						<div class="col-md-6">
			              <div class="form-group">
			                <label>From Account</label>
			                <br />
			                <c:out value="${accountNo}"/><input type="hidden" class="form-control border-input" name="accountnumber" value="${accountNo}" />
			              </div>
			            </div>
			            <div class="col-md-6">
			              <div class="form-group">
			                <label>Pay to</label>
			                <br />
			                <select name="organization" style="width: 50%"><c:forEach
										items="${merchants}" var="externaluser" varStatus="loop">
										 <option value="${externaluser.organisationName}"
										 	<c:if test="${loop.index==0}">
										 		selected
										 	</c:if>
										 >${externaluser.organisationName}
										</option>
									</c:forEach>
							</select>
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-6">
			              <div class="form-group">
			                <label>Amount</label>
			                <input type="text" name="amount" class="form-control border-input" size="20" />
			              </div>
			            </div>
			            <div class="col-md-6">
			              <div class="form-group">
			                <label>Description</label>
			                <input type="text" class="form-control border-input"
								maxlength="45" value="${description}" name="description" />
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-12">
			              <div class="form-group">
			                <label>Private Key File <font style="font-weight: normal">(For Critical transactions(> $500) please upload your private key)</font></label>
			                <input type="file" name="PrivateKeyFileLoc" class="btn btn-file" />
			              </div>
			            </div>
					</div>
					<div class="row">
						<div class="col-md-12" align="center">
			              <div class="form-group">
			                <button class="btn btn-success button-style" size="20" value="Submit" type="submit">Submit</button>
			                <button class="btn btn-danger button-style"><a href="account">Cancel</a></button>
			              </div>
			            </div>
					</div>
					<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
				</form>
			</div>
		</div>
	</div>	
</body>
</html>