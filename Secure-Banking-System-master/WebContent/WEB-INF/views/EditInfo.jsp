<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<title>Update Personal Information</title>

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

	<h3 align="center">Update Your Personal Information</h3>

	<form:form
		action="${pageContext.request.contextPath}/employee/editinfo/save"
		name="Registration" method="post" onsubmit="return isValid()"
		class="form-signin">
		<div id="errors" style="color: #ff0000">${errors}</div>

		<table align="center" class="table table-nonfluid" cellpadding="10">
			<tr>
				<td>FIRST NAME *</td>
				<td><input type="text" name="name" maxlength="30"
					class="form-control" value="${user.getName()}" />(max 30
					characters a-z and A-Z)</td>
			</tr>

			<%-- <tr>
				<td>MIDDLE NAME</td>
				<td><input type="text" name="MName" maxlength="30"
					class="form-control" value="${user.getMiddlename()}" />(max 30
					characters a-z and A-Z)</td>
			</tr>

			<tr>
				<td>LAST NAME *</td>
				<td><input type="text" name="LName" maxlength="30"
					class="form-control" value="${user.getLastname()}" />(max 30
					characters a-z and A-Z)</td>
			</tr>
 --%>
			<tr>
				<td>EMAIL ID *</td>
				<td><input type="text" name="Email" maxlength="50"
					class="form-control" value="${user.getEmail().getUsername()}"
					disabled /></td>
			</tr>

			<tr>
				<td>Password *</td>
				<td><input type="password" name="Pass" maxlength="100"
					class="form-control" value="" /></td>
			</tr>

			<tr>
				<td>Retype Password *</td>
				<td><input type="password" name="RPass" maxlength="100"
					class="form-control" value="" /></td>
			</tr>

			<tr>
				<td>ADDRESS*<br /> <br /> <br /></td>
				<td><textarea name="address" rows="4" cols="15"
						class="form-control" >${user.getAddress()}</textarea></td>
			</tr>

			<%-- <tr>
				<td>ADDRESS line 2 *<br /> <br /> <br /></td>
				<td><textarea name="Address2" rows="4" cols="15"
						class="form-control" >${user.getAddressline2()}</textarea></td>
			</tr>

			<tr>
				<td>CITY *</td>
				<td><input type="text" name="City" maxlength="30"
					class="form-control" value="${user.getCity()}" />(max 30
					characters a-z and A-Z)</td>
			</tr>

			<tr>
				<td>ZIP CODE *</td>
				<td><input type="text" name="Zipcode" maxlength="6"
					class="form-control" value="${user.getZipcode()}" />(6 digit
					number)</td>
			</tr>


			<tr>
				<td>STATE *</td>
				<td><input type="text" name="State" maxlength="30"
					class="form-control" value="${user.getState()}" />(max 30
					characters a-z and A-Z)</td>
			</tr>
 --%>

			<tr>
				<td>SSN *</td>
				<td><input type="text" name="SSN" maxlength="30"
					class="form-control" value="${user.getSsn()}" /></td>
			</tr>


			<tr>
				<td colspan="2" align="center"><input type="submit"
					class="form-control" value="Update Info"></td>
			</tr>
		</table>

	</form:form>
	<form:form method="get"
		action="${pageContext.request.contextPath}/employee">
		<input type="submit" class="btn btn-lg btn-primary btn-block"
			value="Back">
	</form:form>

	<script language="javascript">
		function isValid() {
			var a = document.forms["Registration"]["name"].value;
			if (a == null || a == "") {
				alert("please Enter First Name");
				return false;
			}

			var m = document.forms["Registration"]["LName"].value;
			if (m == null || m == "") {
				alert("please Enter the Last Name");
				return false;
			}

			var f = document.forms["Registration"]["address"].value;
			if (f == null || f == "") {
				alert("please Enter Address");
				return false;
			}

			var h = document.forms["Registration"]["Address2"].value;
			if (h == null || h == "") {
				alert("please Enter Address");
				return false;
			}

			var g = document.forms["Registration"]["City"].value;
			if (g == null || g == "") {
				alert("please Enter City");
				return false;
			}

			var j = document.forms["Registration"]["Zipcode"].value;
			if (j == null || j == "") {
				alert("please Enter Zipcode");
				return false;
			}
			if (isNaN(j) || j.indexOf(" ") != -1) {
				alert("Enter numeric value");
				return false;
			}
			if (j.length > 5) {
				alert("Enter only 5 characters");
				return false;
			}

			var i = document.forms["Registration"]["State"].value;
			if (i == null || i == "") {
				alert("please Enter State");
				return false;
			}

			var k = document.forms["Registration"]["SSN"].value;
			if (k == null || k == "") {
				alert("please Enter the SSN number");
				return false;
			}
			if (isNaN(k) || k.indexOf(" ") != -1) {
				alert("Enter numeric value");
				return false;
			}
			if (k.length > 10) {
				alert("enter 9 characters");
				return false;
			}
		}
	</script>

</body>
</html>