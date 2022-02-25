<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Registration</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>
	<div class="modal-dialog modal-sm"
		style="position: fixed; left: 40%; bottom: 30%;">

		<div class="modal-content">
			<!--<img src="..\..\assets\icon.png" width="120" height="80" style="margin: auto;">-->
			<div class="modal-header">
				<h5 style="margin: auto;">Register</h5>
			</div>
			<div class="modal-body px-5">
				<form action="LoginRegisterServlet" method="post">
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							Email:<input type="text" name="email" required>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							Password:<input type="password" name="password" required>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							Name:<input type="text" name="name" required>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							Surname:<input type="text" name="surname" required>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							Income:<input type="number" name="income" step="any"required>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3" style="padding: 5px; padding-left: 70px;">
							<input type="submit" class="btn btn-primary" value="Register" name="register">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<p style="margin: auto;">
					Already registered? - <a
						href="http://localhost:8090/FinanceTrackerWebsite/login.jsp">Login
						Here</a>
				</p>
			</div>
		</div>
	</div>
</body>
</html>