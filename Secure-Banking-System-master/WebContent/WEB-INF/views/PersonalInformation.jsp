<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<title>Bank SIX | Personal Information</title>
<style type="text/css">
.PI-cont {
  height: 100%;
  width: 100%;
  display: flex;
  position: fixed;
  align-items: centre;
  justify-content: center;
}
.PI-cont .row {
	width: 100%;
}
.form-signin input{
  margin: 0px 0px 10px 0px;
  height: 30px;
}
.PI{
	margin-left: 19%;
	display: inline;
	align: center;
	font-weight: bold;
}
.details {
  text-align: left;
  
}
.login-form {
	width: 50%;
	margin-left: 20%;
}
.form-signin button{
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
.button-style a{
	text-decoration: none;
	color: white;
	cursor: pointer;
}
</style>
</head>
<body>
<c:url value="/j_spring_security_logout" var="logoutUrl" />
<div class="container PI-cont">
	  <div class="row">
	   <div class="col-xs-12 login-form">
	   <h2 align="center" class="bank">
					Bank SIX
				</h2>
				<hr>

	<button class="btn btn-danger button-style" ><a href="${pageContext.request.contextPath}/customer">Back</a></button>
	<h4 align="center" class="PI">Personal Information</h4>
	<div id="errors" style="color: #ff0000">${errors}</div>

	<form class="form-signin" action="edit" method="post" style="margin-top: 3%;">
		<div class="row">
	   <div class="col-md-4">
	   <div class="form-group">
				<label class="details">Name</label>
				<input type="text" name="name" value="${name}" />
	   </div></div>	
			
           <div class="col-md-4">
           <div class="form-group">
				<label class="details">SSN</label>
				<input type="text" name="ssn" value="${ssn}" disabled/>		
	           
	       </div></div>
	       
	        <div class="col-md-4">
	       <div class="form-group">
				<label class="details">Email Id</label>
				<input type="email" name="email" value="${email}" disabled />
           </div></div></div>
	       <div class="row">
	       <div class="col-md-12">
	       <div class="form-group">
               <label class="details">Address</label>
				<textarea name="address" class="form-control" rows="4"
							cols="15" >${address}</textarea>	
				</div></div></div>
				<div class="row">
	       <div class="col-md-12">
	       <div class="form-group" align="center">
	       		<button class="btn btn-success button-style" value="Save" type="submit">Update</button>
	      </div>
	      </div>
	      </div>
	
		<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>"
			value="<c:out value="${_csrf.token}"/>" />
			
	</form>
		
	</div></div></div>
</body>
</html>