<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Login</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- Bootstrap core CSS     -->
<link href="assets/css/bootstrap.min.css" rel="stylesheet" />

<!-- Animation library for notifications   -->
<link href="assets/css/animate.min.css" rel="stylesheet"/>

<!--  Paper Dashboard core CSS    -->
<link href="assets/css/paper-dashboard.css" rel="stylesheet"/>

<!--  Fonts and icons     -->
<link href="http://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
<link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
 <link href="assets/css/themify-icons.css" rel="stylesheet">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	
<!--   Core JS Files   -->
<script src="assets/js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>

<!--  Checkbox, Radio & Switch Plugins -->
<script src="assets/js/bootstrap-checkbox-radio.js"></script>

<!--  Charts Plugin -->
<script src="assets/js/chartist.min.js"></script>

<!--  Notifications Plugin    -->
<script src="assets/js/bootstrap-notify.js"></script>

<!-- Paper Dashboard Core javascript -->
<script src="assets/js/paper-dashboard.js"></script>

<style type="text/css">
<style type="text/css">
.error {
	color: red;
	text-align: center;
}

.blank_row {
	height: 10px;
	background-color: #FFFFFF;
}
.login-cont {
  height: 100%;
  width: 100%;
  display: flex;
  position: fixed;
  align-items: center;
  justify-content: center;
}
.login-form {
  width: 400px;
  height: 400px;
}
.login-form input{
  margin: 20px 0px;
  height: 40px;
}
.details {
  text-align: center;
}
.login-form button{
  text-align: center;
  height: 40px;
  width: 100px;
  margin-left: 40%;
}
.bank{
	margin-top: 3%;
}
</style>
</head>
<body>
	<div class="container login-cont">
	  <div class="row">
	    <div class="col-xs-12 login-form">
	    	<h2 align="center" class="bank">
				G6 Bank<br> <br>
			</h2>
			<!-- <div class="error">${message}</div> -->
	      <h3 class="details">Enter Credentials</h3>
	      <form name="LoginForm" method="post" class="form-signin"
				action="<c:url value='authentication_check' />">
					<input type="email" id="userEmail" name="email" class="form-control" placeholder="Email address" required
							autofocus>
					<input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
					<td align="center" colspan="2"><div class="g-recaptcha"
						data-sitekey="6Lf6kw8TAAAAAMosmegdJlwFmUbqoi41K9IBdXVt"></div></td>
				<a href="ForgotPassword">Forgot Password</a>
				<br />
				<button class="btn btn-success" type="submit">Login</button>
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
	      </form>
	      <br />
	      <p>New User? <a href="registration">Register</a></p>
	    </div>
	  </div>
	</div>
</body>
</html>




