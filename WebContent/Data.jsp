<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Objects.Gson.ClienteGson"%>
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
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		
		<link href="css/datedropper.css" rel="stylesheet" type="text/css" />
		<link href="css/calendar.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="js/datedropper.js"></script>
		
		<script type="text/javascript" src="js/script.js"></script>
		<script type="text/javascript" src="js/ityped.js"></script>
		<script type="text/javascript" src="js/morris.min.js"></script>
		<script type="text/javascript" src="js/raphel.js"></script>
		
		<!-- bootstrap -->
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
  		
	</head>
	
	<body>
		
		<%	
			ArrayList<ClienteGson> clientes = (ArrayList<ClienteGson>) request.getAttribute("clientes");
			String nameUser = (String) request.getAttribute("name_user");	
			
		%>
		
		<div class="main">
			<div class="content-menu">
				<ul>
					<li><div id="btnClientes" onclick="changePanel(this.id)" class="circleIcono btnSelected"><i class="material-icons googleIcono btnSelected">link</i></div><h4 class="titleItem">Enlaces</h4></li>
					<li><div id="btnKeywords" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">sort_by_alpha</i></div><h4 class="titleItem">Webs</h4></li>
					<!-- <li><div id="btnEstadisticas" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">trending_up</i></div><h4 class="titleItem">Estadisticas</h4></li> -->
				</ul>
			</div>
		</div>
		
		<div class="content-page">
		
			<!-- Clientes -->
			<div class="allClients">
				<div id="lcc" class="listClients">
					<div class="titleCategory">
						<div class="titleInner">Clientes<div class="horDiv wa"><div id="addC" class="addK"><i class="material-icons addKi">add</i></div><div onclick="searchCliente(event)"><div id="ipCLient" class="srchI"><i class="material-icons addKi">search</i><input id="searchC" class="searchI" type="text" oninput="searchC()"></div></div></div></div>
					</div>
					<div class="infoCategory">
						<div  class="info"><%=clientes.size()%> clientes</div>
					</div>
					
					<!-- lkc = listKeywordsClient -->
					<div id="lstC" class="listItems">
						<% for (int i = 0; i < clientes.size(); i++){%>
							<div id="<%=clientes.get(i).getIdCliente()%>" onclick="selectClient(this.id)" class="item">
								<%if(i!=0){ %><div class="line"></div><%}%>
								<div class="itemChild <%if(clientes.get(i).getEditando()==1){%>blur<%}%>">
									<div class="nameItem">
										<span class="nameItem sName" onmouseover="viewCampo(this)" onmouseout="restartCampo(this)" ><%= clientes.get(i).getNombre() %></span>
									</div>
									<div class="dominioItem"><%= clientes.get(i).getWeb() %></div>
									<%if(clientes.get(i).getEditando()==0){%>
										<%if(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() == 0){%>
											<div class="noti notiPos"><i class="material-icons lf">done</i></div>
										<%}else{%>
											<div class="noti"><%= clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() %></div>
										<%}%>
										
									<%}%>
								</div>
								<div class="blockClient <%if(clientes.get(i).getEditando()==1){%>visible<%}%>"><div class="lockDiv"><i class="material-icons lf blur"> lock </i></div></div>
							</div>
						<% }%>
						
					</div>
					
				</div>
				
				<div id="uniqueClient" class="uniqueClient">
					
					
					<div class="containerApp">	
						<div class="msgSaludo">Hola <%= nameUser %>, </div>
						<div><span class="msg">Selecciona un cliente.</span></div>							
					</div>
					
					<!-- tag element -->
					
					<!-- init dateDropper -->

					 
					<!--
					<div class="infoClient">
						<div class="nameClient"></div>
						<div class="urlClient">http://google.com</div>
					</div>
					<div class="keywordsClient">
						
						<div class="titleTable">Keywords<div class="horDiv"></div></div>
						<div id="keywords_Client" class="contentTable">
							<!--  <div class="itemTable"><div class="itemKeyword">keyword 1</div><div class="itemStatus"><label class="switch"><input type="checkbox"  checked><span class="slider round"></span></label></div><div class="itemDeleted">eliminar</div></div>
							--x>
							
						</div>
					
					</div>
					-->
					
				</div>
			</div>
		
			<!-- Keywords -->
			<div class="allKeywords keyCont">
				<div id="lkk" class="listClients"></div>
				
				<div id="kywData" class="uniqueClient">
				
					
				</div> 
			</div>	
		 
		</div> 
		
		<script type="text/javascript" src="js/scripts.js"></script>
		<script type="text/javascript">
			
			/*window.ityped.init(document.querySelector('.msg'),{
				strings : ['Selecciona un cliente.'],
				disableBackTyping: true,
				loop : false
			})*/
			
			
							
		</script>

	<script type="text/javascript">

		
		
	</script>
		
	</body>
	
</html>





















