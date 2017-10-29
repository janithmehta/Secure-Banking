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

<title>Transaction Lookup</title>

<style type="text/css">
table.inner {
	border: 0px
}

.table-nonfluid {
	table-layout:fixed;
    width:100%;
}
.table-nonfluid input{
	width: 100%;
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
.lookup-button {
	text-align: center;
	height: 40px;
	width: 100px; 
}
.form-button {
	width: 10%;
	display: inline-block;
}

</style>
</head>
<body>
<div class = "container">
	<div class="row">
			<h2 align="center" class="bank"> Bank SIX </h2>
			<hr>
			<form:form method="get" class="form-back"
				action="${pageContext.request.contextPath}/employee">
				<input type="submit"
					class="btn btn-lg btn-danger" value="Back">
			</form:form>
			<h4 align="center" class="desc">Transaction Lookup</h4>			
		</div>	
		<div class="row">
					<form:form name="form" align="center"
				action="${pageContext.request.contextPath}/employee/transactionlookup"
				class="form-inline" onsubmit="return isValid()" method="GET">
				<label><font font-weight="normal">Transaction ID</font></label> <input type="number" min="0" name="transid" class="form-control" />&nbsp;
				 <input value="View Transaction" type="submit" class="btn btn-lg btn-primary " />
			</form:form>
		</div>
	<div class="row">
		<div align="center" style="margin-top: 5%">
			<table border="1" class="table table-nonfluid">
				<tr>
					<th>Tid</th>
					<th>Date</th>
					<th>Type</th>
					<th>Amount</th>
					<th>Status</th>
					<th>From</th>
					<th>To</th>
					<th>Description</th>
				<tr>
					<td><input type="text" name="Tid"
						value="${transaction.getTransid()}" disabled></td>
					<td><input type="text" name="Date"
						value="${transaction.getTransDate()}" disabled></td>
					<td><input type="text" name="Type"
						value="${transaction.getTransType()}" disabled></td>
					<td><input type="text" id="amt" name="Amount"
						value="${transaction.getAmount()}"></td>
					<td><input type="text" name="Status"
						value="${transaction.getTransStatus()}" disabled></td>
					<td><input type="text" name="From"
						value="${transaction.fromacc.getAccountnumber()}" disabled></td>
					<td><input type="text" name="To"
						value="${transaction.toacc.getAccountnumber()}" disabled></td>
					<td><input type="text" name="Desc"
						value="${transaction.getTransDesc()}" disabled></td>
				</tr>
			</table>
			</div>
			<div class="row" align="center">
				<form:form method="post" onsubmit="return isValid1()" class="form-button"
							action="${pageContext.request.contextPath}/employee/transactionlookup/authorize">
							<input type="hidden" id="1_" name="Tid_"
								value="${transaction.getTransid()}">
							<input type="submit" class="btn btn-success lookup-button"
								id="btnAuthorize" value="Authorize"> &nbsp;</form:form>
				<form:form method="post" onsubmit="return isValid1()" class="form-button"
							action="${pageContext.request.contextPath}/employee/transactionlookup/cancel">
							<input type="hidden" id="2_" name="Tid_"
								value="${transaction.getTransid()}">
							<input type="submit" id="btnCancel"
								class="btn btn-default lookup-button" value="Cancel"> &nbsp;</form:form>
				<form:form method="post" onsubmit="return isValid1()" class="form-button"
							action="${pageContext.request.contextPath}/employee/transactionlookup/modify">
							<input type="hidden" id="3_" name="Tid_"
								value="${transaction.getTransid()}">
							<input type="hidden" id="amt_" name="Amount_"
								value="${transaction.getAmount()}" />
							<input type="submit" id="btnModify"
								class="btn btn-info lookup-button" value="Modify">
						</form:form>								
		</div>
	</div>
	
	<script type="text/javascript">
		function isValid() {
			var x = document.forms["form"]["transid"].value;
			if (x == null || x == "") {
				alert("Insert Transaction ID");
				return false;
			}
		}

		function isValid1() {
			var x1 = document.getElementById("1_").value;
			var x2 = document.getElementById("2_").value;
			var x2 = document.getElementById("3_").value;

			if ((x1 == "" || x1 == null) && (x2 == "" || x2 == null)
					&& (x3 == null || x3 == ""))
				return false;

			document.getElementById("amt_").value = document
					.getElementById("amt").value;

			return true;
		}
	</script>
</div>	
</body>
</html>