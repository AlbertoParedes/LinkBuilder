package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.SystemOutLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Classes.OrdenarObjetos;
import Classes.ParsingJson;
import Classes.Webservice;
import Objects.Cliente2;
import Objects.Resultado;
import Objects.Gson.Categoria;
import Objects.Gson.CategoriaGson;
import Objects.Gson.Cliente;
import Objects.Gson.ClienteGson;
import Objects.Gson.Empleado;
import Objects.Gson.Enlace;
import Objects.Gson.Foro;
import Objects.Gson.ForoGson;
import Objects.Gson.ResultadoGson;
import Objects.Gson.TematicaGson;

@WebServlet("/Data_Enlaces")
public class Data_Enlaces extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Webservice ws = new Webservice();
	private ParsingJson pj = new ParsingJson();
	private Cliente cliente = new Cliente();
	private OrdenarObjetos ob = new OrdenarObjetos();
	private Empleado empleado = new Empleado();


	private ArrayList<Enlace> enlaces =  new ArrayList<Enlace>();
	private ArrayList<Foro> forosDisponibles = new ArrayList<Foro>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();



	public Data_Enlaces() {super();}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		empleado = (Empleado) request.getAttribute("empleado");
		
		System.out.println("-------->"+empleado.getName());
		
		/*
		HttpSession session = request.getSession();
		int id_empleado = Integer.parseInt(session.getAttribute("id_user").toString());
		String nick_empleado = session.getAttribute("name_user").toString();
		String role_empleado = session.getAttribute("role_user").toString();
		*/

		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(empleado.getId()+""));
		String json = ws.clientes(wbList, "getClientes", "clientes.php");
		String[] jsonArray = json.split(";;");
		System.out.println("0- :"+jsonArray[0]);
		System.out.println("1- :"+jsonArray[1]);
		//empleado = new Gson().fromJson(jsonArray[0].substring(1, jsonArray[0].length()-1), Empleado.class);

		this.clientes.clear();
		clientes = pj.parsearClientesMap(jsonArray[1]);
		ob.clientesByDomain(clientes);

		//request.setAttribute("clientes", clientes);//pasamos la lista de clientes
		request.setAttribute("name_user", empleado.getName());
		request.setAttribute("ventana",empleado.getPanel());
		request.getRequestDispatcher("Data.jsp").forward(request, response);
		
		//doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int id_empleado = Integer.parseInt(session.getAttribute("id_user").toString());
		String nick_empleado = session.getAttribute("name_user").toString();
		String role_empleado = session.getAttribute("role_user").toString();

		String metodo = request.getParameter("metodo");
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		if(metodo.equals("cargarVistaEnlaces")) {
			cargarVistaEnlaces(request, response, out);
		}else if(metodo.equals("enlaces_SelectClient")) { 
			try {selectClient(request, response, out,id_empleado, role_empleado);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_CheckClients")) {
			//try {checkClients(request, response, out,id_user,role);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_ResultadosMes")) {
			resultadosMes(request, response, out, role_empleado);
		}else if(metodo.equals("guardarEnlaceResultado")) {
			guardarEnlaceResultado(request, response, out,id_empleado);
		}else if(metodo.equals("mostrarWebResultado")) {
			mostrarWebResultado(request, response, out);
		}else if(metodo.equals("borrarCategoriaResultado")) {
			borrarCategoriaResultado(request, response, out);
		}else if(metodo.equals("enlaces_guradarDestino")) {
			guradarDestino(request, response, out);
		}else if(metodo.equals("guardarCategoriaResultado")) {
			guardarCategoriaResultado(request, response, out);
		}else if(metodo.equals("guardarWebResultado")) {
			guardarWebResultado(request, response, out);
		}else if(metodo.equalsIgnoreCase("enlaces_buscarMedio")) {
			buscarMedio(request, response, out);
		}else if(metodo.equals("enlaces_nuevoDestino")) {
			nuevoDestino(request, response, out);
		}else if(metodo.equals("enlaces_GuardarAnchor")) {
			guardarAnchor(request, response, out);
		}else if(metodo.equals("enlaces_BuscarCliente")) {
			buscarCliente(request, response, out);
		}

	}

	private void cargarVistaEnlaces(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		String html="";
		int clienteSeleccionado=-1;
		for (int i = 0; i < clientes.size(); i++){
				String itemSleccionado="";
				if(clienteSeleccionado == clientes.get(i).getIdCliente()) {itemSleccionado="item_select";}
				html+="		<div id='"+clientes.get(i).getIdCliente()+"' onclick='enlaces_SelectClient(this.id, this)' class='item "+itemSleccionado+"'>";
				if(i!=0){html+="<div class='line'></div>";}
				html+="			<div class='itemChild";if(clientes.get(i).getEditando()==1)html+=" blur"; html+="'>";
				html+="				<div class='dominioItem'>";
				html+="					<span class='dominioItem' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+clientes.get(i).getDominio()+"</span>";
				html+="				</div>";
				html+="				<div class='nameItem sName'>"+clientes.get(i).getNombre()+"</div>";
				String opacity="";
				if(clientes.get(i).getEditando()==1){opacity="opacity_0";}
				if(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() == 0){
					html+="		<div class='noti notiPos "+opacity+"'><i class='material-icons lf'>done</i></div>";
				}else{
					html+="		<div class='noti "+opacity+"'>"+(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone())+"</div>";
				}
				if(clientes.get(i).getStatus().equals("our")){ 		
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sYS_color'>bookmark</i><div class='div_line_bookmark sYS'></div></div>";
				}
				else if(clientes.get(i).getStatus().equals("new")){ 
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sOK_color'>bookmark</i><div class='div_line_bookmark sOK'></div></div>";
				}
				html+="			</div>";
				html+="			<div class='blockClient"; if(clientes.get(i).getEditando()==1)html+="visible";html+="'><div class='lockDiv'><i class='material-icons lf blur'> lock </i></div></div>";
				html+="			<div class='pop_up_info_blocked'>";
				html+="				<div class='msg_blocked'>Editando cliente por</div>";
				html+="				<div class='msg_name_blocked'>Guillermo</div>";
				html+="				<div class='lockDiv'><i class='material-icons lf'> lock </i></div>";
				html+="				<div class='lockDiv' style='left:23px;'><i class='material-icons lf'> lock </i></div>";
				html+="			</div>";
				html+="			<div class='loader'><div class='l_d1'></div><div class='l_d2'></div><div class='l_d3'></div><div class='l_d4'></div><div class='l_d5'></div></div>";
				html+="		</div>";
		}
		
		out.println("<div id='lcc' class='listClients'>");
		out.println("	<div class='titleCategory'>");
		out.println("		<div class='titleInner'>Clientes<div class='horDiv wa'><div id='addC' class='addK'><i class='material-icons addKi'>add</i></div><div onclick='searchCliente(event)'><div id='ipCLient' onclick='stopPropagation()' class='srchI'><i onclick='searchCliente(event)' class='material-icons addKi'>search</i><input id='searchC' class='searchI' type='text' oninput='searchC()'></div></div></div></div>");
		out.println("	</div>");
		out.println("	<div class='infoCategory'>");
		out.println("		<div  class='info'>"+clientes.size()+" clientes</div>");
		out.println("	</div>");
		out.println("	<div id='lstC' class='listItems'>");
		out.println(		html);
		out.println("	</div>");
		out.println("</div>");
		out.println("	<div id='uniqueClient' class='uniqueClient'>");
		out.println("		<div class='containerApp'>");
		out.println("			<div class='msgSaludo'>Hola "+empleado.getName()+", </div>");
		out.println("			<div><span class='msg'>Selecciona un cliente.</span></div>");
		out.println("		</div>");
		
		out.println("	</div>");
		
	}

	private void selectClient(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_empleado, String user_role) throws IOException, ParseException {
		System.out.println();
		String id_client = request.getParameter("id_client");
		String fecha;
		
		//if(fecha.contains("undefined")) {//cuando obtenemos el a�o por javascript nos devueve siempre undefined si estamos en el mes correspondiente al dia de hoy
			Date today = new Date(); 
			Calendar cal = Calendar.getInstance(); cal.setTime(today);
			int month = cal.get(Calendar.MONTH)+1, year = cal.get(Calendar.YEAR);
			String mes = month+""; if(month<10) mes= "0"+mes;
			fecha = year+"-"+mes;
		//}
		
		System.out.println("fechaaaaaa: "+fecha);

		String json = ws.getEnlaces(id_client,id_empleado+"", fecha ,"getEnlaces","enlaces.php");
		System.out.println(json);
		String[] jsonArray = json.split(";;");
		System.out.println("0- "+jsonArray[0]);//array de enlaces


		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();
		String blocked="",userEditando="";

		int disponibilidad=0;
		//parseamos los enlaces (resultados)
		ArrayList<Enlace> enlaces = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonArray[0], new TypeToken<List<Enlace>>(){}.getType());
		System.out.println("#"+enlaces.size());
		if(enlaces.size() != 0) {
			disponibilidad = enlaces.get(0).getDisponibilidad();
			userEditando = enlaces.get(0).getUserEditando();	
		}else if(enlaces.size() == 0) {//si es igual a cero significa que no se ha devuelto ningun resultado
			disponibilidad = 2;
		}
		
		if(disponibilidad==1) {
			System.out.println("1- "+jsonArray[1]);//array de foros
			System.out.println("2- "+jsonArray[2].substring(1, jsonArray[2].length()-1));//array de datos del cliente
			//System.out.println("3- "+jsonArray[3]);//array de datos del cliente 

			//parseamos los foros disponibles
			ArrayList<Foro> forosDisponibles = new Gson().fromJson(jsonArray[1], new TypeToken<List<Foro>>(){}.getType());
			this.forosDisponibles.clear();
			this.forosDisponibles = forosDisponibles;

			this.enlaces.clear();
			this.enlaces = enlaces;
			//parseamos al cliente seleccionado
			Cliente cliente = new Gson().fromJson(jsonArray[2].substring(1, jsonArray[2].length()-1), Cliente.class);
			this.cliente = cliente;

			mostrarResultados(request, response, out, user_role, obj);

			

		}else if(disponibilidad==2) {
			obj.put("blocked", "2");
			out.print(obj);

			System.out.println("No ha ningun resultado para este cliente");
		}else {

			blocked="1";

			obj.put("blocked", blocked);
			obj.put("userEditando", userEditando);
			out.print(obj);

			System.out.println("Estas bloqueado por "+userEditando);
		}






	}



	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out,String empleado_role, JSONObject obj) throws IOException, ParseException {
		//obtenemosForos();
		//int posicion = Integer.parseInt(request.getParameter("posicion"));
		/*Date date= new Date();Calendar cal = Calendar.getInstance();cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		//con esto mantenemos la fecha seleccionada aunque cambiemos de cliente
		/*try {
			mes = Integer.parseInt(request.getParameter("mes"));
			year = Integer.parseInt(request.getParameter("year"));
		} catch (Exception e) {}
		 */

		//out = response.getWriter();

		String html="",blocked="0";

		html+="<div class='infoClient'>";
		html+="	<div class='nameClient'>"+cliente.getWeb()+"</div>";
		html+="	<div class='urlClient'>"+cliente.getNombre()+"</div>";
		html+="	<input id='dateSelected' onchange='changeMonth()' class='inCal' data-lock='to' type='text' data-lang='es' data-min-year='2017'  data-large-mode='false' data-format='F' data-theme='calendar'/>";
		html+="	<script>$('.datedropper').remove();$('#dateSelected').dateDropper();</script>";
		html+="</div>";

		//barra de herramientas
		html+="<div class='ctoolbar'><div id='cGuardar' class='zoom'>guardar</div></div>";

		html+="<div class='div_table'>";
		//html+="	<div class='titleTable'>Keywords<div class='horDiv'></div></div>";
		html+="	<div id='results_Client' class='contentTable'>";
		//tabla
		html+="		<table id='tClientes' class='table'>";
		if(!empleado_role.equals("user_paid")) {
			html+="			<thead class=''><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer'><i class='material-icons f_size26'>account_circle</i></th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cCateg'>Categoria</th><th class='cabeceraTable cWeb'>Medio</th><th class='cabeceraTable cLink'>Enlace</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>";
		}else {
			html+="			<thead class=''><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer'><i class='material-icons f_size26'>account_circle</i></th><th class='cabeceraTable pago_web'>Medio</th><th class='cabeceraTable cPrecio'>Coste</th><th class='cabeceraTable cPrecio'>Venta</th><th class='cabeceraTable cPrecio cBeneficio'>Beneficio</th><th class='cabeceraTable cPrecio c_incremento'>Incremento</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cLink'>Link</th></tr></thead>";
		}
		html+="			<tbody>";
		html+="			</tbody>"; 
		html+="		</table>";
		html+="	</div>";

		html+="</div>";


		obj.put("blocked", blocked);
		obj.put("html", html);
		out.print(obj);

	}

	private void resultadosMes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String user_role) throws IOException {
		boolean respuesta = true;//con esto comprobamos que tenemos algun resultado en el mes seleccionado
		String mes = request.getParameter("mes");
		String year = request.getParameter("year");

		String categoriasLi = "<i onclick='borrarCategoria(this)' class='material-icons crossReset'> clear </i>";
		for (int j = 0; j < Data.categorias.size(); j++) {
			categoriasLi += "<li id='"+Data.categorias.get(j).getIdCategoria()+"' class='' onclick='guardarCategoriaResultado(this)'>"+Data.categorias.get(j).getEnlace()+"</li>";		
		}

		System.out.println(year+"-"+mes);

		if(enlaces.get(0).getFecha().toString().startsWith(year+"-"+mes)) {//COMPOBAMOS QUE LA FEMCHA MESUAL COINCIDE PORQUE SINO DEBEMOS PEDIR AL SERVIDOR LOS DATOS DEL MES SELECCIONADO
			
		}else {//si el mes no coincide 
			String json = ws.getEnlaces(cliente.getIdCliente()+"", year+"-"+mes ,"","getEnlacesAnteriores","enlaces.php");
			System.out.println(json);
			String[] jsonArray = json.split(";;");

			//Esto es irrelevante, en un futuro hacer mas pruebas y ver si conviene quitar esta peticion 
			ArrayList<Foro> forosDisponibles = new Gson().fromJson(jsonArray[0], new TypeToken<List<Foro>>(){}.getType());
			this.forosDisponibles.clear();
			this.forosDisponibles = forosDisponibles;

			//parseamos los enlaces (resultados)
			ArrayList<Enlace> enlaces = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonArray[1], new TypeToken<List<Enlace>>(){}.getType());
			if(enlaces.size()>0) {
				this.enlaces.clear();
				this.enlaces = enlaces;
			}else if(enlaces.size()==0) {
				respuesta=false;
			}
			System.out.println("cambiando de mes "+enlaces.size());
		}

		
		if(respuesta){
			String urlsAAtacarLi="";
			
			String onHoverGoLink="onmouseover='hoverGoLinkIn(this)' onmouseout='hoverGoLinkOut(this)'";
			
			for (int i = 0; i < enlaces.get(0).getUrlsAAtacar().size(); i++) {
				urlsAAtacarLi += "<li onclick='enlaces_guradarDestino(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+enlaces.get(0).getUrlsAAtacar().get(i)+"</span></li>";
			}

			ob.enlacesByTipo(enlaces);
			

			for (int i = 0; i < enlaces.size(); i++) {
				Enlace e = enlaces.get(i);
				int id_resultado = Integer.parseInt(e.getIdResultado());

				//Foro utilizado para este resultado
				int posicionForo=-1;
				for (Foro f : forosDisponibles) {
					if(e.getIdForo()==f.getIdForo()) {
						posicionForo = forosDisponibles.indexOf(f);break;
					}
				}

				String claseStatus="";
				if(!e.getEnlace().trim().equalsIgnoreCase("")) claseStatus="sOK";
				else claseStatus="sPendiente";

				String claseTipo="",tipo="";
				if(e.getTipo().equalsIgnoreCase("follow")) {claseTipo="lf";tipo="follow";}
				else if(e.getTipo().equalsIgnoreCase("nofollow")) {claseTipo="lnf";tipo="nofollow";}

				//if(!user_role.equals("user_paid")) {
				//lista de categorias-----------------------------
				String nameCategoria = "Selecciona una categor&iacute;a";
				if(e.getCategoria()!=0)nameCategoria = e.getNombreCategoria(); 

				String botonDescripcion="";
				if(!e.getDescripcionForo().trim().equals("")) {
					botonDescripcion="<i onclick='enlace_openDescription(this)' class='material-icons description_enlace goLink'>notes</i>";
				}
 
				//TABLA----------------------
				out.println("<tr id='"+id_resultado+"' posicion='"+i+"'>");
				out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");

				out.println("	<td class='tdCat cCUser pr txt_center_mg_0' onclick='opentUser(this)'>");
				out.println("		<div class='tdCat tdWeb txt_center_mg_0'>");
				out.println("			<span class='tdCat' type='text'>"+e.getNameEmpleado()+"</span>");
				out.println("		</div>"); 
				out.println("	</td>");
				
				
				out.println("	<td class='tdCat cell_destino pr' onclick='openDestinos(this)'>");
				out.println("		<div class='tdCat tdWeb'>");
				out.println("			<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>"+e.getDestino()+"</span>");
				out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
				out.println(			"<i onclick='enlaces_guradarDestino(this)' class='material-icons crossReset'> clear </i>");
				out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text'  onkeypress='enlaces_nuevoDestinoEnter(event,this)' placeholder='Nuevo destino'><i onclick='enlaces_nuevoDestino(this)' class='material-icons p_a add_destinos_enlace'>add</i></div>");
				out.println(			"<ul class='slWeb pdd_v_10'>"+urlsAAtacarLi+"</ul>");
				out.println(		"</div>");
				out.println("	</td>"); 

				out.println("	<td class='tdCat cCateg pr' onclick='openCategoriaResultado(this)'>");
				out.println("		<div class='tdCat'>");
				out.println("			<span class='tdCat' data-id-categoria='"+e.getCategoria()+"'>"+nameCategoria+"</span>");
				out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		"<ul class='slCt effect7 pop_up'>"+categoriasLi+"</ul>");
				out.println("	</td>");
				out.println("	<td class='tdCat tdWeb cWeb pr' onclick='openWebResultado(this)'>");
				out.println("		<div class='tdCat tdWeb'>");
				out.println("			<span data-id-foro='"+e.getIdForo()+"' data-posicion-foro='"+posicionForo+"' data-id-categoria='"+e.getCategoria()+"'   onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb goLink'>"+e.getWebForo()+"</span>");
				out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
				out.println(			botonDescripcion); 
				out.println("		</div>");
				out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
				out.println(			"<i onclick='guardarWebResultado(this)' class='material-icons crossReset'> clear </i>");
				out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text' oninput='buscarMedio(this)' placeholder='Busca un medio'><i class='material-icons p_a lupa_medios'>search</i></div>");
				out.println(			"<ul class='slWeb'></ul>");
				out.println(		"</div>");
				out.println("		<textarea class='div_enlaces_pop_up pop_up effect7 div_enlaces_descripcion_pop_up'  wrap='hard' onclick='stopPropagation()' readonly>");
				out.println(			e.getDescripcionForo().trim().replace("<", "&lt;").replace(">", "&gt;"));
				out.println(		"</textarea>");
				out.println("	</td>");

				out.println("	<td class='cLink'>");
				out.println("		<input class='inLink' onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)' onclick='goLink(this)' "+onHoverGoLink+"  type='text' value='"+e.getEnlace()+"'>");
				out.println("	</td>");

				out.println("	<td class='cAnchor'><input class='inLink' onchange='guardarAnchor(this)' oninput='saveClient(this)' type='text' value='"+e.getAnchor()+"'></td>");
				out.println("	<td tipo='"+tipo+"' class='cTipo'><i class='material-icons "+claseTipo+"'>link</i></td>");
				out.println("</tr>");
				/*}else {

						int beneficio = cliente.getResultados().get(i).getPrecion_venta()-cliente.getResultados().get(i).getPrecio_compra();
						Double div = (beneficio*1.0/cliente.getResultados().get(i).getPrecio_compra())*100;

						//TABLA----------------------
						out.println("<tr id='"+id_resultado+"' posicion='"+i+"'>");
						out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");
						out.println("	<td class='tdCat tdWeb pago_web pr' onclick='openWebResultado(this)'>");
						out.println("		<div class='tdCat tdWeb'>");
						out.println("			<span mweb='"+mweb+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb'>"+cliente.getResultados().get(i).getWeb_foro()+"</span>");
						out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
						out.println("		</div>");
						out.println(		"<ul class='slCt slWeb effect7 pop_up'></ul>");
						out.println("	</td>");
						out.println("	<td data-paid='compra' class='cPrecio td_input_precio pr' onclick='openCoste(this)'><span>"+cliente.getResultados().get(i).getPrecio_compra()+" &euro;</span>"+"<input class='inLink paid_inputs' onchange='updatePrecio(this)' onclick='stopPropagation(this)' oninput='saveClient(this)' onkeypress='getKey(event)' type='number' value='"+cliente.getResultados().get(i).getPrecio_compra()+"'></td>");
						out.println("	<td data-paid='venta' class='cPrecio td_input_precio pr' onclick='openCoste(this)'><span>"+cliente.getResultados().get(i).getPrecion_venta()+" &euro;</span>"+"<input class='inLink paid_inputs' onchange='updatePrecio(this)' onclick='stopPropagation(this)' oninput='saveClient(this)' onkeypress='getKey(event)' type='number' value='"+cliente.getResultados().get(i).getPrecion_venta()+"'></td>");
						out.println("	<td data-paid='beneficio' class='cPrecio'>"+beneficio+" &euro;</td>");
						out.println("	<td data-paid='incremento' class='cPrecio'>"+div.intValue()+"%</td>");
						out.println("	<td class='cDest'><input class='inLink' onchange='updateDestino(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getDestino()+"'></td>");
						out.println("	<td class='cAnchor'><input class='inLink' onchange='guardarAnchor(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getAnchor()+"'></td>");
						out.println("	<td class='cLink'>");
						out.println("		<input class='inLink' onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getEnlace()+"'>");
						out.println("	</td>");
						out.println("</tr>");

					}*/
			}
		}else {
			
		}
		

	}

	private void guardarEnlaceResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user) {
		String link = request.getParameter("link");
		System.out.println("Enlace a subir:  "+link);
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int follows_done = Integer.parseInt(request.getParameter("follows_done"));
		int nofollows_done = Integer.parseInt(request.getParameter("nofollows_done"));
		int mes_js = Integer.parseInt(request.getParameter("mes"));
		int year_js = Integer.parseInt(request.getParameter("year"));

		Date date= new Date();Calendar cal = Calendar.getInstance();cal.setTime(date);
		int mes = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);

		ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
		ws.updateResultado(id_resultado+"", "hecho_por", id_user+"" , "updateResultado.php");


		if(mes==mes_js && year==year_js) {

			cliente.setFollowsDone(follows_done);
			cliente.setNofollowsDone(nofollows_done);

			System.out.println(cliente.getFollows());
			System.out.println(cliente.getNofollows());
			ws.updateCliente(cliente.getIdCliente()+"","follows_done",cliente.getFollowsDone()+"","updateCliente.php");
			ws.updateCliente(cliente.getIdCliente()+"","nofollows_done",cliente.getNofollowsDone()+"","updateCliente.php");

		}
		
		System.out.println(cliente.getFollows()-cliente.getFollowsDone()+"�");


		if(cliente.getFollows()-cliente.getFollowsDone()==0) {
			out.println("<div class='noti notiPos'><i class='material-icons lf'>done</i></div>");
		}else {
			out.println("<div class='noti'>"+(cliente.getFollows()-cliente.getFollowsDone())+"</div>");
		}

	}


	private void borrarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int id_foro_anterior = Integer.parseInt(request.getParameter("id_foro_anterior"));
		int categoria_foro_anterior = Integer.parseInt(request.getParameter("categoria_foro_anterior"));
		String medio_anterior = request.getParameter("medio_anterior");
		String descripcion_foro_anterior = request.getParameter("descripcion_foro_anterior");

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "categoria", "0" , "updateResultado.php");
		ws.updateResultado(id_resultado+"", "id_foro", "0" , "updateResultado.php");
		out.println("<span class='tdCat' data-id-categoria='0'>Selecciona una categor&iacute;a</span> <i class='material-icons arrow'>arrow_drop_down</i>");

		if(id_foro_anterior>0) {
			forosDisponibles.add(new Foro(id_foro_anterior, medio_anterior, categoria_foro_anterior, descripcion_foro_anterior));
		}
		System.out.println("Insertado");

	}
	private void guardarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "categoria", idCategoria+"" , "updateResultado.php");
		System.out.println("Insertado");
	}

	private void mostrarWebResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));

		ob.forosByWeb(forosDisponibles);
		for (Foro f : forosDisponibles) {
			if(idCategoria==0) {//si la categoria es 0 mostramos todos los enlaces 
				out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' onclick='guardarWebResultado(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");

			}else if(f.getCategoria()==idCategoria) {
				out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' onclick='guardarWebResultado(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
			}
		}
	}
	private void guardarWebResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();

		int id_categoria_tdCategoria = Integer.parseInt(request.getParameter("id_categoria_tdCategoria"));

		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int id_foro = Integer.parseInt(request.getParameter("id_foro"));//id en la base de datos del foro seleccionado
		int posicion_foro = Integer.parseInt(request.getParameter("posicion_foro"));
		int id_categoria = Integer.parseInt(request.getParameter("categoria_foro"));

		int id_foro_anterior = Integer.parseInt(request.getParameter("id_foro_anterior"));
		int posicion_foro_anterior = Integer.parseInt(request.getParameter("posicion_foro_anterior"));
		int id_categoria_anterior = Integer.parseInt(request.getParameter("categoria_foro_anterior"));
		String descripcion_foro_anterior = request.getParameter("descripcion_foro_anterior");
		String medio_anterior = request.getParameter("medio_anterior");

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "id_foro", id_foro+"" , "updateResultado.php");

		String medio ="";
		if(posicion_foro==-1 && id_categoria==0 ) 	medio = "";
		else medio = forosDisponibles.get(posicion_foro).getWebForo();

		String html="",descripcion="";
		html+="<span  data-id-foro='"+id_foro+"' data-posicion-foro='-1' data-id-categoria='"+id_categoria+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb goLink'>"+medio+"</span>";
		html+="<i class='material-icons arrow'>arrow_drop_down</i>";
		if(id_foro>0) {
			html+="<i onclick='enlace_openDescription(this)' class='material-icons description_enlace goLink'>notes</i>";
			descripcion = forosDisponibles.get(posicion_foro).getDescripcion();
		}

		if(posicion_foro!=-1) forosDisponibles.remove(posicion_foro);

		if(posicion_foro_anterior==-1 && id_foro_anterior>0) forosDisponibles.add(new Foro(id_foro_anterior, medio_anterior, id_categoria_anterior, descripcion_foro_anterior));
		ob.forosByWeb(forosDisponibles);

		if(id_categoria_tdCategoria==0) {
			System.out.println("se ha cambiado la categoria");
			ws.updateResultado(id_resultado+"", "categoria", id_categoria+"" , "updateResultado.php");
		}


		obj.put("html", html);
		obj.put("descripcion", descripcion);
		out.print(obj);

		System.out.println("Insertado");
	}
	private void buscarMedio(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String texto = request.getParameter("texto");
		int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
		for (Foro f : forosDisponibles) {
			if(id_categoria==0 && f.getWebForo().toLowerCase().contains(texto.toLowerCase())) {//si la categoria es 0 mostramos todos los enlaces 
				out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' onclick='guardarWebResultado(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
			}else if(f.getCategoria()==id_categoria && f.getWebForo().toLowerCase().contains(texto.toLowerCase())) {
				out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' onclick='guardarWebResultado(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
			}
		}
	}
	private void guradarDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String destino = request.getParameter("destino");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "destino", destino+"" , "updateResultado.php");
		System.out.println("Insertado");

	}

	private void nuevoDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		String url = request.getParameter("url");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));

		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();
		String status="",text="";


		if(url.startsWith("http://") || url.startsWith("https://")) {
			ws.updateResultado(id_resultado+"", "destino", url+"" , "updateResultado.php");
			status = "1";
		}else {
			status = "0";
		}
		obj.put("status", status);
		out.print(obj);

	}
	private void guardarAnchor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String anchor = request.getParameter("anchor");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "anchor", anchor+"" , "updateResultado.php");
		System.out.println("Insertado");
	}



	private void buscarCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {

		String k = request.getParameter("keyword").toLowerCase();
		int clienteSeleccionado = Integer.parseInt(request.getParameter("clienteSeleccionado"));
		System.out.println(k);
		String html="";
		for (int i = 0; i < clientes.size(); i++){
			if(clientes.get(i).getNombre().toLowerCase().contains(k.toLowerCase()) || clientes.get(i).getWeb().toLowerCase().contains(k.toLowerCase())) {
				String itemSleccionado="";
				if(clienteSeleccionado == clientes.get(i).getIdCliente()) {itemSleccionado="item_select";}
				html+="		<div id='"+clientes.get(i).getIdCliente()+"' onclick='enlaces_SelectClient(this.id, this)' class='item "+itemSleccionado+"'>";
				if(i!=0){html+="<div class='line'></div>";}
				html+="			<div class='itemChild";if(clientes.get(i).getEditando()==1)html+=" blur"; html+="'>";
				html+="				<div class='dominioItem'>";
				html+="					<span class='dominioItem' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+clientes.get(i).getDominio()+"</span>";
				html+="				</div>";
				html+="				<div class='nameItem sName'>"+clientes.get(i).getNombre()+"</div>";
				String opacity="";
				if(clientes.get(i).getEditando()==1){opacity="opacity_0";}
				if(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone() == 0){
					html+="		<div class='noti notiPos opacity'><i class='material-icons lf'>done</i></div>";
				}else{
					html+="		<div class='noti opacity'>"+(clientes.get(i).getFollows() - clientes.get(i).getFollowsDone())+"</div>";
				}
				if(clientes.get(i).getStatus().equals("our")){ 		
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sYS_color'>bookmark</i><div class='div_line_bookmark sYS'></div></div>";
				}
				else if(clientes.get(i).getStatus().equals("new")){ 
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sOK_color'>bookmark</i><div class='div_line_bookmark sOK'></div></div>";
				}
				html+="			</div>";
				html+="			<div class='blockClient"; if(clientes.get(i).getEditando()==1)html+="visible";html+="'><div class='lockDiv'><i class='material-icons lf blur'> lock </i></div></div>";
				html+="			<div class='pop_up_info_blocked'>";
				html+="				<div class='msg_blocked'>Editando cliente por</div>";
				html+="				<div class='msg_name_blocked'>Guillermo</div>";
				html+="				<div class='lockDiv'><i class='material-icons lf'> lock </i></div>";
				html+="				<div class='lockDiv' style='left:23px;'><i class='material-icons lf'> lock </i></div>";
				html+="			</div>";
				html+="			<div class='loader'><div class='l_d1'></div><div class='l_d2'></div><div class='l_d3'></div><div class='l_d4'></div><div class='l_d5'></div></div>";
				html+="		</div>";
			}
		}

		out.println(html+"<script type='text/javascript'>setC();</script>");
	}



}



















