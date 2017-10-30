<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank SIX | Account Details Page</title>
<!-- Nice Scroll Js CDN -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.nicescroll/3.6.8-fix/jquery.nicescroll.min.js"></script>
<script>
$(document).ready(function () {

    $('#sidebar').niceScroll({
        cursorcolor: '#53619d', // Changing the scrollbar color
        cursorwidth: 4, // Changing the scrollbar width
        cursorborder: 'none', // Rempving the scrollbar border
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

});
</script>

<style>

body {
    font-family: 'Poppins', sans-serif;
    background: #fafafa;
}

p {
    font-family: 'Poppins', sans-serif;
    font-size: 1.1em;
    font-weight: 300;
    line-height: 1.7em;
    color: #999;
}

/* a, a:hover, a:focus {
    color: inherit;
    text-decoration: none;
    transition: all 0.3s;
}
 */
#sidebar {
    /* don't forget to add all the previously mentioned styles here too */
    background: #337ab7;
    color: #fff;
    transition: all 0.3s;
    min-width: 250px;
    max-width: 250px;
	height: 100vh;
}

#sidebar .sidebar-header {
    padding: 20px;
    background: #428bca;
}

#sidebar ul.components {
    padding: 20px 0;
}

#sidebar ul p {
    color: #fff;
    padding: 10px;
}

#sidebar ul li a {
    padding: 10px;
    font-size: 1.1em;
    display: block;
}
/* #sidebar ul li a:hover {
    color: #7386D5;
    background: #fff;
} */

#sidebar ul li.active > a, a[aria-expanded="true"] {
    color: #fff;
    background: #6d7fcc;
}
ul ul a {
    font-size: 0.9em !important;
    padding-left: 30px !important;
    background: #6d7fcc;
}

.wrapper {
        display: flex;
}


a[data-toggle="collapse"] {
    position: relative;
}

a[aria-expanded="false"]::before, a[aria-expanded="true"]::before {
    content: '\e259';
    display: block;
    position: absolute;
    right: 20px;
    font-family: 'Glyphicons Halflings';
    font-size: 0.6em;
}

a[aria-expanded="true"]::before {
    content: '\e260';
}

@media (max-width: 768px) {
    #sidebar {
        margin-left: -250px;
    }
    #sidebar.active {
        margin-left: 0;
    }
}

.btn-primary-test{

color: #fff;
background-color: #337ab7;
border-color: none; !important

}

.btn-primary-test:hover {
background-color: #286090;
border-color: #204d74;
color: #fff;
text-decoration: none;
}

</style>
</head>
<body>

	<div class = "wrapper">
		<nav id = "sidebar">	
			<div class="sidebar-header">
				<h3>Welcome ${firstName} ${lastName}</h3>
			</div>
			
			<ul class="list-unstyled components">
				<li>
					<a href="debit" class="btn btn-primary-test" role="button">Debit</a>
				</li>
				<li>
					<a href="credit" class="btn btn-primary-test" role="button">Credit</a>
				</li>
				<li>
					<a href="transfer" class="btn btn-primary-test" role="button">Transfer</a>
				</li>
				<li>
					<a href="payment" class="btn btn-primary-test" role="button">Make Payment</a>
				</li>
				<li>
					<a href="downloadpage" class="btn btn-primary-test" role="button">Account Information</a>
				</li>
				<li>
					<a href="customer" class="btn btn-primary-test" role="button">Back</a>
				</li>
				<li>
					<c:url var="logoutUrl" value="/j_spring_security_logout" />
					<form action="${logoutUrl}" method="post" id="logout">
						<a href="javascript:void(0)" onclick="document.getElementById('logout').submit();" class="btn btn-primary-test">Logout</a> <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>
				</li>
			</ul>
		</nav>
		
		<div id="page-content-wrapper" style="background-color: #fff;">
			<div class="container">
				<div class="col-md-12" align="center">
					<div class="row">
						<h2 align="center" class="bank"> Bank SIX </h2>
						<hr>
					</div>
					<div class="row">
						<p>${message}</p>
					</div>
					
					<div class="row">
						<div class="col-md-4">
							<span>Account Number: <c:out value="${accountnumber}"/></span>
						</div>
						<div class="col-md-4">
							<span>Account Type: <c:out value="${accountType}"/></span>
						</div>
						<div class="col-md-4">
							<span>Your Account Balance: $<c:out value="${balance}"/></span>
						</div>
						<br />
						<br />
					</div>
					
					<div class="row">
						<table class="table table-nonfluid" cellpadding="3" cellspacing="3">
							<tr>
								<th>Date</th>
								<th>Type</th>
								<th>From Account</th>
								<th>To Account</th>
								<th>Description</th>
								<th>Status</th>
								<th>Amount</th>
								<!-- <th>Balance</th> -->
							</tr>
							<c:if test="${fn:length(transactions) == 0}">
								<tr>
									<td colspan=7 align="center">No transactions</td>
								</tr>
							</c:if>

							<c:forEach items="${transactions}" var="transaction">
								<tr>
									<td><c:out value="${transaction.tdate}" /></td>
									<td><c:out value="${transaction.ttype}" /></td>
									<td><c:out value="${transaction.fromacc.getAccountnumber()}" /></td>
									<td><c:out value="${transaction.toacc.getAccountnumber()}" /></td>
									<td><c:out value="${transaction.tdesc}" /></td>
									<td><c:out value="${transaction.tstatus}" /></td>
									<td>$<c:out value="${transaction.amount}" /></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>