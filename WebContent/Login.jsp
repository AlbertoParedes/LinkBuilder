<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Dosis:500" rel="stylesheet">
		<link rel="stylesheet" href="css/style.css" type="text/css">
		<title>Traking</title>
	</head>
	<body>		
		
		<div id="mainInicio">
				<div class="containerApp contLogin">
					
					<h1 class="is">Iniciar sesión</h1>
					<form name="login" method="post" action="Login">
						<input maxlength="255" name="user" id="username" placeholder="Nombre de usuario" type="text" class="TextInput TextInput_large">
						<input maxlength="255" name="password" id="password" placeholder="Contraseña" type="password" class="TextInput TextInput_large">
						<div onclick="verify()" class="btnContinuar">Continuar</div>
					</form>
					
					
					
				</div>
			
		</div>
		
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/scripts.js"></script>
		
		
	</body>
</html>