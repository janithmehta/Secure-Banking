<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

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

<title>System Administrator</title>


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

a, a:hover, a:focus {
    color: inherit;
    text-decoration: none;
    transition: all 0.3s;
}

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
#sidebar ul li a:hover {
    color: #7386D5;
    background: #fff;
}

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
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	
	
	<div class = "wrapper">
		
		<nav id = "sidebar">	
			<div class="sidebar-header">
            		<h3>System Administrator</h3>
        		</div>
			
			<ul class="list-unstyled components">
				<li>
					<form:form name="tl" method="post" action="${pageContext.request.contextPath}/employee/internaluserlookup">
						<input class="btn btn-lg btn-primary-test btn-block" id="tl" CELLPADDING="4" CELLSPACING="3" type="submit" name="InternalUser" value="Internal User Lookup" />
					</form:form>
				</li>
				<li>
					<form:form name="ti" method="post" action="${pageContext.request.contextPath}/employee/logs">
						<input class="btn btn-lg btn-primary-test btn-block" id="tl" CELLPADDING="4" CELLSPACING="3" type="submit" name="Logs" value="Logs" />
					</form:form>
				</li>
				<li>
					<form:form name="ei" method="post" action="${pageContext.request.contextPath}/employee/editinfo">
						<input class="btn btn-lg btn-primary-test btn-block" id="tl" CELLPADDING="4" CELLSPACING="3" type="submit" name="EditInfo" value="Edit Personal Information" />
					</form:form>
				</li>
				<li>
					<form:form name="pii" method="post" action="${pageContext.request.contextPath}/employee/pii">
						<input class="btn btn-lg btn-primary-test btn-block" id="tl" CELLPADDING="4" CELLSPACING="3" type="submit" name="PII" value="PII" />
					</form:form>
				</li>
				<li>
					<form:form action="${logoutUrl}" method="post" id="logoutForm">
						<input class="btn btn-lg btn-primary-test btn-block" id="tl" CELLPADDING="4" CELLSPACING="3" type="submit" name="Logout" value="Log out" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form:form>
				</li>
			</ul>
		</nav>
		
		<div id="page-content-wrapper" style="background-color: #fff;">
			<div class="container">
				<div class="col-md-12" align="center" style="margin-top:10%">
					<form:form id="taskForm" method="post" action="${pageContext.request.contextPath}/employee">
						<table class="table">
							<tr>
								<th>Task Id</th>
								<th>Message</th>
								<th>Status</th>
								<th>Transaction ID</th>
								<th>Selected</th>
							</tr>
		
							<c:forEach items="${taskList}" var="taskList">
								<tr>
									<td><c:out value="${taskList.task_id}" /></td>
									<td><c:out value="${taskList.message}" /></td>
									<td><c:out value="${taskList.status}" /></td>
									<td><c:out value="${taskList.transid.getTransid()}" /></td>
									<td><input type="radio" name="task"
										value="${taskList.task_id}" /></td>
								</tr>
							</c:forEach>
						</table>
						<br />
						<br />
						<input type="hidden" id="taskselected" name="taskselected" value="">
						<input type="submit" class="btn btn-lg btn-success" value="Submit">
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- div class="col-md-4">
		<p align='left'>
		<table id="table1" class="table">
			<tr>
				<td>
					
				</td>
			</tr>

			<tr>
				<td>
					
				</td>
			</tr>

			<tr>
				<td>
					
				</td>
			</tr>
			<tr>
				
			</tr>
			<tr>
				<td>
					
				</td>
			</tr>
		</table>
	</div> -->

	<%-- <div class="col-md-6">
		<form:form id="taskForm" method="post" action="${pageContext.request.contextPath}/employee">
			<table class="table">
				<tr>
					<th>Task Id</th>
					<th>Message</th>
					<th>Status</th>
					<th>Tid</th>
					<th>Selected</th>
				</tr>

				<c:forEach items="${taskList}" var="taskList">
					<tr>
						<td><c:out value="${taskList.task_id}" /></td>
						<td><c:out value="${taskList.message}" /></td>
						<td><c:out value="${taskList.status}" /></td>
						<td><c:out value="${taskList.transid.getTransid()}" /></td>
						<td><input type="radio" name="task"
							value="${taskList.task_id}" /></td>
					</tr>
				</c:forEach>
			</table>
			<br />
			<br />
			<input type="hidden" id="taskselected" name="taskselected" value="">
			<input type="submit" class="btn btn-lg btn-success" value="Submit">
		</form:form>
 --%>
		<script>
			$(document).ready(
				function() {
					$("#taskForm").submit(
						function() {
							$("#taskselected").val(
								$("input[name=task]:checked", "#taskForm").val()
							);
							aler("invoked");
							if ($("#taskselected").val() == "")
								return false;
							else
								return true;
						});
					});
		</script>
	<!-- </div> -->
</body>
</html>
