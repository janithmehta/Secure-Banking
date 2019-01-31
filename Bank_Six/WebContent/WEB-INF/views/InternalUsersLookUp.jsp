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

<title>Internal Users Lookup</title>

<style type="text/css">
table.inner {
	border: 0px
}
.desc{
	margin-left: 17%;
	display: inline;
	align: center;
	font-weight: bold;
}
.form-back {
	width: 20%;
	display: inline-block;
}
.form-back button {
	text-align: center;
	height: 40px;
	width: 100px; 
}
.bank{
	margin-top: 3%;
}
</style>
</head>

<body>
	<div class = "container">
		<div class="row">
			<h2 align="center" class="bank"> Bank SIX </h2>
			<hr>
			<form:form method="get" class="form-back" action="${pageContext.request.contextPath}/employee">
				<input type="submit" class="btn btn-lg btn-danger" value="Back">
			</form:form>
			<h4 align="center" class="desc">Internal User Lookup</h4>
		</div>	    	
		<div class="row">
			<form:form name="form" align="center" action="${pageContext.request.contextPath}/employee/internaluserlookup" onsubmit="return isValid()" class="form-inline" method="GET">
				User Email : <input type="text" id="email" class="form-control" name="email" />&nbsp;
		 		<input value="View User" type="submit" class="btn btn-primary" />
				<br>
			</form:form>
		</div>	
		<div class="row">
			<form:form class="form-signin" style="margin-top: 5%" method="post" onsubmit="return isValid1()" action="${pageContext.request.contextPath}/employee/internaluserlookup/save">
				<table class="table">
					<tr>
						<th>User ID</th>
						<th>Name</th>
						<th>Address</th>
						<th>SSN</th>
						<th>Access Privilege</th>
					</tr>
					<tr>
						<td>
							<input type="text" name="Userid" maxlength="30" value="${user1.getUsrid()}" class="form-control" readonly="readonly" />
						</td>
						<td>
							<input type="text" name="name" class="form-control" maxlength="30" value="${user1.getName()}" />
						</td>
						<td>
							<input type="text" name="address" class="form-control" rows="4" cols="15" value="${user1.getAddress()}">
						</td>
						<td>
							<input type="text" name="SSN" class="form-control" maxlength="30" value="${user1.getSsn()}" />
						</td>
						<td>
							<input type="text" name="AP" class="form-control" maxlength="30" readonly="readonly" value="${user1.getAccessprivilege()}" />
						</td>
					</tr>
				</table>
				<div id="errors" style="color: #ff0000">${errors}</div>
					<input type="hidden" id="email2" name="email_internalUser" value="${email}" />
					<input type="submit" style="margin-left: 45%;" id="btnModify" class="btn btn-lg btn-success" value="Modify">
			</form:form>
		</div>
		</br>
		<script type="text/javascript">
			function isValid() {
				var email = document.forms["form"]["email"].value;
				if (email == null || email == "") {
					alert("Enter email");
					return false;
				}
				return true;
			}
		</script>
	</div>
</body>
</html>