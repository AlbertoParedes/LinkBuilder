<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Objects.Gson.Cliente"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<link rel="stylesheet" href="css/fontawesome-all.css">
		
		<link rel="stylesheet" href="css/morris.css" type="text/css">
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Lato:300,400" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
		<link rel="stylesheet" href="css/style.css" type="text/css">
		<link rel="stylesheet" href="css/pretty-checkbox.min.css" type="text/css">
		
		<title>LinkBuilder</title>
		<style>::-webkit-scrollbar {display: none;width: 20px;}</style>
		
  		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
  		
	</head>
	
	<body>
		
		<%	
			ArrayList<Cliente> clientes = (ArrayList<Cliente>) request.getAttribute("clientes");
			String nameUser = (String) request.getAttribute("name_user");
			String ventana = (String) request.getAttribute("ventana");
			
		%>
		
		<div class="main">
			<div class="content-menu">
				<ul>
					<li><div id="btnEnlaces" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">link</i></div><h4 class="titleItem">Enlaces</h4></li>
					<li><div id="btnMedios" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">sort_by_alpha</i></div><h4 class="titleItem">Medios</h4></li>
					<li><div id="btnClientes" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">perm_identity</i></div><h4 class="titleItem">Clientes</h4></li>				</ul>
			</div>
		</div>
		
		
		
		<div class="content-page">
		
			<!-- Enlaces -->
			<div class="allClients" id='vistaEnlaces'></div>
		
			<!-- Medios -->
			<div class="allKeywords keyCont">
				<div id="lkk" class="listClients"></div>
				<div id="kywData" class="uniqueClient"></div> 
			</div>	
		 
		 	<!-- Clientes -->
		 	<div class="allClientes keyCont">		 		
		 		<div id="divClientes" class="vistaClientes"></div> 
		 	</div>
		</div>
		<div class="blockAll inner_pop_up" onclick="bloquearTodo()"><div class="loader"><div class="l_d1"></div><div class="l_d2"></div><div class="l_d3"></div><div class="l_d4"></div><div class="l_d5"></div></div></div>
		<iframe name="null" style="display:none"></iframe>
		<div id="panelConfirmacion" class="panelConfirmacion">
			<div class="tableRow">
				<div class="tableCell"></div>
			</div>
		</div>
		
		
		<script src="js/jquery-3.3.1.min.js"></script>
		
		<script type="text/javascript" src="js/script_enlaces.js"></script>
		<script type="text/javascript" src="js/script_clientes.js"></script>
		<script type="text/javascript" src="js/script_medios.js"></script>
		
		
		<link href="css/datedropper.css" rel="stylesheet" type="text/css" />
		<link href="css/calendar.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="js/datedropper.js"></script>
		
		<script type="text/javascript" src="js/scripts.js"></script>
		<script type="text/javascript" src="js/ityped.js"></script>
		<script type="text/javascript" src="js/morris.min.js"></script>
		<script type="text/javascript" src="js/raphel.js"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
		
		<!-- bootstrap -->
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
		<div id="loadPage">
			<script>
				if("<%=ventana%>" == "enlaces"){
					//cargarVistaEnlaces();
					$('#btnEnlaces').click();
				}else if("<%=ventana%>" == "medios"){
					$('#btnMedios').click();
				}else if("<%=ventana%>" == "clientes"){
					$('#btnClientes').click();
				}
			</script>
		</div>
	
	</body>
	
</html>





















