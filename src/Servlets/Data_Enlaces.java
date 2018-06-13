package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Classes.Webservice;
import Objects.Cliente;
import Objects.Resultado;
import Objects.Gson.CategoriaGson;
import Objects.Gson.ClienteGson;
import Objects.Gson.Enlace;
import Objects.Gson.ForoGson;
import Objects.Gson.ResultadoGson;
import Objects.Gson.TematicaGson;

@WebServlet("/Data_Enlaces")
public class Data_Enlaces extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Webservice ws = new Webservice();
	private Cliente cliente = new Cliente();
	private ArrayList<ForoGson> foros = new ArrayList<ForoGson>();
	private ArrayList<CategoriaGson> categorias = new ArrayList<CategoriaGson>();
	private ArrayList<TematicaGson> tematicas = new ArrayList<TematicaGson>();
	private ArrayList<ClienteGson> clientes = new ArrayList<ClienteGson>();
	
	private ArrayList<Enlace> enlaces =  new ArrayList<Enlace>();
       
    public Data_Enlaces() {super();}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();
		
		//revisar codigooooooooo-----------------------------------------------------
		//ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");

		
		/*
		//Obtenemos los clientes del usuario ------------------------------------------------------------------------
		String json = ws.getClientsByUser("["+id_user+"]", role, "getClientsByUser.php");
		System.out.println(json);
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		this.clientes.clear();
		this.clientes = clientesGson;

		//obtenemos las tematicas
		json = ws.getJSON("getTematica.php");
		gson = new Gson();
		ArrayList<TematicaGson> tematicasGson = gson.fromJson(json, new TypeToken<List<TematicaGson>>(){}.getType());
		this.tematicas = tematicasGson;

		obtenemosCategorias();
		obtenemosForos();
		//revisar codigooooooooo-----------------------------------------------------////////////////////////////////////
		 */

		String metodo = request.getParameter("metodo");
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		
		if(metodo.equals("enlaces_SelectClient")) { 
			try {selectClient(request, response, out,id_user, role);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_CheckClients")) {
			try {checkClients(request, response, out,id_user,role);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_ResultadosMes")) {
			resultadosMes(request, response, out, role);
		}else if(metodo.equals("guardarEnlaceResultado")) {
			guardarEnlaceResultado(request, response, out,id_user);
		}else if(metodo.equals("mostrarWebResultado")) {
			mostrarWebResultado(request, response, out);
		}else if(metodo.equals("borrarCategoriaResultado")) {
			borrarCategoriaResultado(request, response, out);
		}else if(metodo.equals("cha")) {
			guardarAnchor(request, response, out);
		}else if(metodo.equals("guardarCategoriaResultado")) {
			guardarCategoriaResultado(request, response, out);
		}
		
	}
	
	private void selectClient(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_empleado, String user_role) throws IOException, ParseException {
		String id_client = request.getParameter("id_client");
		String fecha = request.getParameter("fecha");

		if(fecha.contains("undefined")) {//cuando obtenemos el año por javascript nos devueve siempre undefined si estamos en el mes correspondiente al dia de hoy
			Date today = new Date(); 
			Calendar cal = Calendar.getInstance(); cal.setTime(today);
			int month = cal.get(Calendar.MONTH)+1, year = cal.get(Calendar.YEAR);
			String mes = month+""; if(month<10) mes= "0"+mes;
			fecha = year+"-"+mes;
		}
		
		String json = ws.getEnlaces(id_client,id_empleado+"", fecha ,"getEnlaces","enlaces.php");
		System.out.println(json);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ArrayList<Enlace> enlaces = gson.fromJson(json, new TypeToken<List<Enlace>>(){}.getType());
		for (Enlace e : enlaces) {
			
			if(e.getDisponibilidad()==1) {
				this.enlaces.clear();
				this.enlaces = enlaces;
				mostrarResultados(request, response, out, user_role);
				break;
			}else {
				System.out.println("Estas bloqueado");
			}
			
			
		}

		//obtenemos la disponibilidad del cliente clickado y los resultados de el
		/*String json = ws.getClientById(id_client,user_role, "getClientById.php");
		System.out.println("Hola"+json);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ArrayList<ResultadoGson> resultadosGson = gson.fromJson(json, new TypeToken<List<ResultadoGson>>(){}.getType());

		Cliente cliente = null;
		if(resultadosGson.size()==0) {
			String jsonCliente = ws.getClientById(id_client, user_role, "getClient.php");
			gson = new Gson();
			ArrayList<ClienteGson> resultadosGsonClientes = gson.fromJson(jsonCliente, new TypeToken<List<ClienteGson>>(){}.getType());
			int x = 0;
			for (ClienteGson r : resultadosGsonClientes) {
				if(x==0 && r.getEditando() == 0) {
					cliente = new Cliente(r.getIdCliente(), r.getWeb(), r.getNombre(), r.getServicio(), r.getFollows(), r.getNofollows(), r.getAnchor(), Integer.parseInt(r.getBlog()), r.getIdioma(), r.getFollowsDone(), r.getNofollowsDone(), r.getLinkbuilder(), r.getEditando(),r.getEnlacesDePago(), new ArrayList<Resultado>(), new ArrayList<ForoGson>());
					ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");
					ws.actualizarEditando(1, id_user, r.getIdCliente(), "actualizarEditando.php");x=1;
				}
				if(r.getEditando() != 0) break;
			}
		}else {

			int x = 0;
			for (ResultadoGson r : resultadosGson) {
				if(x==0 && r.getEditando() == 0) {
					cliente = new Cliente(r.getIdCliente(), r.getWeb(), r.getNombre(), r.getServicio(), r.getFollows(), r.getNofollows(), r.getAnchorCliente(), r.getBlog(), r.getIdioma(), r.getFollowsDone(), r.getNofollowsDone(), r.getLinkbuilder(), r.getEditando(), r.getEnlaces_de_pago(),new ArrayList<Resultado>(), new ArrayList<ForoGson>());
					//desbilitamos todos los clientes que estan bloqueados por este usuario
					ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");
					//crear peticion para poner el cliente en edicion = 1
					json = ws.actualizarEditando(1, id_user, r.getIdCliente(), "actualizarEditando.php");
					x=1;
				}
				if(r.getEditando() == 0) {
					cliente.getResultados().add(new Resultado(r.getIdResultado(), r.getIdForo(), r.getEnlace(), r.getFecha(), r.getTipoRes(), r.getDestino(), r.getCategoriaResultado(), r.getEstado(), r.getAnchorR(), r.getWebForo(), r.getPrecio_compra(), r.getPrecio_venta()));
					cliente.getForos().add(new ForoGson(r.getIdForo(), r.getWebForo(), r.getTipoForo(), r.getDR(), r.getDA(), r.getTematica(), r.getDestino(), r.getCategoriaResultado(), r.getReqAprobacion(), r.getReqRegistro(), r.getApareceFecha(), r.getReutilizable()));
				}else {
					break;
				}

			}
		}
		//si es null es que un usuario ya esta editando este cliente
		if(cliente!=null) {
			this.cliente = cliente;
			mostrarResultados(request, response, out, user_role);
		}*/

	}
	
	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out,String empleado_role) throws IOException, ParseException {
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
		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>"+cliente.getNombre()+"</div>");
		out.println("	<div class='urlClient'>"+cliente.getWeb()+"</div>");
		//out.println("	<input id='dateSelected' onchange='changeMonth()' class='inCal' data-lock='to' type='text' data-lang='es' data-min-year='2017'  data-large-mode='false' data-format='F' data-theme='calendar'/>");
		//out.println("	<script>$('.datedropper').remove();$('#dateSelected').dateDropper();</script>");
		out.println("</div>");

		//barra de herramientas
		out.println("<div class='ctoolbar'><div id='cGuardar' class='zoom'>guardar</div></div>");

		out.println("<div class='keywordsClient'>");
		//out.println("	<div class='titleTable'>Keywords<div class='horDiv'></div></div>");
		out.println("	<div id='results_Client' class='contentTable'>");
		//tabla
		out.println("		<table id='tClientes' class='table'>");
		if(!empleado_role.equals("user_paid")) {
			out.println("			<thead><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cLink'>Link</th><th class='cabeceraTable cCateg'>Categoria</th><th class='cabeceraTable cWeb'>Web</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>");
		}else {
			out.println("			<thead><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable pago_web'>Medio</th><th class='cabeceraTable cPrecio'>Coste</th><th class='cabeceraTable cPrecio'>Venta</th><th class='cabeceraTable cPrecio cBeneficio'>Beneficio</th><th class='cabeceraTable cPrecio c_incremento'>Incremento</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cLink'>Link</th></tr></thead>");
		}
		out.println("			<tbody>");
		//solo seleccionamos los resultados de la fecha deseada
		
		/*ArrayList<Resultado> resultados = new ArrayList<Resultado>();
		for (int i = 0; i < cliente.getResultados().size(); i++) {
			//System.out.println(cliente.getResultados().get(i).toString());
			String fecha[] = cliente.getResultados().get(i).getFecha().toString().split("-");
			int y = Integer.parseInt(fecha[0]);
			int m = Integer.parseInt(fecha[1]);
			if(mes == m && year == y) {
				resultados.add(cliente.getResultados().get(i));
			}
		}*/
		//----------------------------------------------------------
		//Con esto creamos los resultados nuevos de cada mes, cuando los creamos pedimos al webservice que nos devuelva los resultados con sus id's para poder asignarselos a nuetro array de clientes
		/*System.out.println(resultados.size()+" - "+empleado_role);
		if(resultados.size()==0 && !user_role.equals("paid_user")) {
			for (int i = 0; i < cliente.getFollows(); i++) {
				int id_cliente = cliente.getId_cliente();
				String tipo ="FOLLOW";
				ws.insertResultadoVacio(id_cliente, tipo, "insertResultadoVacio.php");
				System.out.println("Insertando FOLLOW");
			}
			for (int i = 0; i < cliente.getNofollows(); i++) {
				int id_cliente = cliente.getId_cliente();
				String tipo ="NOFOLLOW";
				ws.insertResultadoVacio(id_cliente, tipo, "insertResultadoVacio.php");
				System.out.println("Insertando NOFOLLOW");
			}
			String fecha = year+"-"+mes+"-"+day;
			String json = ws.getResultVacios(cliente.getId_cliente(), fecha, "getResultVacios.php");
			System.out.println("["+json );
			Object jsonObject =JSONValue.parse(json.toString());
			JSONArray arrayData = (JSONArray)jsonObject;
			for(int i=0;i<arrayData.size();i++){
				JSONObject row =(JSONObject)arrayData.get(i);
				//para el array de resultados del cliente
				int id_resultado = Integer.parseInt(row.get("id_resultado").toString());
				int id_foro = Integer.parseInt(row.get("id_foro").toString());
				String enlace = (String) row.get("enlace");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateInString = (String) row.get("fecha");
				Date dia = formatter.parse(dateInString);
				java.sql.Date dateR = new java.sql.Date(dia.getTime());
				String tipo = (String) row.get("tipo");
				String destino = (String) row.get("destino");
				int categoria = Integer.parseInt(row.get("categoria").toString());
				String estado = (String) row.get("estado");
				String anchorR = (String) row.get("anchor");
				int precio_compra = Integer.parseInt(row.get("precio_compra").toString());
				int precio_venta = Integer.parseInt(row.get("precio_venta").toString());

				cliente.getResultados().add(new Resultado(id_resultado, id_foro, enlace, dateR, tipo, destino, categoria, estado, anchorR,"",precio_compra ,precio_venta));
			}
		}else {
			System.out.println("curso del programa normal");
		}*/
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");

		out.println("</div>");
	}
	
	private void checkClients(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user,String role) throws IOException, ParseException {
		int id_client = Integer.parseInt(request.getParameter("id_client"));
		//int id_cliente_before = Integer.parseInt(request.getParameter("id_cliente_before"));
		//Obtenemos los clientes del usuario ------------------------------------------------------------------------
		String json = ws.getClientsByUser("["+id_user+"]",role, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		this.clientes.clear();
		this.clientes = clientesGson;
		//-----------------------------------------------------------------------------------------------------------
		int inicio = 0;
		String clases = "",clases2="";

		for (ClienteGson c : clientesGson) {
			if(c.getEditando()==1 && c.getUserEditando()!=id_user) {
				clases="itemChild blur";
				clases2="blockClient visible";
			}
			else {
				clases ="itemChild";
				clases2="blockClient";
			}

			out.println("<div id='"+c.getIdCliente()+"' onclick='enlaces_SelectClient(this.id)' class='item'>");
			if(inicio!=0) {
				out.println(	"<div class='line'></div>");
			}
			out.println(		"<div class='"+clases+"'>");
			out.println(			"<div class='nameItem'>");
			out.println(				"<span class='nameItem sName' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  >"+c.getNombre()+"</span>");
			out.println(			"</div>");
			out.println(			"<div class='dominioItem'>"+c.getWeb()+"</div>");
			if(c.getEditando()==0 || c.getUserEditando() == id_user) {
				if(c.getFollows()-c.getFollowsDone()==0) {
					out.println("<div class='noti notiPos'><i class='material-icons lf'>done</i></div>");
				}else {
					out.println("<div class='noti'>"+(c.getFollows()-c.getFollowsDone())+"</div>");
				}
			}	
			out.println(		"</div>");
			out.println("	<div class='"+clases2+"'><div class='lockDiv'><i class='material-icons lf blur'> lock </i></div></div>");
			out.println("</div>");
			inicio++;
		}
	}
	
	private void resultadosMes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String user_role) throws IOException {
		int mes = Integer.parseInt(request.getParameter("mes"));
		int year = Integer.parseInt(request.getParameter("year"));
		//out = response.getWriter();

		for (int i = 0; i < enlaces.size(); i++) {
			Enlace e = enlaces.get(i);
			int id_resultado = Integer.parseInt(e.getIdResultado());

			String fecha[] = e.getFecha().toString().split("-");
			int y = Integer.parseInt(fecha[0]);
			int m = Integer.parseInt(fecha[1]);

			if(mes == m && year == y) {

				//Foro utilizado para este resultado
				int mweb = cliente.getResultados().get(i).getId_foro();
				String claseStatus="";
				if(!cliente.getResultados().get(i).getEnlace().trim().equalsIgnoreCase("")) claseStatus="sOK";
				else claseStatus="sPendiente";

				String claseTipo="",tipo="";
				if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("follow")) {claseTipo="lf";tipo="follow";}
				else if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("nofollow")) {claseTipo="lnf";tipo="nofollow";}

				if(!user_role.equals("user_paid")) {
					//lista de categorias---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					String opCategoria = "Selecciona una categor&iacute;a";
					String htmlCategorias = "";
					for (int j = 0; j < categorias.size(); j++) {
						if(cliente.getResultados().get(i).getCategoria() == categorias.get(j).getIdCategoria() && categorias.get(j).getIdCategoria()!=0) {
							opCategoria = categorias.get(j).getEnlace();
							htmlCategorias += "			<li id='"+categorias.get(j).getIdCategoria()+"' class='liActive' onclick='guardarCategoriaResultado(this)'>"+categorias.get(j).getEnlace()+"</li>";
						}else if(categorias.get(j).getIdCategoria()!=0) {
							htmlCategorias += "			<li id='"+categorias.get(j).getIdCategoria()+"' onclick='guardarCategoriaResultado(this)'>"+categorias.get(j).getEnlace()+"</li>";
						}			
					}

					//TABLA----------------------
					out.println("<tr id='"+id_resultado+"' posicion='"+i+"'>");
					out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");
					out.println("	<td class='cLink'>");
					out.println("		<input class='inLink' onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getEnlace()+"'>");
					out.println("	</td>");
					out.println("	<td class='tdCat cCateg pr' onclick='openCategoriaResultado(this)'>");
					out.println("		<div class='tdCat'>");
					out.println("			<span class='tdCat'>"+opCategoria+"</span>");
					out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
					out.println("		</div>");
					out.println(		"<ul class='slCt effect7 pop_up'><i onclick='borrarCategoria(this)' class='material-icons crossReset'> clear </i>"+htmlCategorias+"</ul>");
					out.println("	</td>");
					out.println("	<td class='tdCat tdWeb cWeb pr' onclick='openWebResultado(this)'>");
					out.println("		<div class='tdCat tdWeb'>");
					out.println("			<span mweb='"+mweb+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb'>"+cliente.getResultados().get(i).getWeb_foro()+"</span>");
					out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
					out.println("		</div>");
					out.println(		"<ul class='slCt slWeb effect7 pop_up'></ul>");
					out.println("	</td>");
					
					out.println("	<td class='tdCat cell_destino pr' onclick='openDestinos(this)'>");
					//out.println("	<td class='cDest pr'  onclick='openDestinosEnlace(this)'>");//destinoooo
					//out.println("		<input class='inLink' onchange='updateDestino(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getDestino()+"'>");
					out.println("		<div data-id='lista_destinos' class='div_destinos pop_up effect7 pop_up_move2left'>");
					out.println("			<ul class='scroll_115 pdd_v_12 inner_pop_up'>");
					out.println("				<li class='pdd_h_17 pr inner_pop_up' ><span>"+"HOMEEEEEE"+"</span></li>");
					out.println("			</ul>");
					out.println("		</div>");
					out.println("	</td>");

					
					out.println("	<td class='cAnchor'><input class='inLink' onchange='updateAnchor(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getAnchor()+"'></td>");
					out.println("	<td tipo='"+tipo+"' class='cTipo'><i class='material-icons "+claseTipo+"'>link</i></td>");
					out.println("</tr>");
				}else {

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
					out.println("	<td class='cAnchor'><input class='inLink' onchange='updateAnchor(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getAnchor()+"'></td>");
					out.println("	<td class='cLink'>");
					out.println("		<input class='inLink' onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getEnlace()+"'>");
					out.println("	</td>");
					out.println("</tr>");

				}


			}
		}
	}

	private void guardarEnlaceResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user) {
		String link = request.getParameter("link");
		System.out.println("Enlace a subir:  "+link);
		String tipo = request.getParameter("tipo");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int posicion = Integer.parseInt(request.getParameter("posicion"));
		int follows_done = Integer.parseInt(request.getParameter("follows_done"));
		int nofollows_done = Integer.parseInt(request.getParameter("nofollows_done"));
		int mes_js = Integer.parseInt(request.getParameter("mes"));
		int year_js = Integer.parseInt(request.getParameter("year"));

		Date date= new Date();Calendar cal = Calendar.getInstance();cal.setTime(date);
		int mes = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);

		String json = ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
		System.out.println("->"+json);
		ws.updateResultado(id_resultado+"", "hecho_por", id_user+"" , "updateResultado.php");
		cliente.getResultados().get(posicion).setEnlace(link);

		if(mes==mes_js && year==year_js) {
			cliente.setFollows_done(follows_done);
			cliente.setNofollows_done(nofollows_done);
			ws.updateCliente(cliente.getId_cliente()+"","follows_done",cliente.getFollows_done()+"","updateCliente.php");
			ws.updateCliente(cliente.getId_cliente()+"","nofollows_done",cliente.getNofollows_done()+"","updateCliente.php");
		}

		out.println("<div class='nameItem nameItem_select'>");
		out.println(	"<span class='nameItem sName nameItem_select' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+cliente.getNombre()+"</span>");
		out.println("</div>");
		out.println("<div class='dominioItem nameItem_select'>"+cliente.getWeb()+"</div>");

		if(cliente.getFollows()-cliente.getFollows_done()==0) {
			out.println("<div class='noti notiPos'><i class='material-icons lf'>done</i></div>");
		}else {
			out.println("<div class='noti'>"+(cliente.getFollows()-cliente.getFollows_done())+"</div>");
		}

	}
	
	//Alberto 
		private void mostrarWebResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
			String forosUsados="";
			for (ForoGson foro : cliente.getForos()) {forosUsados = forosUsados + "["+foro.getIdForo()+"]";}
			System.out.println("$"+forosUsados);
			out.println("<i onclick='guardarWebResultado(this)' class='material-icons crossReset'> clear </i>");
			for (int i = 0; i < foros.size(); i++) {
				String id_foro = "["+foros.get(i).getIdForo()+"]"; 
				if(foros.get(i).getCategoria()==idCategoria && !forosUsados.contains(id_foro)) {
					out.println("<li id='"+foros.get(i).getIdForo()+"' posicion='"+i+"' onclick='guardarWebResultado(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+foros.get(i).getWebForo()+"</span></li>");
				}
			}

			System.out.println();
		}

		private void borrarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
			int posicion = Integer.parseInt(request.getParameter("posicion"));

			//Aplicamos los cambios en la bbdd y en el array
			ws.updateResultado(id_resultado+"", "categoria", "0" , "updateResultado.php");
			cliente.getResultados().get(posicion).setCategoria(0);
			System.out.println("Insertado");

		}
		private void guardarAnchor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			String anchor = request.getParameter("anchor");
			int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));

			ws.updateResultado(id_resultado+"", "anchor", anchor+"" , "updateResultado.php");
			for (int i = 0,j = cliente.getResultados().size()-1; i < cliente.getResultados().size(); i++, j--) {
				if(cliente.getResultados().get(i).getId_resultado() == id_resultado) {
					cliente.getResultados().get(i).setAnchor(anchor); i = cliente.getResultados().size();
				}else if(cliente.getResultados().get(j).getId_resultado() == id_resultado) {
					cliente.getResultados().get(j).setAnchor(anchor); i = cliente.getResultados().size();
				}
			}

		}
		private void guardarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
			int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
			int posicion = Integer.parseInt(request.getParameter("posicion"));

			//Aplicamos los cambios en la bbdd y en el array
			ws.updateResultado(id_resultado+"", "categoria", idCategoria+"" , "updateResultado.php");
			cliente.getResultados().get(posicion).setCategoria(idCategoria);
			System.out.println("Insertado");
		}
		private void obtenemosCategorias() {
			//ontenemos todas las categorias
			String json = ws.getJSON("getCategorias.php");
			Gson gson = new Gson();
			ArrayList<CategoriaGson> categoriasGson = gson.fromJson(json, new TypeToken<List<CategoriaGson>>(){}.getType());
			this.categorias.clear();
			this.categorias = categoriasGson;
		}
		private void obtenemosForos() {
			//obtenemos todos los foros
			String json = ws.getJSON("getForos.php");
			Gson gson = new Gson();
			ArrayList<ForoGson> forosGson = gson.fromJson(json, new TypeToken<List<ForoGson>>(){}.getType());
			this.foros.clear();
			this.foros = forosGson;

		}
}
