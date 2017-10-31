<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script src='https://www.google.com/recaptcha/api.js'></script>

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
  height: 520px;
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
				Bank SIX
			</h2>
			<hr>
			<div class="error">${message}</div>
	      <h3 class="details">Enter Credentials</h3>
	      <form name="LoginForm" method="post" class="form-signin"
				action="<c:url value='authentication_check' />" onsubmit="return registerValid()">
					<input type="email" id="userEmail" name="email" class="form-control" placeholder="Email address" required
							autofocus>
					<input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
		              <div class="g-recaptcha"
						data-sitekey="6LdPyDUUAAAAAKIu3-_MBYotx4ATiiLt6duETtNN"></div>
						<span id = "captcha"></span>
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
	<script language="javascript">
		function registerValid() {
			var v = grecaptcha.getResponse();
            if(v.length == 0)
            {
                document.getElementById('captcha').innerHTML="You can't leave Captcha Code empty";
                return false;
            }
			return true;
		}
	</script>
</body>
</html>




