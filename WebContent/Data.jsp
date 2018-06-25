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
		
		<script type="text/javascript" src="js/script_enlaces.js"></script>
		<script type="text/javascript" src="js/script_clientes.js"></script>
		
		
		
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		
		<link href="css/datedropper.css" rel="stylesheet" type="text/css" />
		<link href="css/calendar.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="js/datedropper.js"></script>
		
		<script type="text/javascript" src="js/script.js"></script>
		<script type="text/javascript" src="js/ityped.js"></script>
		<script type="text/javascript" src="js/morris.min.js"></script>
		<script type="text/javascript" src="js/raphel.js"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
		
		<!-- bootstrap -->
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
  		
	</head>
	
	<body>
		
		<%	
			ArrayList<Cliente> clientes = (ArrayList<Cliente>) request.getAttribute("clientes");
			String nameUser = (String) request.getAttribute("name_user");	
			
		%>
		
		<div class="main">
			<div class="content-menu">
				<ul>
					<li><div id="btnClientes" onclick="changePanel(this.id)" class="circleIcono btnSelected"><i class="material-icons googleIcono btnSelected">link</i></div><h4 class="titleItem">Enlaces</h4></li>
					<li><div id="btnKeywords" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">sort_by_alpha</i></div><h4 class="titleItem">Medios</h4></li>
					<li><div id="btnListaClientes" onclick="changePanel(this.id)" class="circleIcono"><i class="material-icons googleIcono">perm_identity</i></div><h4 class="titleItem">Clientes</h4></li>				</ul>
			</div>
		</div>
		
		<div class="content-page">
		
			<!-- Clientes -->
			<div class="allClients">
				<div id="lcc" class="listClients">
					<div class="titleCategory">
						<div class="titleInner">Clientes<div class="horDiv wa"><div id="addC" class="addK"><i class="material-icons addKi">add</i></div><div onclick="searchCliente(event)"><div id="ipCLient" onclick='stopPropagation()' class="srchI"><i onclick="searchCliente(event)" class="material-icons addKi">search</i><input id="searchC" class="searchI" type="text" oninput="searchC()"></div></div></div></div>
					</div>
					<div class="infoCategory">
						<div  class="info"><%=clientes.size()%> clientes</div>
					</div>
					
					<!-- lkc = listKeywordsClient -->
					<div id="lstC" class="listItems">
						<% for (int i = 0; i < clientes.size(); i++){%>
							<div id="<%=clientes.get(i).getIdCliente()%>" onclick="enlaces_SelectClient(this.id, this)" class="item">
								<%if(i!=0){ %><div class="line"></div><%}%>
								<div class="itemChild <%if(clientes.get(i).getEditando()==1){%>blur<%}%>">
									<div class="dominioItem">
										<span class="dominioItem" onmouseover="viewCampo(this)" onmouseout="restartCampo(this)" ><%= clientes.get(i).getDominio() %></span>
									</div>
									<div class="nameItem sName"><%= clientes.get(i).getNombre() %></div>
									<%if(clientes.get(i).getEditando()==0){%>
										<%if(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() == 0){%>
											<div class="noti notiPos"><i class="material-icons lf">done</i></div>
										<%}else{%>
											<div class="noti"><%= clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() %></div>
										<%}%>
										
									<%}%>
									<%if(clientes.get(i).getStatus().equals("our")){%>
										<div class="div_bookmark"><i class="material-icons div_i_bookmark sYS_color">bookmark</i><div class="div_line_bookmark sYS"></div></div>
									<%} else if(clientes.get(i).getStatus().equals("new")){%>
										<div class="div_bookmark"><i class="material-icons div_i_bookmark sOK_color">bookmark</i><div class="div_line_bookmark sOK"></div></div>
									<%}%>
								</div>
								<div class="blockClient <%if(clientes.get(i).getEditando()==1){%>visible<%}%>"><div class="lockDiv"><i class="material-icons lf blur"> lock </i></div></div>
								<div class='pop_up_info_blocked'>
									<div class="msg_blocked">Editando cliente por</div>
    								<div class="msg_name_blocked">Guillermo</div>
    								<div class="lockDiv"><i class="material-icons lf"> lock </i></div>
    								<div class="lockDiv" style="left:23px;"><i class="material-icons lf"> lock </i></div>
								</div>
								<div class="loader"><div class="l_d1"></div><div class="l_d2"></div><div class="l_d3"></div><div class="l_d4"></div><div class="l_d5"></div></div>
							</div>
						<% }%>
						
					</div>
					
				</div>
				
				<div id="uniqueClient" class="uniqueClient">
					
					
					<div class="containerApp">	
						<div class="msgSaludo">Hola <%= nameUser %>, </div>
						<div><span class="msg">Selecciona un cliente.</span></div>							
					</div>	
				</div>
			</div>
		
			<!-- Keywords -->
			<div class="allKeywords keyCont">
				<div id="lkk" class="listClients"></div>
				
				<div id="kywData" class="uniqueClient">
				
					
				</div> 
			</div>	
		 
		 	<!-- lista clientes -->
		 	<div class="allClientes keyCont">		 		
		 		<div id="divClientes" class="vistaClientes">
						
				</div> 
		 	</div>
		</div>
		
		<div class="blockAll" onclick="bloquearTodo()"></div>
		
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
	<iframe name="null" style="display:none"></iframe>
		<div id="panelConfirmacion" class="panelConfirmacion">
			<div class="tableRow">
				<div class="tableCell">
					
				</div>
			</div>
		</div>
	
	</body>
	
</html>





















