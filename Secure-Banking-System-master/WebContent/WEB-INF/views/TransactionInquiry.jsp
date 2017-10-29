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

<title>Transaction Inquiry</title>
<style type="text/css">
.table-nonfluid {
	width: auto !important;
	margin-top: 5%;
}
.table-nonfluid th, .table-nonfluid td {
	width: 5%;
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
	width: 60%;
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
  width: 150px;  
}
.bank{
	margin-top: 3%;
}
.button-style{	
  margin: 0px 20px 0px 20px !important;
}
.form-back {
	width: 20%;
	display: inline-block;
}
.form-logout {
	width: 20%;
	display: inline-block;
	margin-left: 35%;
}
.form-back button, .form-logout button {
	text-align: center;
	height: 40px;
	width: 100px; 
}
.desc{
	margin-left: 17%;
	display: inline;
	align: center;
	font-weight: bold;
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
				<form:form method="get" class="form-back"
					action="${pageContext.request.contextPath}/employee">
					<button class="btn btn-danger" size="20" value="Back" type="submit">Back</button>
				</form:form>
				<h4 class="desc">View Transactions</h4>
				<form:form name="form" 
						action="${pageContext.request.contextPath}/employee/transactioninquiry"
						onsubmit="return isAccountValid()" method="GET" class="form-inline" style="margin-top: 5%;">
					<div class="row">
						<div class="col-md-12" align="center">
			              <div class="form-group">
			                <label>Bank Account Number</label>
			                <input type="text" class="form-control border-input" name="account" />
			                <button class="btn btn-success button-style" value="View Transactions" onClick="ViewTransactions()" type="submit">View Transactions</button>
			              </div>
			            </div>
					</div>
				</form:form>
				<div align="center" id="transactionDetails"> 
					<table class="table table-nonfluid">
						<tr>
							<th>Transaction id</th>
							<th>Date</th>
							<th>Type</th>
							<th>Amount</th>
							<th>Transaction Status</th>
							<th>From</th>
							<th>To</th>
							<th>Description</th>
							<c:forEach items="${transactionList}" var="transactionList">
								<tr>
									<td><c:out value="${transactionList.getTransid()}" /></td>
									<td><c:out value="${transactionList.getTransDate()}" /></td>
									<td><c:out value="${transactionList.getTransType()}" /></td>
									<td><c:out value="${transactionList.getAmt()}" /></td>
									<td><c:out value="${transactionList.getTransStatus()}" /></td>
									<td><c:out value="${transactionList.fromacc.getAccountnumber()}" /></td>
									<td><c:out value="${transactionList.toacc.getAccountnumber()}" /></td>
									<td><c:out value="${transactionList.getTransDesc()}" /></td>
								</tr>
							</c:forEach>
					</table>
				</div>
				
			</div>
		</div>
	</div>	
</body>
<script language="javascript">
	/*function ViewTransactions() {
		if(document.forms["form"]["account"].value != null || document.forms["form"]["account"].value != "") {
			if(transactionList.length > 0)
				document.getElementById('transactionDetails').style.display = 'inline';
		}		
	}*/
	function isAccountValid() {
		var x = document.forms["form"]["account"].value;
		if (x == null || x == "") {
			alert("Please Enter Bank Account Number");
			document.form.account.focus();
			return false;
		}
	}
</script>
