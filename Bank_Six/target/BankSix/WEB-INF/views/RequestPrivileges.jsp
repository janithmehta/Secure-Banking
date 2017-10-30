<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<title>Request Privileges</title>
<style type="text/css">
h3{font-family: Calibri; font-size: 22pt; font-style: normal; font-weight: bold; color:Black;
text-align: center; text-decoration: underline }
table{font-family: Calibri; color:black; font-size: 11pt; font-style: normal;
text-align:; border-collapse: collapse;}
table.inner{border: 0px}
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
				<form action="${logoutUrl}" method="post" class="form-logout"
						id="logoutForm">
						<button class="btn btn-primary button-style" id="tl" type="submit" name="Logout" value="Log out">Logout</button>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
				</form>
				<form align="center" name="form" onsubmit="return isValid()" method="POST">
				<div class="row">
					<div class="col-md-12">
		              <div class="form-group">
		              	<label>Request Privileges</label>
		              	<textarea type="textArea" rows="10" cols="50" name="message" placeholder="Type your message here. . ."/>
		              </div>
		            </div>
				</div>
				<div class="row">
					<div class="col-md-12" align="center">
		              <div class="form-group">
		                <button class="btn btn-success button-style" size="20" value="Send Message" type="submit">Send Message</button>
		              </div>
		            </div>
				</div>
			</form>
			</div>
		</div>
	</div>
</body>
<script language="javascript">
function isValid() {
    var x = document.forms["form"]["message"].value;
    if (x == null || x == "") {
        alert("Message cannot be blank");
        return false;
    }
}
</script>