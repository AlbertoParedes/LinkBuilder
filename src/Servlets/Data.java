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
import Objects.Categoria;
import Objects.Cliente;
import Objects.Foro;
import Objects.Resultado;
import Objects.Tematica;
import Objects.Gson.CategoriaGson;
import Objects.Gson.ClienteGson;
import Objects.Gson.ForoGson;
import Objects.Gson.TematicaGson;
import Objects.Gson.ResultadoGson;


@WebServlet("/Data")
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Webservice ws = new Webservice();

	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
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
		String json = ws.getClientsByUser(id_user, role, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		
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

		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		out = response.getWriter();

		if(metodo.equals("ckcs")) {
			try {checkClients(request, response, out,id_user,role);} catch (ParseException e) {}
		}else if(metodo.equals("ckc")) { 
			try {checkClientEdition(request, response, out,id_user);} catch (ParseException e) {}
		}else if (metodo.equals("lkc")) {
			try {mostrarResultados(request, response, out);} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("chv")) {
			morstarResultadosMes(request, response, out);
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
			guardarForoCompleto(request, response, out);
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
		}
	}

	private void checkClients(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user,String role) throws IOException, ParseException {
		int id_client = Integer.parseInt(request.getParameter("id_client"));
		//Obtenemos los clientes del usuario ------------------------------------------------------------------------
		String json = ws.getClientsByUser(id_user,role, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		//-----------------------------------------------------------------------------------------------------------
		int inicio = 0;
		String clases = "",clases2="";

		for (ClienteGson c : clientesGson) {
			if(c.getEditando()==1 && c.getIdCliente()!=id_client) {clases="itemChild blur";clases2="blockClient visible";}
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
			if(c.getEditando()==0 || c.getIdCliente() == id_client) {
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
	private void checkClientEdition(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user) throws IOException, ParseException {
		int id_client = Integer.parseInt(request.getParameter("id_client"));
		System.out.println(id_client);

		//obtenemos la disponibilidad del cliente clickado
		String json = ws.getClientById(id_client, "getClientById.php");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ArrayList<ResultadoGson> resultadosGson = gson.fromJson(json, new TypeToken<List<ResultadoGson>>(){}.getType());

		Cliente cliente = null;

		int x = 0;
		for (ResultadoGson r : resultadosGson) {
			if(x==0 && r.getEditando() == 0) {
				cliente = new Cliente(r.getIdCliente(), r.getWeb(), r.getNombre(), r.getServicio(), r.getFollows(), r.getNofollows(), r.getAnchorCliente(), r.getBlog(), r.getIdioma(), r.getFollowsDone(), r.getNofollowsDone(), r.getLinkbuilder(), r.getEditando(), new ArrayList<Resultado>(), new ArrayList<ForoGson>());
				//desbilitamos todos los clientes que estan bloqueados por este usuario
				ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");
				//crear peticion para poner el cliente en edicion = 1
				json = ws.actualizarEditando(1, id_user, r.getIdCliente(), "actualizarEditando.php");
				x=1;
			}
			if(r.getEditando() == 0) {
				cliente.getResultados().add(new Resultado(r.getIdResultado(), r.getIdForo(), r.getEnlace(), r.getFecha(), r.getTipoRes(), r.getDestino(), r.getCategoriaResultado(), r.getEstado(), r.getAnchorR(), r.getWebForo()));
				cliente.getForos().add(new ForoGson(r.getIdForo(), r.getWebForo(), r.getTipoForo(), r.getDR(), r.getDA(), r.getTematica(), r.getDestino(), r.getCategoriaResultado(), r.getReqAprobacion(), r.getReqRegistro(), r.getApareceFecha(), r.getReutilizable()));
			}else {
				break;
			}

		}
		//si es null es que un usuario ya esta editando este cliente
		if(cliente!=null) {
			this.cliente = cliente;
			mostrarResultados(request, response, out);
		}

	}
	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ParseException {
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
		out.println("			<thead><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cLink'>Link</th><th class='cabeceraTable cCateg'>Categoria</th><th class='cabeceraTable cWeb'>Web</th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>");
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
		if(resultados.size()==0) {
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
				String anchorR = (String) row.get("anchorR");

				cliente.getResultados().add(new Resultado(id_resultado, id_foro, enlace, dateR, tipo, destino, categoria, estado, anchorR,""));
			}
		}else {
			System.out.println("curso del programa normal");
		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");

		out.println("</div>");
	}
	private void morstarResultadosMes(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		int mes = Integer.parseInt(request.getParameter("mes"));
		int year = Integer.parseInt(request.getParameter("year"));
		out = response.getWriter();

		for (int i = 0; i < cliente.getResultados().size(); i++) {

			int id_resultado = cliente.getResultados().get(i).getId_resultado();

			String fecha[] = cliente.getResultados().get(i).getFecha().toString().split("-");
			int y = Integer.parseInt(fecha[0]);
			int m = Integer.parseInt(fecha[1]);

			if(mes == m && year == y) {
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

				//Foro utilizado para este resultado
				int mweb = cliente.getResultados().get(i).getId_foro();
				String claseStatus="";
				if(!cliente.getResultados().get(i).getEnlace().equalsIgnoreCase("")) claseStatus="sOK";
				else claseStatus="sPendiente";
				
				String claseTipo="";
				if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("follow"))claseTipo="lf";
				else if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("nofollow"))claseTipo="lnf";
				
				//TABLA----------------------
				out.println("<tr id='"+id_resultado+"' posicion='"+i+"'>");
				out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");
				out.println("	<td class='cLink'>");
				out.println("		<input class='inLink' onchange='guardarEnlaceResultado(this)' oninput='saveClient()' type='text' value='"+cliente.getResultados().get(i).getEnlace()+"'>");
				out.println("	</td>");
				out.println("	<td class='tdCat cCateg pr' onclick='openCategoriaResultado(this)'>");
				out.println("		<div class='tdCat'>");
				out.println("			<span class='tdCat'>"+opCategoria+"</span>");
				out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		"<ul class='slCt effect7'>"+htmlCategorias+"</ul>");
				out.println("	</td>");
				out.println("	<td class='tdCat tdWeb cWeb pr' onclick='openWebResultado(this)'>");
				out.println("		<div class='tdCat tdWeb'>");
				out.println("			<span mweb='"+mweb+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb'>"+cliente.getResultados().get(i).getWeb_foro()+"</span>");
				out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		"<ul class='slCt slWeb effect7'></ul>");
				out.println("	</td>");
				out.println("	<td class='cDest'><input class='inLink' onchange='updateDestino(this)' oninput='saveClient()' type='text' value='"+cliente.getResultados().get(i).getDestino()+"'></td>");
				out.println("	<td class='cAnchor'><input class='inLink' onchange='updateAnchor(this)' oninput='saveClient()' type='text' value='"+cliente.getResultados().get(i).getAnchor()+"'></td>");
				out.println("	<td tipo='follow' class='cTipo'><i class='material-icons "+claseTipo+"'>link</i></td>");
				out.println("</tr>");
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

		
		ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
		ws.updateResultado(id_resultado+"", "hecho_por", id_user+"" , "updateResultado.php");
		cliente.getResultados().get(posicion).setEnlace(link);

		//TODO A�adir al los if la condicion de las fechas 
		int follows_no_done = 0,nofollows_no_done = 0;
		for (int j = cliente.getResultados().size()-1; j > -1; j--) {
			System.out.println(cliente.getResultados().get(j).getEnlace()+ "  "+cliente.getResultados().get(j).getFecha());
			if(cliente.getResultados().get(j).getEnlace().trim().equals("") && cliente.getResultados().get(j).getTipo().equalsIgnoreCase("follow")) {
				follows_no_done++;
			}else if(cliente.getResultados().get(j).getEnlace().trim().equals("") && cliente.getResultados().get(j).getTipo().equalsIgnoreCase("nofollow")) {
				nofollows_no_done++;
			}
		}

		cliente.setFollows_done(cliente.getFollows()-follows_no_done);
		cliente.setNofollows_done(cliente.getNofollows()-nofollows_no_done);

		if(tipo.equalsIgnoreCase("follow")) {
			ws.updateCliente(cliente.getId_cliente()+"","follows_done",cliente.getFollows_done()+"","updateCliente.php");
		}else if(tipo.equalsIgnoreCase("nofollow")) {
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
	private void guardarForoCompleto(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {

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


		ws.insertWebs(web, dr, da, descripcion, reutilizable, tipo, aprobacion, registro, fecha, tematica, categoria, "insertWebs.php");
		System.out.println("Insertada");
	}
	private void mostrarForos(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {

		int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
		int posicion = Integer.parseInt(request.getParameter("posicion"));

		//obtenemos los foros de la categoria seleccionada
		String json = ws.getForosByCategoria(id_categoria+"", "getForosByCategoria.php");
		Gson gson = new Gson();
		ArrayList<ForoGson> foros = gson.fromJson(json, new TypeToken<List<ForoGson>>(){}.getType());

		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>"+categorias.get(posicion).getEnlace()+"</div>");
		out.println("	<div class='btnAdd' onclick='openNew(this)'>Nueva web</div>");
		out.println("</div>");
		out.println("<div class='ctoolbar'><div id='websGuardar' class='zoom'>guardar</div></div>");

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
			out.println(			"<input class='inLink' type='text' oninput='showGuardar()' onchange='guardarWebForo(this)' value='"+foros.get(i).getWebForo()+"'>");
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
			out.println(		"<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>"+foros.get(i).getDescripcion()+"</span>");
			out.println(		"<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println(	"</div>");
			out.println(	"<textarea class='slCt effect7 taWeb'  wrap='hard' oninput='resizeta(this,0)' onclick='editandoDescripcion(this)' onchange='guardarDescripcion(this)'>"+foros.get(i).getDescripcion()+"</textarea>");
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
		out.println(	"<div class='cancelNew' onclick='cancelNew(this)' ><i class='material-icons lnf'> clear </i></div>");
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
		out.println(	"<div class='guardarNew' onclick='guardarNew(this)'>guardar</div>");
		out.println("</div>");
		out.println("<div class='divBlock'></div>");
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

	//Alberto 
	private void mostrarWebResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		String forosUsados="";
		for (ForoGson foro : cliente.getForos()) {forosUsados = forosUsados + "["+foro.getIdForo()+"]";}

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

		cliente.getForos().add(foros.get(posicionForo));
		cliente.getResultados().get(posicionResultado).setId_foro(foros.get(posicionForo).getIdForo());
		cliente.getResultados().get(posicionResultado).setWeb_foro(foros.get(posicionForo).getWebForo());
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
	

	//modificar!!!!!!!!!!
	private void mostrarClientesInput(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String k = request.getParameter("keyword").toLowerCase();

		System.out.println(k);


		int j = 0;
		for (int i = 0; i < clientes.size(); i++) {
			String nombre = clientes.get(i).getNombre().toLowerCase();
			String web = clientes.get(i).getWeb().toLowerCase();

			if(nombre.contains(k) || web.contains(k)) {
				if(j==0) {out.println("<div id='"+i+"' onclick='selectClient(this.id, 'lkc')' class='item'><div class='itemChild'><div class='nameItem'>"+clientes.get(i).getNombre()+"</div><div class='dominioItem'>"+clientes.get(i).getWeb()+"</div></div></div>");j++;
				}else {out.println(   "<div id='"+i+"' onclick='selectClient(this.id, 'lkc')' class='item'><div class='line'></div><div class='itemChild'><div class='nameItem'>"+clientes.get(i).getNombre()+"</div><div class='dominioItem'>"+clientes.get(i).getWeb()+"</div></div></div>");}
			}

		}

		out.println("<script type='text/javascript'>setC();</script>");

		System.out.println("Lista clientes cargada");

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




































