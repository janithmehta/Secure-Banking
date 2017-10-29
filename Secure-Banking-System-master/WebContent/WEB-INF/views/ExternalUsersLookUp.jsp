<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<title>Assigned External Users</title>
<style type="text/css">
table.inner {
	border: 0px
	}
.container{
  height: 100%;
  width: 100%;
  display: flex;
  position: fixed;
  align-items: centre;
  justify-content: center;
  }
 .container .row{
 width: 100%;
 } 	
 .form input{
  margin: 0px 0px 10px 0px;
  height: 30px;
  }
</style>
</head>


<body>
<c:url value="/j_spring_security_logout" var="logoutUrl" />
<div class = "container">
<div class="row">
	   <div class="col-xs-12 login-form">
<h3 align="center">External User Lookup :</h3>
<form align="center" name="form" onsubmit="return isValid()" method="POST">
<center>
Enter Username : <input name="username" > &nbsp;
<div row>
<div class="col-md-6">
	       <div class="form-group">
<input type="submit" class="btn btn-lg btn-success btn-block" value="Show User Information">
</div></div>
<div class="col-md-12">
	       <div class="form-group">
	<form action="${logoutUrl}" method="post" class="form-logout"
						id="logoutForm">
						<button class="btn btn-primary button-style" id="tl" type="submit" name="Logout" value="Log out">Logout</button>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
				</form></div></div></div>
</form>
</center>

<hr>

<form action="registration/validation" method="POST">
 
<table align="center" cellpadding = "10">
 
<tr>
<td>FIRST NAME</td>
<td><input type="text" name="First_Name" maxlength="30"/>
</td>
</tr>
 <tr>
<td>MIDDLE NAME</td>
<td><input type="text" name="Middle_Name" maxlength="30"/>
</td>
</tr>
<tr>
<td>LAST NAME</td>
<td><input type="text" name="Last_Name" maxlength="30"/>
</td>
</tr>

<tr>
<td>EMAIL ID</td>
<td><input type="text" name="Email_Id" maxlength="100" /></td>
</tr> 
<tr>
<td>Password</td>
<td><input type="password" name="password" maxlength="100" /></td>
</tr> 

<tr>
<td>ADDRESS line 1<br /><br /><br /></td>
<td><textarea name="Address1" rows="4" cols="15"></textarea></td>
</tr>



<tr>
<td>CITY</td>
<td><input type="text" name="City" maxlength="30" />
</td>
</tr>
 

<tr>
<td>ZIP CODE</td>
<td><input type="text" name="Pin_Code" maxlength="6" />
</td>
</tr>
 

<tr>
<td>STATE</td>
<td><input type="text" name="State" maxlength="30" />
</td>
</tr>
 

<tr>
<td>SSN</td>
<td><input type="text" name="SSN" maxlength="30" /></td>
</tr>
 
 </table>
 
</form>
</div></div></div>
</body>

<script language="javascript">
function isValid() {
    var x = document.forms["form"]["username"].value;
    if (x == null || x == "") {
        alert("Insert Username");
        return false;
    }
}
</script>