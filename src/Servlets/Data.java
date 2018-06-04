package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Classes.ReadFactura;
import Classes.Webservice;
import Objects.Categoria;
import Objects.Cliente;
import Objects.Foro;
import Objects.Resultado;
import Objects.Tematica;
import Objects.Gson.CategoriaGson;
import Objects.Gson.ClienteGson;
import Objects.Gson.ForoGson;
import Objects.Gson.TematicaGson;
import Objects.Gson.UsuarioGson;
import Objects.Gson.ResultadoGson;


@WebServlet("/Data")
@MultipartConfig
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Webservice ws = new Webservice();

	private ArrayList<ClienteGson> clientes = new ArrayList<ClienteGson>();
	private Cliente cliente = new Cliente();
	private ArrayList<ForoGson> foros = new ArrayList<ForoGson>();
	private ArrayList<CategoriaGson> categorias = new ArrayList<CategoriaGson>();
	private ArrayList<TematicaGson> tematicas = new ArrayList<TematicaGson>();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		if(session.getAttribute("name_user") != null) {
			int id = Integer.parseInt(session.getAttribute("id_user").toString());
			String name = session.getAttribute("name_user").toString();
			String role = session.getAttribute("role_user").toString();
			try {cargarPage(request, response, id, name,role);} catch (ParseException e) {e.printStackTrace();}
		}else {
			response.sendRedirect("Login");
		}
	}

	private void cargarPage(HttpServletRequest request, HttpServletResponse response, int id_user, String name_user, String role) throws ServletException, IOException, ParseException {
		System.out.println(id_user+" "+name_user);
		ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");

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

		String f = name_user.substring(0, 1).toUpperCase();name_user = f + name_user.substring(1,name_user.length()).toLowerCase();

		request.setAttribute("clientes", clientesGson);//pasamos la lista de clientes
		request.setAttribute("name_user", name_user);

		request.getRequestDispatcher("Data.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();

		String metodo = request.getParameter("metodo");
		System.out.println(metodo);

		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		out = response.getWriter();

		if(metodo.equals("ckcs")) {
			try {checkClients(request, response, out,id_user,role);} catch (ParseException e) {}
		}else if(metodo.equals("ckc")) { 
			try {checkClientEdition(request, response, out,id_user, role);} catch (ParseException e) {}
		}else if (metodo.equals("lkc")) {
			try {mostrarResultados(request, response, out, role);} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("chv")) {
			morstarResultadosMes(request, response, out, role);
		}else if(metodo.equals("guardarCategoriaResultado")) {
			guardarCategoriaResultado(request, response, out);
		}else if(metodo.equals("mostrarWebResultado")) {
			mostrarWebResultado(request, response, out);
		}else if(metodo.equals("guardarWebResultado")) {
			guardarWebResultado(request, response, out);
		}else if(metodo.equals("guardarEnlaceResultado")) {
			guardarEnlaceResultado(request, response, out,id_user);
		}else if(metodo.equals("chd")) {
			guardarDestino(request, response, out);
		}else if(metodo.equals("cha")) {
			guardarAnchor(request, response, out);
		}else if(metodo.equals("guardarForoCompleto")) {
			guardarForoCompleto(request, response);
		}else if(metodo.equals("borrarCategoriaResultado")) {
			borrarCategoriaResultado(request, response, out);
		}
		//daniii
		else if(metodo.equals("cats")) {
			mostrarCategorias(request, response, out);
		}else if(metodo.equals("selectCat")) {
			mostrarForos(request, response, out);
		}else if(metodo.equals("guardarForo")) {
			guardarForo(request, response, out);
		}
		else if(metodo.equals("scs")) {
			mostrarClientesInput(request, response, out);
		}else if(metodo.equals("cleanBlocksUser")) {
			ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");
			for (ClienteGson c : this.clientes) {
				c.setEditando(0);
			}
		}
		//venta clientes
		else if(metodo.equals("ventanaClientes")) {
			try {mostrarVentanaClientes(request, response, out, id_user,role);} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("guardarValoresCliente")) {
			guardarValoresCliente(request, response, out);
		}else if(metodo.equals("guardarNuevoCliente")) {
			guardarNuevoCliente(request, response);
		}else if(metodo.equals("eliminarCliente")) {
			eliminarCliente(request, response, out);
		}
		
		//anadir facturas de los enlaces
		else if(metodo.equals("uploadFactura")) {
			System.out.println("subiendo fichero");
			subirNuevaFactura(request, response, id_user);
		}
	}

	

	private void borrarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int posicion = Integer.parseInt(request.getParameter("posicion"));

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "categoria", "0" , "updateResultado.php");
		cliente.getResultados().get(posicion).setCategoria(0);
		System.out.println("Insertado");

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

			out.println("<div id='"+c.getIdCliente()+"' onclick='selectClient(this.id)' class='item'>");
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
	private void checkClientEdition(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role) throws IOException, ParseException {
		int id_client = Integer.parseInt(request.getParameter("id_client"));
		System.out.println(id_client);

		//obtenemos la disponibilidad del cliente clickado y los resultados de el
		String json = ws.getClientById(id_client,user_role, "getClientById.php");
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
		}

	}
	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out,String user_role) throws IOException, ParseException {
		//obtenemosForos();
		//int posicion = Integer.parseInt(request.getParameter("posicion"));
		Date date= new Date();Calendar cal = Calendar.getInstance();cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		//con esto mantenemos la fecha seleccionada aunque cambiemos de cliente
		/*try {
			mes = Integer.parseInt(request.getParameter("mes"));
			year = Integer.parseInt(request.getParameter("year"));
		} catch (Exception e) {}
		 */

		out = response.getWriter();
		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>"+cliente.getNombre()+"</div>");
		out.println("	<div class='urlClient'>"+cliente.getWeb()+"</div>");
		out.println("	<input id='dateSelected' onchange='changeMonth()' class='inCal' data-lock='to' type='text' data-lang='es' data-min-year='2017'  data-large-mode='false' data-format='F' data-theme='calendar'/>");
		out.println("	<script>$('.datedropper').remove();$('#dateSelected').dateDropper();</script>");
		out.println("</div>");

		//barra de herramientas
		out.println("<div class='ctoolbar'><div id='cGuardar' class='zoom'>guardar</div></div>");

		out.println("<div class='keywordsClient'>");
		//out.println("	<div class='titleTable'>Keywords<div class='horDiv'></div></div>");
		out.println("	<div id='results_Client' class='contentTable'>");
		//tabla
		out.println("		<table id='tClientes' class='table'>");
		if(!user_role.equals("user_paid")) {
			out.println("			<thead><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cLink'>Link</th><th class='cabeceraTable cCateg'>Categoria</th><th class='cabeceraTable cWeb'>Web</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>");
		}else {
			out.println("			<thead><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable pago_web'>Web</th><th class='cabeceraTable cPrecio'>Coste</th><th class='cabeceraTable cPrecio'>Venta</th><th class='cabeceraTable cPrecio cBeneficio'>Beneficio</th><th class='cabeceraTable cPrecio c_incremento'>Incremento</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cLink'>Link</th></tr></thead>");
		}
		out.println("			<tbody>");

		//solo seleccionamos los resultados de la fecha deseada
		ArrayList<Resultado> resultados = new ArrayList<Resultado>();
		for (int i = 0; i < cliente.getResultados().size(); i++) {
			//System.out.println(cliente.getResultados().get(i).toString());
			String fecha[] = cliente.getResultados().get(i).getFecha().toString().split("-");
			int y = Integer.parseInt(fecha[0]);
			int m = Integer.parseInt(fecha[1]);
			if(mes == m && year == y) {
				resultados.add(cliente.getResultados().get(i));
			}
		}
		//----------------------------------------------------------
		//Con esto creamos los resultados nuevos de cada mes, cuando los creamos pedimos al webservice que nos devuelva los resultados con sus id's para poder asignarselos a nuetro array de clientes
		System.out.println(resultados.size()+" - "+user_role);
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
		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");

		out.println("</div>");
	}
	private void morstarResultadosMes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String user_role) throws IOException {
		int mes = Integer.parseInt(request.getParameter("mes"));
		int year = Integer.parseInt(request.getParameter("year"));
		out = response.getWriter();

		for (int i = 0; i < cliente.getResultados().size(); i++) {

			int id_resultado = cliente.getResultados().get(i).getId_resultado();

			String fecha[] = cliente.getResultados().get(i).getFecha().toString().split("-");
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
					out.println(		"<ul class='slCt effect7'><i onclick='borrarCategoria(this)' class='material-icons crossReset'> clear </i>"+htmlCategorias+"</ul>");
					out.println("	</td>");
					out.println("	<td class='tdCat tdWeb cWeb pr' onclick='openWebResultado(this)'>");
					out.println("		<div class='tdCat tdWeb'>");
					out.println("			<span mweb='"+mweb+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb'>"+cliente.getResultados().get(i).getWeb_foro()+"</span>");
					out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
					out.println("		</div>");
					out.println(		"<ul class='slCt slWeb effect7'></ul>");
					out.println("	</td>");
					out.println("	<td class='cDest'><input class='inLink' onchange='updateDestino(this)' oninput='saveClient(this)' type='text' value='"+cliente.getResultados().get(i).getDestino()+"'></td>");
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
					out.println(		"<ul class='slCt slWeb effect7'></ul>");
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
	private void guardarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int posicion = Integer.parseInt(request.getParameter("posicion"));

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "categoria", idCategoria+"" , "updateResultado.php");
		cliente.getResultados().get(posicion).setCategoria(idCategoria);
		System.out.println("Insertado");
	}
	private void guardarEnlaceResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user) {
		String link = request.getParameter("link");
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

		ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
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
	private void guardarDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String destino = request.getParameter("destino");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));

		ws.updateResultado(id_resultado+"", "destino", destino+"" , "updateResultado.php");
		for (int i = 0,j = cliente.getResultados().size()-1; i < cliente.getResultados().size(); i++, j--) {
			if(cliente.getResultados().get(i).getId_resultado() == id_resultado) {
				cliente.getResultados().get(i).setDestino(destino); i = cliente.getResultados().size();
			}else if(cliente.getResultados().get(j).getId_resultado() == id_resultado) {
				cliente.getResultados().get(j).setDestino(destino); i = cliente.getResultados().size();
			}
		}

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


	//Dani
	private void mostrarCategorias(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {

		obtenemosCategorias();

		out = response.getWriter();
		out.println("	<div class='titleCategory'><div class='titleInner'>Categorias<div class='horDiv wa'><div id='addK' class='addK'><i class='material-icons addKi'>add</i></div><div onclick='searchKey(event)'><div id='ipkey' class='srchI'><i class='material-icons addKi'>search</i><input id='searchK' class='searchI' type='text' oninput='searchK()'></div></div></div></div></div>");
		out.println("	<div class='infoCategory'><div class='info'>"+categorias.size()+" Categorias</div></div>");
		out.println("		<div id='lkkI' class='listItems'>");

		for (int i = 1; i < categorias.size(); i++) {
			if(i==1) {out.println("<div id='"+categorias.get(i).getIdCategoria()+"' posicion='"+i+"' onclick='selectCategoria(this)' class='item'><div class='itemChild childKey'><div class='nameItem nameKey'>"+categorias.get(i).getEnlace()+"</div></div></div>");
			}else {out.println("<div id='"+categorias.get(i).getIdCategoria()+"' posicion='"+i+"' onclick='selectCategoria(this)' class='item'><div class='line'></div><div class='itemChild childKey'><div class='nameItem nameKey'>"+categorias.get(i).getEnlace()+"</div></div></div>");}
		}
		out.println("		</div>");
	}
	private void guardarForo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {

		String valor = request.getParameter("valor");
		String id_foro = request.getParameter("id_foro");
		String campo = request.getParameter("campo");

		//estos if controlaran si son el valor es un boolean
		if(valor.equalsIgnoreCase("true")) valor="1";
		else if(valor.equalsIgnoreCase("false"))valor="0";

		ws.updateForo(id_foro, campo, valor, "updateForo.php");
		System.out.println("Insertado en la BBDD");
	}
	@SuppressWarnings("unchecked")
	private void guardarForoCompleto(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
		String web = request.getParameter("web");
		String dr = request.getParameter("dr");
		String da = request.getParameter("da");
		String descripcion = request.getParameter("descripcion");
		String reutilizable = request.getParameter("reutilizable");
		String tipo = request.getParameter("tipo");
		String aprobacion = request.getParameter("aprobacion");
		String registro = request.getParameter("registro");
		String fecha = request.getParameter("fecha");
		String tematica = request.getParameter("tematica");
		String categoria = request.getParameter("categoria");
		
		
		//comprobamos que el medio no se encuentra ya en la base de datos
		String dominio = web.replace("http://", "").replace("https://","").replace("www.","");
		dominio = dominio.substring(0, dominio.indexOf("."));
		String json = ws.getForoByPieceDominio(dominio, "getForoByPieceDominio.php");
		System.out.println(web+" -> "+json);
		Gson gson = new Gson();
		ArrayList<ForoGson> forosGson = gson.fromJson(json, new TypeToken<List<ForoGson>>(){}.getType());

		boolean coincidenciaExacta =false;
		String coincidenciaParcial="";
		for (ForoGson f : forosGson) {
			if(f.getWebForo().equals(web)) {coincidenciaExacta=true;break;
			}else {coincidenciaParcial=f.getWebForo();}
		}
		response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    JSONObject obj = new JSONObject();
	    String status="",text="";
		if(forosGson.size()==0) {//si esto es igual a 0 significa que en la bbdd no hay ningun cliente con este dominio
			status="0";
			System.out.println("Insertada");
			ws.insertWebs(web, dr, da, descripcion, reutilizable, tipo, aprobacion, registro, fecha, tematica, categoria, "insertWebs.php");
		}else {
			if(coincidenciaExacta) {	status="1"; text="Este medio ya existe";}
			else {						status="2"; text="Coincidencia parcial en el dominio con el medio:  ";}
		}
		obj.put("status", status);obj.put("text", text);obj.put("c", coincidenciaParcial);out.print(obj);

		
	}
	private void mostrarForos(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {

		int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
		int posicion = Integer.parseInt(request.getParameter("posicion"));

		//obtenemos los foros de la categoria seleccionada
		String json = ws.getForosByCategoria(id_categoria+"", "getForosByCategoria.php");
		Gson gson = new Gson();
		ArrayList<ForoGson> foros = gson.fromJson(json, new TypeToken<List<ForoGson>>(){}.getType());

		
		String form ="	<form class='formNewFactura' name='uploadFactura' method='post' action='Data' enctype='multipart/form-data' autocomplete='off'>" + 
				"			<label class='fileContainer'>" + 
				"				<input onchange='uploadExcelFactura(this)' type='file' class='inputAddFactura' name='excelFactura' id='excelFactura' value=''/>" + 
				"			</label>" + 
				"			<input type='hidden' name='nombre' value='' />" + 
				"			<input type='hidden' name='metodo' value='uploadFactura' />" + 
				"			<i class='material-icons'>file_upload</i>" + 
				"		</form>";
		
		
		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>"+categorias.get(posicion).getEnlace()+"</div>");
		out.println("	<div class='btnAdd' onclick='openNewWeb(this)'>Nueva web</div>");
		out.println("</div>");
		out.println("<div class='ctoolbar'>"+form+"<div id='websGuardar' class='zoom'>guardar</div></div>");

		out.println("<div class='keywordsClient'>");
		out.println("	<div id='results_Foros' class='contentTable'>");
		out.println("		<table id='tCategorias' class='table'>");
		out.println("			<thead><tr><th class='cabeceraTable cCWeb'>Web</th><th class='cabeceraTable cCDR'>DR</th><th class='cabeceraTable cCDA'>DA</th><th class='cabeceraTable cCTem'>Tem&aacute;tica</th><th class='cabeceraTable cCDesc'>Descripci&oacute;n</th><th class='cabeceraTable cCReut'>Reutilizable</th><th class='cabeceraTable cCRegi'>Requiere</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>");
		out.println("			<tbody>");

		for(int i = 0; i < foros.size(); i++) {
			int id_foro = foros.get(i).getIdForo();

			//LISTA DESPLEGABLE TEMATICA------------------------------------			
			String opTematica = foros.get(i).getTematica();
			//Esta linea de abajo cuenta el numero de palabras que hay en 'opTematica'
			StringTokenizer numTematicas = new StringTokenizer(opTematica,",");
			String htmlTematica = 	"<ul size="+tematicas.size()+" class='slCt slT effect7 ulTem'>";
			htmlTematica += 	  		"<div class='pretty p-icon p-smooth divTemSele divTodo'>";		
			htmlTematica += 				"<input class='slT' id='all' type='checkbox' onclick='guardarTematica(this)'/>";
			htmlTematica += 				"<div class='state p-success paTem'><i class='icon material-icons'>done</i><label>Todas</label></div>";

			htmlTematica += 			"</div><br>";
			for (int j = 0; j < tematicas.size(); j++) {
				String status="";
				if(opTematica.toLowerCase().contains(tematicas.get(j).getNombre().toLowerCase())) status = "checked" ;

				htmlTematica += 		"<div class='pretty p-icon p-smooth divTemSele nameTem'>";
				htmlTematica += 			"<input class='slT' tematica='"+tematicas.get(j).getNombre()+"' type='checkbox' onclick='guardarTematica(this)' "+status+"/>";
				htmlTematica += 			"<div class='state p-success'><i class='icon material-icons'>done</i><label>"+tematicas.get(j).getNombre()+"</label></div>";
				htmlTematica += 		"</div><br>";
			}
			htmlTematica += 		"</ul>";
			if(numTematicas.countTokens()==tematicas.size()) {opTematica="Todas";}
			//-----------DESPLEGABLE REQUIERE--------------------------------------
			String statusAprobacion="",statusRegistro="",statusFecha="";
			if(foros.get(i).getReqAprobacion()==1) statusAprobacion="checked";
			if(foros.get(i).getReqRegistro()==1) statusRegistro="checked";
			if(foros.get(i).getApareceFecha()==1) statusFecha="checked";
			String htmlRequiere="<ul class='slCt slT effect7 ulReq'>";
			htmlRequiere += 	  	"<li class='req'>";
			htmlRequiere += 			"<div class='req'><label class='switch req'><input id='req_aprobacion' onchange='guardarRequiere(this)' type='checkbox' "+statusAprobacion+"><span class='slider round req'></span><span class='spanRequiere req'>Aprobacion</span></label></div>";
			htmlRequiere += 		"</li>";
			htmlRequiere += 		"<li class='req'>";
			htmlRequiere += 			"<div class='req'><label class='switch req'><input id='req_registro' onchange='guardarRequiere(this)' type='checkbox' "+statusRegistro+"><span class='slider round req'></span><span class='spanRequiere req'>Registro</span></label></div>";
			htmlRequiere += 		"</li>";
			htmlRequiere += 		"<li class='req'>";
			htmlRequiere += 			"<div class='req'><label class='switch req'><input id='aparece_fecha' onchange='guardarRequiere(this)' type='checkbox' "+statusFecha+"><span class='slider round req'></span><span class='spanRequiere req'>Fecha</span></label></div>";
			htmlRequiere += 		"</li>";
			htmlRequiere += 	"</ul>";
			//------------DESPLEGABLE COLUMNA REUTILIZABLE---------
			String opReutilizable = "Selecciona";
			if(foros.get(i).getReutilizable().equalsIgnoreCase("un_cliente_una_vez")) opReutilizable = "UN cliente UNA vez";
			else if(foros.get(i).getReutilizable().equalsIgnoreCase("un_cliente_varias_veces")) opReutilizable = "UN cliente VARIAS veces";
			else if(foros.get(i).getReutilizable().equalsIgnoreCase("varios_clientes_una_vez")) opReutilizable = "VARIOS clientes UNA vez";
			else if(foros.get(i).getReutilizable().equalsIgnoreCase("varios_clientes_varias_veces")) opReutilizable = "VARIOS clientes VARIAS veces";
			String htmlReutilizable = "	<ul class='slCt effect7'>";
			htmlReutilizable += "			<div class='divSeparar'>Enlazar a:</div>";
			htmlReutilizable += "			<li contenido='un_cliente_una_vez' onclick='guardarReutilizable(this)'>UN cliente UNA vez</li>";		
			htmlReutilizable += "			<li contenido='un_cliente_varias_veces' onclick='guardarReutilizable(this)'>UN cliente VARIAS veces</li>";
			htmlReutilizable += "			<li contenido='varios_clientes_una_vez' onclick='guardarReutilizable(this)'>VARIOS clientes UNA vez</li>";
			htmlReutilizable += "			<li contenido='varios_clientes_varias_veces' onclick='guardarReutilizable(this)'>VARIOS clientes VARIAS veces</li>";	
			htmlReutilizable += "		</ul>";
			//------------DESPLEGABLE COLUMNA TIPO-----------------.
			String claseLink="";
			if(foros.get(i).getTipo().equalsIgnoreCase("follow"))  claseLink = "lf";
			else if(foros.get(i).getTipo().equalsIgnoreCase("nofollow"))  claseLink = "lnf";

			//------------------------Insertamos las filas de la tabla de foros-----------------------------------------------------------------------------------------------------------
			out.println("<tr id='"+id_foro+"'>");	
			//Columna WEB con animacion
			out.println(	"<td class='cCWeb'>");
			out.println(		"<div class='tdCat tdWeb'>");
			out.println(			"<input class='inLink' type='text' oninput='showGuardar()' onchange='guardarWebForo(this)'  value='"+foros.get(i).getWebForo()+"'>");
			out.println(		"	</div>");
			out.println(	"</td>");
			//Columna DR
			out.println("	<td class='cCDR' >");
			out.println("			<input class='inLink' type='text' oninput='showGuardar()' onchange='guardarDR(this)' value='"+foros.get(i).getDR()+"'>");				
			out.println("   </td>");
			//Columna DA
			out.println("	<td class='cCDA'>");
			out.println("			<input class='inLink' type='text' oninput='showGuardar()' onchange='guardarDA(this)' value='"+foros.get(i).getDA()+"'>");				
			out.println("   </td>");
			//Columna TEMATICA con el desplegable-----------------
			out.println("   <td class='tdCat cCTem pr' onclick='openTematica(this)'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat'>"+opTematica+"</span>");
			out.println(			"<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println("		</div>");
			out.println(		htmlTematica);
			out.println("	</td>");
			//Fin columna tematica--------------------------------
			//Columna DESCRIPCION
			out.println("<td class='cCDesc pr' onclick='openDescripcion(this)'>");
			out.println(	"<div class='tdCat tdWeb'>");
			out.println(		"<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>"+foros.get(i).getDescripcion().replace("<", "&lt;").replace(">", "&gt;")+"</span>");
			out.println(		"<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println(	"</div>");
			out.println(	"<textarea class='slCt effect7 taWeb'  wrap='hard' oninput='resizeta(this,0)' onclick='editandoDescripcion(this)' onchange='guardarDescripcion(this)'>"+foros.get(i).getDescripcion().replace("<", "&lt;").replace(">", "&gt;")+"</textarea>");
			out.println("</td>");
			//Columna REUTILIZABLE
			out.println("	<td class='tdCat cCReut pr' onclick='openReutilizable(this)'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span contenido='"+foros.get(i).getReutilizable()+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  class='tdCat'>"+opReutilizable+"</span>");
			out.println(			"<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println("		</div>");
			out.println(		htmlReutilizable);
			out.println("	</td>");
			//Columna REQUIERE DONE
			out.println("	<td class='tdCat cCReq pr' onclick='openRequiere(this)'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<i class='material-icons slT'>settings</i>");
			out.println("		</div>");
			out.println(		htmlRequiere);
			out.println("	</td>");
			//Columna TIPO con el desplegable
			out.println(	"<td class='cTipo slT pr cp' onclick='openTipo(this)'><i class='material-icons slT "+claseLink+"'>link</i>");
			out.println(		"<ul class='slCt effect7 ulTipoForo'>");
			out.println(			"<li tipo='follow' onclick='guardarTipo(this)'><i class='material-icons lf'>link</i></li>");
			out.println(			"<li tipo='nofollow' onclick='guardarTipo(this)'><i class='material-icons lnf'>link</i></li>");
			out.println(		"</ul>");
			out.println(	"</td>");
			out.println("</tr>");		

		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");

		String tematicas = htmlTemaicas();
		String requiere = htmlRequiere();

		out.println("</div>");
		out.println("<div class='newSomething effect7'>");
		out.println(	"<div class='cancelNew' onclick='cancelNew(this)' ><i class='material-icons i_cancel'> clear </i></div>");
		out.println(	"<table id='tNuevaWeb' class='table'>");
		out.println(	"	<thead><tr><th class='cabeceraTable cCWeb'>Web</th><th class='cabeceraTable cCDR'>DR</th><th class='cabeceraTable cCDA'>DA</th><th class='cabeceraTable cCTem'>Tem&aacute;tica</th><th class='cabeceraTable cCDesc'>Descripci&oacute;n</th><th class='cabeceraTable cCReut'>Reutilizable</th><th class='cabeceraTable cCRegi'>Requiere</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>");
		out.println("			<tbody>");
		out.println(				"<tr>"
							+ "<td class='cCWeb'><div class='tdCat tdWeb'><input class='inLink' type='text' placeholder='Introduce una web'></div></td>"
							+ "<td class='cCDR' ><input class='inLink' type='text' value='0'></td>"
							+ "<td class='cCDA'> <input class='inLink' type='text' value='0'></td>"
							+ "<td class='tdCat cCTem pr' onclick='openTematica(this)'>"
							+ "<div class='tdCat tdWeb'>"
							+ "<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat'>Selecciona tem&aacute;tica</span><i class='material-icons arrow'>arrow_drop_down</i>"
							+ "</div>"
							+tematicas
							+ "</td>"
							+ "<td class='cCDesc pr' onclick='openDescripcion(this)'>"
							+ "<div class='tdCat tdWeb'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>Introduce una descripci&oacute;n</span><i class='material-icons arrow'>arrow_drop_down</i></div>"
							+ "<textarea class='slCt effect7 taWeb  nuevaWeb'  wrap='hard' oninput='resizeta(this,0)' onclick='editandoDescripcion(this)' onchange='guardarDescripcion(this)'></textarea>"
							+ "</td>"
							+ "<td class='tdCat cCReut pr' onclick='openReutilizable(this)'>"
							+"<div class='tdCat tdWeb'><span contenido='' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  class='tdCat'></span><i class='material-icons arrow'>arrow_drop_down</i></div>"
							+htmlReutilizable()
							+ "</td>"
							+ "<td class='tdCat cCReq pr' onclick='openRequiere(this)'>"
							+"<div class='tdCat tdWeb'><i class='material-icons slT'>settings</i></div>"+requiere
							+ "</td>"
							+ "<td class='cTipo slT pr cp' onclick='openTipo(this)'><i tipo='follow' class='material-icons slT lf'>link</i>"
							+ "<ul class='slCt effect7 ulTipoForo nuevaWeb'>"
							+"<li tipo='follow' onclick='guardarTipo(this)'><i class='material-icons lf'>link</i></li>"
							+"<li tipo='nofollow' onclick='guardarTipo(this)'><i class='material-icons lnf'>link</i></li>"
							+"</ul>"
							+ "</td>"
							+ "</tr>");
		out.println("			</tbody>");
		out.println(	"</table>");
		out.println(	"<div class='infoNew'></div>"); 
		out.println(	"<div class='guardarNew' onclick='guardarNew(this)'>guardar</div>");
		out.println("</div>");
		out.println("<div class='divBlock'></div>");
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
	private void guardarWebResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int id_foro = Integer.parseInt(request.getParameter("id_foro"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int elemtAnterior = Integer.parseInt(request.getParameter("elemtAnterior"));
		int posicionResultado = Integer.parseInt(request.getParameter("posicionResultado"));
		int posicionForo = Integer.parseInt(request.getParameter("posicionForo"));

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "id_foro", id_foro+"" , "updateResultado.php");

		if(id_foro !=0) {
			cliente.getForos().add(foros.get(posicionForo));
			cliente.getResultados().get(posicionResultado).setId_foro(foros.get(posicionForo).getIdForo());
			cliente.getResultados().get(posicionResultado).setWeb_foro(foros.get(posicionForo).getWebForo());
		}else {
			cliente.getResultados().get(posicionResultado).setId_foro(0);
			cliente.getResultados().get(posicionResultado).setWeb_foro("");
		}

		//si no hay ningun foro anteriormento no hacemos el for, asi nos ahorramos ese proceso
		if(elemtAnterior!=0) {
			for (int i = 0, j = cliente.getForos().size()-1; i < cliente.getForos().size(); i++, j--) {
				if(cliente.getForos().get(i).getIdForo() == elemtAnterior) {cliente.getForos().remove(i);i = cliente.getForos().size();}
				else if(cliente.getForos().get(j).getIdForo() == elemtAnterior) {cliente.getForos().remove(j); i = cliente.getForos().size();}
			}
		}
		out.println("<span mweb='"+id_foro+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb'>"+cliente.getResultados().get(posicionResultado).getWeb_foro()+"</span>");
		out.println("<i class='material-icons arrow'>arrow_drop_down</i>");
		System.out.println("Insertado");

	}
	private void mostrarClientesInput(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String k = request.getParameter("keyword").toLowerCase();
		System.out.println(k);
		int inicio = 0;
		String clases = "",clases2="";
		for (int i = 0; i < clientes.size(); i++) {
			ClienteGson c = clientes.get(i);

			String nombre = clientes.get(i).getNombre().toLowerCase();
			String web = clientes.get(i).getWeb().toLowerCase();

			if(nombre.contains(k) || web.contains(k)) {

				if(c.getEditando()==1 /*&& c.getIdCliente()!=id_client*/) {clases="itemChild blur";clases2="blockClient visible";}
				else {clases ="itemChild";clases2="blockClient";}

				out.println("<div id='"+c.getIdCliente()+"' onclick='selectClient(this.id)' class='item'>");
				if(inicio!=0) {
					out.println(	"<div class='line'></div>");
				}
				out.println(		"<div class='"+clases+"'>");
				out.println(			"<div class='nameItem'>");
				out.println(				"<span class='nameItem sName' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  >"+c.getNombre()+"</span>");
				out.println(			"</div>");
				out.println(			"<div class='dominioItem'>"+c.getWeb()+"</div>");
				if(c.getEditando()==0 /*|| c.getIdCliente() == id_client*/) {
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
		out.println("<script type='text/javascript'>setC();</script>");
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
	private String htmlReutilizable() {
		String htmlReutilizable = "	<ul class='slCt effect7 nuevaWeb'>";
		htmlReutilizable += "			<div class='divSeparar'>Enlazar a:</div>";
		htmlReutilizable += "			<li contenido='un_cliente_una_vez' onclick='guardarReutilizable(this)'>UN cliente UNA vez</li>";		
		htmlReutilizable += "			<li contenido='un_cliente_varias_veces' onclick='guardarReutilizable(this)'>UN cliente VARIAS veces</li>";
		htmlReutilizable += "			<li contenido='varios_clientes_una_vez' onclick='guardarReutilizable(this)'>VARIOS clientes UNA vez</li>";
		htmlReutilizable += "			<li contenido='varios_clientes_varias_veces' onclick='guardarReutilizable(this)'>VARIOS clientes VARIAS veces</li>";	
		htmlReutilizable += "		</ul>";
		return htmlReutilizable;
	}
	private String htmlTemaicas() {
		String htmlTematica = 	"<ul size="+tematicas.size()+" class='slCt slT effect7 ulTem  nuevaWeb'>";
		htmlTematica += 	  		"<div class='pretty p-icon p-smooth divTemSele divTodo'>";		
		htmlTematica += 				"<input class='slT' id='all' type='checkbox' onclick='guardarTematica(this)'/>";
		htmlTematica += 				"<div class='state p-success paTem'><i class='icon material-icons'>done</i><label>Todas</label></div>";							
		htmlTematica += 			"</div><br>";
		for (int j = 0; j < tematicas.size(); j++) {
			htmlTematica += 		"<div class='pretty p-icon p-smooth divTemSele nameTem'>";
			htmlTematica += 			"<input class='slT' tematica='"+tematicas.get(j).getNombre()+"' type='checkbox' onclick='guardarTematica(this)'/><div class='state p-success'><i class='icon material-icons'>done</i><label>"+tematicas.get(j).getNombre()+"</label></div>";
			htmlTematica += 		"</div><br>";
		}
		htmlTematica += 		"</ul>";

		return htmlTematica;
	}
	private String htmlRequiere(){
		String htmlRequiere="<ul class='slCt slT effect7 ulReq  nuevaWeb'>";
		htmlRequiere += 	  	"<li class='req'>";
		htmlRequiere += 			"<div class='req'><label class='switch req'><input id='req_aprobacion' onchange='guardarRequiere(this)' type='checkbox'><span class='slider round req'></span><span class='spanRequiere req'>Aprobacion</span></label></div>";
		htmlRequiere += 		"</li>";
		htmlRequiere += 		"<li class='req'>";
		htmlRequiere += 			"<div class='req'><label class='switch req'><input id='req_registro' onchange='guardarRequiere(this)' type='checkbox'><span class='slider round req'></span><span class='spanRequiere req'>Registro</span></label></div>";
		htmlRequiere += 		"</li>";
		htmlRequiere += 		"<li class='req'>";
		htmlRequiere += 			"<div class='req'><label class='switch req'><input id='aparece_fecha' onchange='guardarRequiere(this)' type='checkbox'><span class='slider round req'></span><span class='spanRequiere req'>Fecha</span></label></div>";
		htmlRequiere += 		"</li>";
		htmlRequiere += 	"</ul>";
		return htmlRequiere;
	}

	//GUARDAR CAMPOS TABLA CLIENTES
	private void mostrarVentanaClientes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role) throws IOException, ParseException {
		
		String json = ws.getClientsByUser(id_user+"", user_role, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		
		String categoria = user_role.substring(0, user_role.lastIndexOf("_"));
		json = ws.getUsersByCategoria(categoria, "getUsersByCategoria.php");
		gson = new Gson();
		ArrayList<UsuarioGson> usuariosGson = gson.fromJson(json, new TypeToken<List<UsuarioGson>>(){}.getType());
		
		String selectDelete = "<div class='pretty p-icon p-smooth div_select_cliente'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div>";
		//COMIENZA LA TABLA CLIENTES
		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>CLIENTES</div>");//"+categorias.get(posicion).getEnlace()+"
		out.println("	<div class='btnAdd' onclick='openNewCliente(this)'>Nuevo cliente</div>");
		out.println("</div>");
		out.println("<div class='ctoolbar'><div onclick='deleteClient(this)' class='delete_client'><i class='material-icons gris'>delete_outline</i></div><div id='websGuardar' class='zoom'>guardar</div></div>");
		out.println("<div class='keywordsClient listaClientes'>");
		out.println("	<div id='results_Client' class='contentTable'>");
		out.println("		<table id='tClients' class='table'>");
		out.println("			<thead><tr><th class='cabeceraTable select_client '>"+selectDelete+"</th><th class='cabeceraTable cCWebCliente'>Web</th><th class='cabeceraTable cCNombre'>Nombre</th><th class='cabeceraTable cCTipo'>Servicio</th><th class='cabeceraTable cFollow'>Follow</th><th class='cabeceraTable cNoFollow'>NoFollow</th><th class='cabeceraTable anchorC'>Anchor</th><th class='cabeceraTable cCBlog'>Blog</th><th class='cabeceraTable cCIdioma'>Idioma</th><th class='cabeceraTable cCUser'>User</th></tr></thead>");
		out.println("			<tbody>");
		String htmlServicio="",htmlUserFinal="";
		for (int c = 0; c < clientesGson.size(); c++) {
			htmlUserFinal="";
			int id_cliente = clientesGson.get(c).getIdCliente();//este es el ID REAL DE LA BBDD
			//------------DESPLEGABLE COLUMNA SERVICIO-----------------
			String claseLite="",clasePro="",clasePremium="", claseMedida="", readonly="";
			String opServicio = clientesGson.get(c).getServicio();
			if(clientesGson.get(c).getServicio().equals("lite"))         { opServicio = "SEO Lite"; 	  claseLite="class='liActive'";	  readonly="readonly='true'";}
			else if(clientesGson.get(c).getServicio().equals("pro"))    { opServicio = "SEO Pro";       clasePro="class='liActive'";      readonly="readonly='true'";}
			else if(clientesGson.get(c).getServicio().equals("premium")){ opServicio = "SEO Premium";   clasePremium="class='liActive'";  readonly="readonly='true'";}
			else if(clientesGson.get(c).getServicio().equals("medida")) { opServicio = "SEO a medida";  claseMedida="class='liActive'"; }
			htmlServicio = "			<li id='lite' "+claseLite+" data-follows='3' data-nofollows='5' onclick='guardarServicio(this)'>SEO Lite</li>";
			htmlServicio += "			<li id='pro' "+clasePro+" data-follows='4' data-nofollows='10' onclick='guardarServicio(this)'>SEO Pro</li>";
			htmlServicio += "			<li id='premium' "+clasePremium+" data-follows='6' data-nofollows='15' onclick='guardarServicio(this)'>SEO Premium</li>";
			htmlServicio += "			<li id='medida' "+claseMedida+" data-follows='0' data-nofollows='0' onclick='guardarServicio(this)'>SEO a medida</li>";
			//------------DESPLEGABLE COLUMNA USER-----------------
			String opUser = clientesGson.get(c).getLinkbuilder();
			String htmlUser = "",name="", claseUser="";
			for (UsuarioGson u : usuariosGson) {
				claseUser="";
				if(opUser.contains("["+u.getId()+"]")) {
					name = u.getName();claseUser = "class='liActive'";
				}
				htmlUser += "<li id='["+u.getId()+"]' "+claseUser+" onclick='guardarUser(this)'>"+u.getName()+"</li>"; 
				htmlUserFinal += "<li id='["+u.getId()+"]' onclick='guardarUser(this)'>"+u.getName()+"</li>"; 
			}
			//-----------------------------------------------------
			String blogChecked ="checked";
			if(Integer.parseInt(clientesGson.get(c).getBlog())==0) blogChecked="";

			out.println("<tr id='"+id_cliente+"' posicion='"+c+"'>");
			out.println("	<td class='select_client'>");
			out.println("		<div class='pretty p-icon p-smooth div_select_cliente'>");
			out.println("			<input class='slT' type='checkbox'/>");
			out.println("			<div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div>");
			out.println("		</div>");
			out.println("	</td>");
			//Columna WEB
			out.println("	<td class='cCWebCliente'>");
			out.println("		<input class='inLink' oninput='showGuardar()' type='text' onchange='guardarWebCliente(this)' value='"+clientesGson.get(c).getWeb()+"'>");
			out.println("   </td>");
			//Columna NOMBRE
			out.println("	<td class='cCNombre'>");
			out.println("		<input class='inLink' oninput='showGuardar()' type='text' onchange='guardarNombreCliente(this)' value='"+clientesGson.get(c).getNombre()+"'>");
			out.println("	</td>");
			//Columna SERVICIO
			out.println("	<td class='cCTipo pr' onclick='openServicio(this)'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span class='tdCat'>"+opServicio+"</span>");
			out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
			out.println("		</div>");		
			out.println(		"<ul class='slCt effect7'>"+htmlServicio+"</ul>");
			out.println("   </td>");
			//Columna FOLLOW
			out.println("	<td class='cFollow'>");
			out.println("		<input class='inLink' oninput='showGuardar()' type='text' onchange='guardarFollows(this)' value='"+clientesGson.get(c).getFollows()+"' "+readonly+">");				
			out.println("   </td>");			
			//Columna NOFOLLOW
			out.println("   <td class='cNoFollow'>");
			out.println("		<input class='inLink' oninput='showGuardar()' type='text' onchange='guardarNoFollows(this)' value='"+clientesGson.get(c).getNofollows()+"' "+readonly+">");	
			out.println("	</td>");	
			//Columna ANCHOR
			out.println("	<td class='anchorC'>");
			out.println("			<input type='text' oninput='showGuardar()' class='inLink' onchange='guardarAnchorCliente(this)' value='"+clientesGson.get(c).getAnchor()+"'>");
			out.println("	</td>");
			//Columna BLOG
			out.println("	<td class='tdCat cCBlog pr'>");
			out.println("		<div class='tdCat tdWeb ckBlog'>");
			out.println("			<label  class='switch'>");
			out.println("			<input class='slT' type='checkbox' onchange='guardarBlog(this)' "+blogChecked+">");
			out.println("			<span class='slider round'></span>");
			out.println("			</label>");
			out.println("		</div>");
			out.println("	</td>");
			//Columna IDIOMA
			out.println("	<td class='tdCat cCIdioma'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<input oninput='showGuardar()' class='inLink' type='text' onchange='guardarIdioma(this)' value='"+clientesGson.get(c).getIdioma()+"'>");				
			out.println("		</div>");
			out.println("	</td>");
			//Columna USER
			out.println("	<td class='tdCat cCUser pr' onclick='opentUser(this)'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span data-list-user='"+opUser+"' class='tdCat' type='text'>"+name+"</span>");	
			out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
			out.println("		</div>");
			out.println("		<ul class='slCt effect7'>"+htmlUser+"</ul>");
			out.println("	</td>");
			out.println("</tr>");	
		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");
		out.println("</div>");
		out.println("<div class='newSomething effect7'>");
		out.println(	"<div class='cancelNew' onclick='cancelNewCliente(this)' ><i class='material-icons i_cancel'> clear </i></div>");
		out.println(	"<table id='tNuevoCliente' class='table'>");
		out.println("			<thead><tr><th class='cabeceraTable cCWebCliente'>Web</th><th class='cabeceraTable cCNombre'>Nombre</th><th class='cabeceraTable tipoNew'>Servicio</th><th class='cabeceraTable cFollow'>Follow</th><th class='cabeceraTable cNoFollow'>NoFollow</th><th class='cabeceraTable anchorC'>Anchor</th><th class='cabeceraTable cCBlog'>Blog</th><th class='cabeceraTable cCIdioma'>Idioma</th><th class='cabeceraTable cCUser'>User</th></tr></thead>");
		out.println("			<tbody>");
		out.println("				<td class='cCWebCliente'>");
		out.println("					<input class='inLink'  placeholder='Introduce una web' type='text' onchange='guardarWebCliente(this)' value=''>");
		out.println("			    </td>");
		out.println("				<td class='cCNombre'>");
		out.println("					<input class='inLink' placeholder='Introduce un nombre' type='text' onchange='guardarNombreCliente(this)' value=''>");
		out.println("				</td>");
		out.println("				<td class='tipoNew pr'  onclick='openServicio(this)'>");
		out.println("					<div class='tdCat tdWeb'>");
		out.println("						<span class='tdCat'>-</span>");
		out.println("						<i class='material-icons arrow'>arrow_drop_down</i>	");
		out.println("					</div>");		
		out.println(					"<ul class='slCt effect7 nuevaWeb'>"+htmlServicio+"</ul>");
		out.println("			   	</td>");
		out.println("			   	<td class='cFollow'>");
		out.println("					<input class='inLink' type='text' onchange='guardarFollows(this)' value='0'>");				
		out.println("   		   	</td>");			
		out.println("   		   	<td class='cNoFollow'>");
		out.println("			   		<input class='inLink' type='text' onchange='guardarNoFollows(this)' value='0'>");	
		out.println("			   	</td>");
		out.println("				<td class='anchorC'>");
		out.println("					<input type='text' class='inLink' placeholder='Introduce un anchor' onchange='guardarAnchorCliente(this)' value=''>");
		out.println("				</td>");
		out.println("				<td class='tdCat cCBlog pr'>");
		out.println("					<div class='tdCat tdWeb ckBlog'>");
		out.println("						<label  class='switch'>");
		out.println("							<input class='slT' type='checkbox' onchange='guardarBlog(this)'>");
		out.println("							<span class='slider round'></span>");
		out.println("						</label>");
		out.println("					</div>");
		out.println("				</td>");
		out.println("				<td class='tdCat cCIdioma'>");
		out.println("					<input class='inLink' type='text' onchange='guardarIdioma(this)' value='ESP'>");				
		out.println("				</td>");
		out.println("				<td class='tdCat cCUser pr' onclick='opentUser(this)'>");
		out.println("					<div class='tdCat tdWeb'>");
		out.println("						<span class='tdCat' data-list-user='' type='text'>-</span>");
		out.println("						<i class='material-icons arrow'>arrow_drop_down</i>	");
		out.println("					</div>");
		out.println("					<ul class='slCt effect7 nuevaWeb'>"+htmlUserFinal+"</ul>");
		out.println("				</td>");
		out.println("			</tbody>");
		out.println(	"</table>");
		out.println(	"<div class='infoNew'></div>"); 
		out.println(	"<div class='guardarNew' onclick='guardarNewCliente(this)'>guardar</div>");
		out.println("</div>");
		out.println("<div class='divBlockClientes'></div>");

	}
	private void guardarValoresCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String valor = request.getParameter("valor");
		String campo = request.getParameter("campo");
		String id_cliente = request.getParameter("id_cliente");	
		ws.updateCliente(id_cliente, campo, valor, "updateCliente.php");
		System.out.println("guardado");
	}
	@SuppressWarnings("unchecked")
	private void guardarNuevoCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String web = request.getParameter("web");
		String nombre = request.getParameter("nombre");
		String servicio = request.getParameter("servicio");
		String follow = request.getParameter("follow");
		String nofollow = request.getParameter("nofollow");
		String anchor = request.getParameter("anchor");			
		String blog = request.getParameter("blog");
		String idioma = request.getParameter("idioma");
		String user = request.getParameter("user");
		
		//comprobamos que el cliente ya esta insertado en la base de datos
		String dominio = web.replace("http://", "").replace("https://","").replace("www.","");
		dominio = dominio.substring(0, dominio.lastIndexOf("."));
		String json = ws.getClienteByPieceDominio(dominio, "getClienteByPieceDominio.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
	
		boolean coincidenciaExacta =false;
		String coincidenciaParcial="";
		for (ClienteGson c : clientesGson) {
			if(c.getWeb().equals(web)) {coincidenciaExacta=true;break;
			}else {coincidenciaParcial=c.getWeb();}
		}
		response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    JSONObject obj = new JSONObject();
	    String status="",text="";
		if(clientesGson.size()==0) {//si esto es igual a 0 significa que en la bbdd no hay ningun cliente con este dominio
			status="0";
			ws.nuevoCliente(web, nombre, servicio, follow, nofollow, anchor, blog, idioma, user, "insertNuevoCliente.php");
		}else {
			if(coincidenciaExacta) {	status="1"; text="Este cliente ya existe";}
			else {						status="2"; text="Coincidencia parcial en el dominio con el cliente:  ";}
		}
		obj.put("status", status);obj.put("text", text);obj.put("c", coincidenciaParcial);out.print(obj);		
	}
	private void eliminarCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String json = request.getParameter("json");
		
		Object jsonObject =JSONValue.parse(json.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			
			String id_cliente = row.get("id_cliente").toString();
			String web_cliente = row.get("web_cliente").toString();
			ws.updateCliente(id_cliente, "Eliminado", "1", "updateCliente.php");
			System.out.println("Cliente "+web_cliente+" eliminado");
			
			
		}
		
	}
	
	private void subirNuevaFactura(HttpServletRequest request, HttpServletResponse response, int id_user) throws IOException, ServletException {
		
		String nameFile = request.getParameter("nombre");
		System.out.println(nameFile);
		
		ReadFactura rf = new ReadFactura();
		
		if(nameFile.endsWith(".xlsx")) rf.readExcel(request);	
		else System.out.println("No se encontro ningun archivo");
		
	}
	
}