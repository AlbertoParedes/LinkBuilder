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
			try {cargarPage(request, response, id, name);} catch (ParseException e) {e.printStackTrace();}
		}else {
			response.sendRedirect("Login");
		}
	}

	private void cargarPage(HttpServletRequest request, HttpServletResponse response, int id_user, String name_user) throws ServletException, IOException, ParseException {
		System.out.println(id_user+" "+name_user+"############################");
		ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");

		//Obtenemos los clientes del usuario ------------------------------------------------------------------------
		String json = ws.getClientsByUser(id_user, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		//-----------------------------------------------------------------------------------------------------------

		//ontenemos todas las categorias
		json = ws.getJSON("getCategorias.php");
		gson = new Gson();
		ArrayList<CategoriaGson> categoriasGson = gson.fromJson(json, new TypeToken<List<CategoriaGson>>(){}.getType());
		this.categorias = categoriasGson;

		//obtenemos las tematicas de los foros
		json = ws.getJSON("getTematica.php");
		gson = new Gson();
		ArrayList<TematicaGson> tematicasGson = gson.fromJson(json, new TypeToken<List<TematicaGson>>(){}.getType());
		this.tematicas = tematicasGson;

		//obtenemos todos los foros
		json = ws.getJSON("getForos.php");
		gson = new Gson();
		ArrayList<ForoGson> forosGson = gson.fromJson(json, new TypeToken<List<ForoGson>>(){}.getType());
		this.foros = forosGson;

		String f = name_user.substring(0, 1).toUpperCase();name_user = f + name_user.substring(1,name_user.length()).toLowerCase();

		request.setAttribute("clientes", clientesGson);//pasamos la lista de clientes
		request.setAttribute("name_user", name_user);

		request.getRequestDispatcher("Data.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());

		String metodo = request.getParameter("metodo");

		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		if(request.getParameter("posicion")!=null) {
			int posicion = Integer.parseInt(request.getParameter("posicion"));
			cliente = clientes.get(posicion);
		}

		out = response.getWriter();

		if(metodo.equals("ckcs")) {
			try {checkClients(request, response, out,id_user);} catch (ParseException e) {}
		}else if(metodo.equals("ckc")) { 
			try {checkClientEdition(request, response, out,id_user);} catch (ParseException e) {}
		}else if (metodo.equals("lkc")) {
			try {mostrarResultados(request, response, out);} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("chv")) {
			morstarResultadosMes(request, response, out);
		}else if(metodo.equals("sc")) {
			guardarCategoria(request, response, out);
		}else if(metodo.equals("cwbs")) {
			mostrarWebsCat(request, response, out);
		}else if(metodo.equals("sw")) {
			guardarWeb(request, response, out);
		}else if(metodo.equals("chl")) {
			guardarLink(request, response, out);
		}


		else if(metodo.equals("scs")) {
			mostrarClientesInput(request, response, out);
		}else if(metodo.equals("cats")) {
			mostrarCategorias(request, response, out);
		}else if(metodo.equals("selectCat")) {
			mostrarForos(request, response, out);
		}

	}

	private void checkClients(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user) throws IOException, ParseException {
		int id_client = Integer.parseInt(request.getParameter("id_client"));
		//Obtenemos los clientes del usuario ------------------------------------------------------------------------
		String json = ws.getClientsByUser(id_user, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		//-----------------------------------------------------------------------------------------------------------
		int inicio = 0;
		String clases = "",clases2="";

		for (ClienteGson c : clientesGson) {
			if(c.getEditando()==1 && c.getIdCliente()!=id_client) {clases="itemChild blur";clases2="blockClient visible";}
			else {clases ="itemChild";clases2="blockClient";}

			out.println("<div id=\""+c.getIdCliente()+"\" onclick=\"selectClient(this.id)\" class=\"item\">");
			if(inicio!=0) {
				out.println(	"<div class=\"line\"></div>");
			}
			out.println(		"<div class=\""+clases+"\">");
			out.println(			"<div class=\"nameItem\">");
			out.println(				"<span class=\"nameItem sName\" onmouseover=\"viewAll(this)\" >"+c.getNombre()+"</span>");
			out.println(			"</div>");
			out.println(			"<div class=\"dominioItem\">"+c.getWeb()+"</div>");
			if(c.getEditando()==0 || c.getIdCliente() == id_client) {
				if(c.getFollows()-c.getFollowsDone()==0) {
					out.println("<div class=\"noti notiPos\"><i class=\"material-icons lf\">done</i></div>");
				}else {
					out.println("<div class=\"noti\">"+(c.getFollows()-c.getFollowsDone())+"</div>");
				}
			}	
			out.println(		"</div>");
			out.println("	<div class=\""+clases2+"\"><div class=\"lockDiv\"><i class=\"material-icons lf blur\"> lock </i></div></div>");
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
				cliente.getResultados().add(new Resultado(r.getIdResultado(), r.getIdForo(), r.getEnlace(), r.getFecha(), r.getTipoRes(), r.getDescripcion(), r.getCategoriaResultado(), r.getEstado(), r.getAnchorR(), r.getWebForo()));
				cliente.getForos().add(new ForoGson(r.getIdForo(), r.getWebForo(), r.getTipoForo(), r.getDR(), r.getDA(), r.getTematica(), r.getDescripcion(), r.getCategoriaResultado(), r.getReqAprobacion(), r.getReqRegistro(), r.getApareceFecha(), r.getReutilizable()));
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
		out.println("<div class=\"infoClient\">");
		out.println("	<div class=\"nameClient\">"+cliente.getNombre()+"</div>");
		out.println("	<div class=\"urlClient\">"+cliente.getWeb()+"</div>");
		out.println("	<input id=\"dateSelected\" onchange=\"changeMonth()\" class=\"inCal\" data-lock=\"to\" type=\"text\" data-lang=\"es\" data-min-year=\"2017\"  data-large-mode=\"false\" data-format=\"F\" data-theme=\"calendar\"/>");
		out.println("	<script>$(\".datedropper\").remove();$('#dateSelected').dateDropper();</script>");
		out.println("</div>");
		out.println("<div class=\"keywordsClient\">");
		//out.println("	<div class=\"titleTable\">Keywords<div class=\"horDiv\"></div></div>");
		out.println("	<div id=\"results_Client\" class=\"contentTable\">");
		//tabla
		out.println("		<table id=\"tClientes\" class=\"table\">");
		out.println("			<thead><tr><th class=\"cabeceraTable cLink\">Link</th><th class=\"cabeceraTable cCateg\">Categoria</th><th class=\"cabeceraTable cWeb\">Web</th><th class=\"cabeceraTable cDest\">Destino</th><th class=\"cabeceraTable cTipo\">Tipo</th></tr></thead>");
		out.println("			<tbody>");
		int follows = cliente.getFollows();
		int followsDone = cliente.getFollows_done();
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
			Webservice ws = new Webservice();
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
		//int posicion = Integer.parseInt(request.getParameter("id"));
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
				String htmlCategorias = "		<ul id=\"selCat_"+id_resultado+"\" class=\"slCt effect7\">";
				String opCategoria = "-";
				int idCategoria = -1;
				for (int j = 0; j < categorias.size(); j++) {
					if(cliente.getResultados().get(i).getCategoria() == categorias.get(j).getIdCategoria() && categorias.get(j).getIdCategoria()!=0) {
						htmlCategorias += "			<li id=\""+categorias.get(j).getIdCategoria()+"\" class=\"liActive\" onclick=\"liSelectCat(this)\">"+categorias.get(j).getEnlace()+"</li>";
						opCategoria = categorias.get(j).getEnlace();
						idCategoria = categorias.get(j).getIdCategoria();
					}else if(categorias.get(j).getIdCategoria()!=0) {
						htmlCategorias += "			<li id=\""+categorias.get(j).getIdCategoria()+"\" onclick=\"liSelectCat(this)\">"+categorias.get(j).getEnlace()+"</li>";
					}				
				}
				htmlCategorias += "		</ul>";
				//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

				//Lista de todas las webs disponibles de la categoria seleccionada
				String htmlWebs="		<ul id=\"selWeb_"+id_resultado+"\" class=\"slCt slWeb effect7\">";
				htmlWebs += "		    </ul>";
				//Foro utilizado para este resultado
				int mweb = cliente.getResultados().get(i).getId_foro();



				out.println("<tr id=\""+id_resultado+"\">");
				out.println("	<td class=\"cLink\">");
				out.println("		<input class=\"inLink\" onchange=\"updateLink(this)\" type=\"text\" value=\""+cliente.getResultados().get(i).getEnlace()+"\">");
				out.println("	</td>");
				out.println("	<td class=\"tdCat cCateg pr\" onclick=\"selectCategory("+id_resultado+")\">");
				out.println("		<div class=\"tdCat\" id=\"dvCat_"+id_resultado+"\">");
				out.println("			<span id=\"spCat_"+id_resultado+"\" class=\"tdCat\">"+opCategoria+"</span>");
				out.println("			<i class=\"material-icons arrow\">arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		htmlCategorias);
				out.println("	</td>");
				out.println("	<td id=\"tdWeb_"+id_resultado+"\" class=\"tdCat tdWeb cWeb pr\" onclick=\"selectWeb("+id_resultado+")\">");
				out.println("		<div class=\"tdCat tdWeb\" id=\"dvWeb_"+id_resultado+"\">");
				out.println("			<span id=\"spWeb_"+id_resultado+"\" mweb=\""+mweb+"\" onmouseover=\"viewCampo(this)\" onclick=\"openUrl(this, event)\" class=\"tdCat tdWeb\">"+cliente.getResultados().get(i).getWeb_foro()+"</span>");
				out.println("			<i class=\"material-icons arrow\">arrow_drop_down</i>");
				out.println("		</div>");
				out.println(		htmlWebs);
				out.println("	</td>");
				out.println("	<td class=\"cDest\">"+cliente.getResultados().get(i).getFecha()+"</td>");

				if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("follow")) {
					out.println("	<td tipo=\"follow\" class=\"cTipo\"><i class=\"material-icons lf\">link</i></td>");
				}else if(cliente.getResultados().get(i).getTipo().equalsIgnoreCase("nofollow")) {
					out.println("	<td tipo=\"nofollow\" class=\"cTipo\"><i class=\"material-icons lnf\">link</i></td>");
				}
				out.println("</tr>");
			}
		}
	}
	private void guardarCategoria(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		//int id_cliente = Integer.parseInt(request.getParameter("id_cliente"));

		//Aplicamos los cambios en la bbdd y en el array
		Webservice ws = new Webservice();
		ws.updateResultado(id_resultado+"", "categoria", idCategoria+"" , "updateResultado.php");

		for (int i = 0,j=cliente.getResultados().size()-1; i < cliente.getResultados().size(); i++,j--) {
			if(cliente.getResultados().get(i).getId_resultado() == id_resultado ) {
				cliente.getResultados().get(i).setCategoria(idCategoria);
				i=cliente.getResultados().size();	
			}else if(cliente.getResultados().get(j).getId_resultado() == id_resultado ) {
				cliente.getResultados().get(j).setCategoria(idCategoria);
				i=cliente.getResultados().size();
			}
		}
		System.out.println("Insertado");
	}
	private void mostrarWebsCat(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));

		ArrayList<ForoGson> forosDisponibles = (ArrayList<ForoGson>) foros.clone();
		ArrayList<ForoGson> forosUsados = (ArrayList<ForoGson>) cliente.getForos().clone();

		for (int i = 0; i < forosDisponibles.size(); i++) {
			if(forosDisponibles.get(i).getCategoria()!=idCategoria) { 
				forosDisponibles.remove(i);
				i--;
			}else{

				for (int j = 0; j < forosUsados.size(); j++) {
					if(forosDisponibles.get(i).getIdForo()==forosUsados.get(j).getIdForo()) {
						System.out.println("Usado: -> id: "+forosDisponibles.get(i).getCategoria()+"          "+forosDisponibles.get(i).getWebForo());
						forosDisponibles.remove(i);
						forosUsados.remove(j);
						j=forosUsados.size();
						i--;
					}
				}
			}
		}

		//Lista de todas las webs disponibles de la categoria seleccionada
		//String htmlWebs="		<ul id=\"selWeb_"+id_tr+"\" class=\"slCt slWeb effect7\">";
		String htmlWebs="";
		for (int i = 0; i < forosDisponibles.size(); i++) {
			System.out.println("Disponible: -> id: "+forosDisponibles.get(i).getCategoria()+"          "+forosDisponibles.get(i).getWebForo());
			htmlWebs += "			<li id=\""+forosDisponibles.get(i).getIdForo()+"\" onclick=\"liSelectWeb(this.id,"+id_resultado+")\"><span onmouseover=\"viewCampo(this)\">"+forosDisponibles.get(i).getWebForo()+"</span></li>";
		}
		//htmlWebs += "		</ul>";

		out.println(		htmlWebs);
		System.out.println();
	}
	private void guardarWeb(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		System.out.println("Holaaaaaa");
		int id_foro = Integer.parseInt(request.getParameter("id_foro"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int elemtAnterior = Integer.parseInt(request.getParameter("elemtAnterior"));
		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "id_foro", id_foro+"" , "updateResultado.php");

		ForoGson foro = null;
		//buscamos el foro que hemos seleccionado
		for (int i = 0, j = foros.size()-1; i < foros.size(); i++,j--) {
			if(foros.get(i).getIdForo() == id_foro) { foro = foros.get(i); i = foros.size();
			}else if(foros.get(j).getIdForo() == id_foro) { foro = foros.get(j); i = foros.size();}
		}

		cliente.getForos().add(foro);
		for (int i = 0,j = cliente.getResultados().size()-1; i < cliente.getResultados().size(); i++, j--) {
			if(cliente.getResultados().get(i).getId_resultado() == id_resultado) {
				cliente.getResultados().get(i).setId_foro(foro.getIdForo()); i = cliente.getResultados().size();
			}else if(cliente.getResultados().get(j).getId_resultado() == id_resultado) {
				cliente.getResultados().get(j).setId_foro(foro.getIdForo()); i = cliente.getResultados().size();
			}
		}
		//si no hay ningun foro anteriormento no hacemos el for, asi nos ahorramos ese proceso
		if(elemtAnterior!=0) {
			//Recorremos el array de los foros del cliente para habilitar el foro que se ha desabilitado
			for (int i = 0, j = cliente.getForos().size()-1;  i < cliente.getForos().size();  i++, j--) {
				if(cliente.getForos().get(i).getIdForo() == elemtAnterior) {	
					cliente.getForos().remove(i);
					i= cliente.getForos().size();

				}else if(cliente.getForos().get(j).getIdForo() == elemtAnterior) {
					cliente.getForos().remove(j);
					i= cliente.getForos().size();
				}
			}
		}

		out.println("<span id=\"spWeb_"+id_resultado+"\" mweb=\""+id_foro+"\" class=\"tdCat tdWeb\">"+foro.getWebForo()+"</span>");
		out.println("<i class=\"material-icons arrow\">arrow_drop_down</i>");
		System.out.println("Insertado");
	}
	private void guardarLink(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String link = request.getParameter("link");
		String tipo = request.getParameter("tipo");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));

		ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
		for (int i = 0,j = cliente.getResultados().size()-1; i < cliente.getResultados().size(); i++, j--) {
			if(cliente.getResultados().get(i).getId_resultado() == id_resultado) {
				cliente.getResultados().get(i).setEnlace(link); i = cliente.getResultados().size();
			}else if(cliente.getResultados().get(j).getId_resultado() == id_resultado) {
				cliente.getResultados().get(j).setEnlace(link); i = cliente.getResultados().size();
			}
		}

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

		out.println("<div class=\"nameItem nameItem_select\">");
		out.println(	"<span class=\"nameItem sName nameItem_select\" onmouseover=\"viewAll(this)\">"+cliente.getNombre()+"</span>");
		out.println("</div>");
		out.println("<div class=\"dominioItem nameItem_select\">"+cliente.getWeb()+"</div>");
		
		if(cliente.getFollows()-cliente.getFollows_done()==0) {
			out.println("<div class=\"noti notiPos\"><i class=\"material-icons lf\">done</i></div>");
		}else {
			out.println("<div class=\"noti\">"+(cliente.getFollows()-cliente.getFollows_done())+"</div>");
		}
		
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
				if(j==0) {out.println("<div id=\""+i+"\" onclick=\"selectClient(this.id, 'lkc')\" class=\"item\"><div class=\"itemChild\"><div class=\"nameItem\">"+clientes.get(i).getNombre()+"</div><div class=\"dominioItem\">"+clientes.get(i).getWeb()+"</div></div></div>");j++;
				}else {out.println(   "<div id=\""+i+"\" onclick=\"selectClient(this.id, 'lkc')\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild\"><div class=\"nameItem\">"+clientes.get(i).getNombre()+"</div><div class=\"dominioItem\">"+clientes.get(i).getWeb()+"</div></div></div>");}
			}

		}

		out.println("<script type=\"text/javascript\">setC();</script>");

		System.out.println("Lista clientes cargada");

	}


	//--------------Dani----------------------------
		private void mostrarCategorias(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
			
			out = response.getWriter();
			out.println("	<div class=\"titleCategory\"><div class=\"titleInner\">Keywords<div class=\"horDiv wa\"><div id=\"addK\" class=\"addK\"><i class=\"material-icons addKi\">add</i></div><div onclick=\"searchKey(event)\"><div id=\"ipkey\" class=\"srchI\"><i class=\"material-icons addKi\">search</i><input id=\"searchK\" class=\"searchI\" type=\"text\" oninput=\"searchK()\"></div></div></div></div></div>");
			out.println("	<div class=\"infoCategory\"><div class=\"info\">"+categorias.size()+" Categorias</div></div>");
			out.println("		<div id=\"lkkI\" class=\"listItems\">");

			for (int i = 1; i < categorias.size(); i++) {
				if(i==0) {out.println("<div id=\""+categorias.get(i).getIdCategoria()+"\" onclick=\"selectCategoria(this.id)\" class=\"item\"><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+categorias.get(i).getEnlace()+"</div></div></div>");
				}else {out.println("<div id=\""+categorias.get(i).getIdCategoria()+"\" onclick=\"selectCategoria(this.id)\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+categorias.get(i).getEnlace()+"</div></div></div>");}
			}
			
			out.println("		</div>");
		}
		
		private void mostrarForos(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
			//String k = request.getParameter("keyword");
			int posicion = -1;
			try {
				posicion = Integer.parseInt(request.getParameter("id"));
				System.out.println("Categoria: "+categorias.get(posicion).getEnlace());
			}catch (NumberFormatException e) {
				System.out.println("ALGO NO FUE BIEN JODER");
			}
			
			System.out.println(posicion);
			out.println("<div class=\"infoClient\">");
			out.println("	<div class=\"nameClient\">"+categorias.get(posicion).getEnlace()+"</div>");
			out.println("	<div class=\"addK\" style=\"position: absolute; margin-left: 1250px; width: 110px; cursor: pointer;\">+ Nuevo Foro</div>");
			out.println("</div>");		
			out.println("<div class=\"keywordsClient\">");
			out.println("	<div id=\"results_Client\" class=\"contentTable\">");
			
			//COMIENZA LA TABLA
			out.println("		<table id=\"tCategorias\" class=\"table\">");
			out.println("			<thead><tr><th class=\"cabeceraTable cCWeb\">Web</th><th class=\"cabeceraTable cCTipo\">Tipo</th><th class=\"cabeceraTable cCDR\">DR</th><th class=\"cabeceraTable cCDA\">DA</th><th class=\"cabeceraTable cCTem\">Tematica</th><th class=\"cabeceraTable cCDesc\">Descripcion</th><th class=\"cabeceraTable cCApro\">Reutilizable</th><th class=\"cabeceraTable cCRegi\">Requiere</th></tr></thead>");
			out.println("			<tbody>");
			//System.out.println(k);
			
			for (int i = 0; i < foros.size(); i++) {
				if(foros.get(i).getCategoria()==posicion) {
					System.out.println(foros.get(i));
					
					//LISTA DESPLEGABLE TEMATICA------------------------------------			
					String htmlTematica = "		<ul id=\"selTem_"+i+"\" class=\"slCt slT effect7\">";
					String opTematica = foros.get(i).getTematica();
					//Esta linea de abajo cuenta el numero de palabras que hay en 'opTematica'
				    StringTokenizer st = new StringTokenizer(opTematica);
				    
				    boolean all = false;
				    System.out.println("N�mero de palabras->" + st.countTokens());	
					if (opTematica.equals("")) {
						opTematica = "Selecciona tematica";					
					}else if(st.countTokens() == tematicas.size()) {
						opTematica = "Todas";
					}
					
					htmlTematica += "			<li class=\"pretty p-default p-defaultALL p-round p-smooth p-plain pzero\" id=0 >";		
					if(opTematica.contains("Todas")) {
						htmlTematica += "				<input class=\"slT\" id=\"input_"+i+"\" type=\"checkbox\" onclick=\"selectAll(this,"+i+")\" checked>";
						all = true;
					}else
						htmlTematica += "				<input class=\"slT\" id=\"input_"+i+"\" type=\"checkbox\" onclick=\"selectAll(this,"+i+")\">";
					htmlTematica += "				<div class=\"state stateALL p-success-o\">";
					htmlTematica += "					<label>Seleccionar todas</label>";
					htmlTematica += "				</div>";
					htmlTematica += "			</li><br>";
					
					for (int j = 0; j < tematicas.size(); j++) {
							//htmlTematica += "			";
						htmlTematica += "			<li class=\"pretty p-default p-round p-smooth p-plain pzero\" id=\""+tematicas.get(j).getIdTematica()+"\" >";
						
						if(opTematica.contains(tematicas.get(j).getNombre())||all==true)
							htmlTematica += "				<input class=\"slT\" id=\""+tematicas.get(j).getNombre()+"\" type=\"checkbox\" onclick=\"liSelectTem("+i+")\" checked>";
						else
						htmlTematica += "				<input class=\"slT\" id=\""+tematicas.get(j).getNombre()+"\" type=\"checkbox\" onclick=\"liSelectTem("+i+")\">";
						
						htmlTematica += "				<div class=\"state p-success-o\">";
						htmlTematica += "					<label>"+tematicas.get(j).getNombre()+"</label>";
						htmlTematica += "				</div>";
						htmlTematica += "			</li><br>";
					}
					htmlTematica += "		</ul>";
					//---------------------------------------
					
					
					//-----------DESPLEGABLE REQUIERE--------------------------------------
					String htmlRequiere = "		<ul id=\"selReq_"+i+"\" class=\"slCt slT effect7\" style=\"width: 140px;\">";
					/*				Esto es el boton de Alberto
					<div class=\"itemStatus\">
						    <label class=\"switch\">
						        <input type=\"checkbox\"  checked>
						        <span class=\"slider round\"></span>
						    </label>
						</div>*/
					for (int k = 0; k < 3; k++) {
						htmlRequiere += "			<li class=\"pretty p-switch\" style=\"line-height: 1.4\" id=\""+k+"\" >";
						if(k==0&&foros.get(i).getReqAprobacion()==1) {
							htmlRequiere += "				<input class=\"slT\" id=\"R"+k+"\" type=\"checkbox\" onchange=\"cambiarRequiere(this.id,"+i+")\" checked>";
						}else if(k==1&&foros.get(i).getReqRegistro()==1) {
							htmlRequiere += "				<input class=\"slT\" id=\"R"+k+"\" type=\"checkbox\" onchange=\"cambiarRequiere(this.id,"+i+")\" checked>";
						}else if(k==2&&foros.get(i).getApareceFecha()==1) {
							htmlRequiere += "				<input class=\"slT\" id=\"R"+k+"\" type=\"checkbox\" onchange=\"cambiarRequiere(this.id,"+i+")\" checked>";
						}else
							htmlRequiere += "				<input class=\"slT\" id=\"R"+k+"\" type=\"checkbox\" onchange=\"cambiarRequiere(this.id,"+i+")\">";
						htmlRequiere += "				<div class=\"state\">";
						if(k==0) {
							htmlRequiere += "					<label>Aprobacion</label>";
						}else if(k==1) {
							htmlRequiere += "					<label>Registro</label>";
						}else if (k==2) {
							htmlRequiere += "					<label>Aparece Fecha</label>";
						}			
						htmlRequiere += "				</div>";
						htmlRequiere += "			</li><br>";
					}
					htmlTematica += "		</ul>";		
					//---------------------------------------------
					
					//------------DESPLEGABLE COLUMNA REUTILIZABLE---------
					String htmlReutilizable = "		<ul id=\"selReu_"+i+"\" class=\"slCt effect7\"  style=\"width: 20%; line-height: 1.2\">";
					String opReutilizable = foros.get(i).getReutilizable();
					if(opReutilizable.equals("")) {
						opReutilizable = "Selecciona";
					}
						htmlReutilizable += "			<li id=\"Reu1\" onclick=\"liSelectReu(this.id,"+i+")\">Para enlazar a UN cliente UNA sola vez</li><br>";		
						htmlReutilizable += "			<li id=\"Reu2\" onclick=\"liSelectReu(this.id,"+i+")\">Para enlazar a UN cliente VARIAS veces</li><br>";
						htmlReutilizable += "			<li id=\"Reu3\" onclick=\"liSelectReu(this.id,"+i+")\">Para enlazar a VARIOS clientes UNA sola vez</li><br>";
						htmlReutilizable += "			<li id=\"Reu4\" onclick=\"liSelectReu(this.id,"+i+")\">Para enlazar a VARIOS clientes VARIAS veces</li><br>";	
						htmlReutilizable += "		</ul>";
					//-----------------------------------------------------
					
					//------------DESPLEGABLE COLUMNA TIPO-----------------
					String htmlTipo = "		<ul id=\"selTipo_"+i+"\" class=\"slCt effect7\"  style=\"width: auto; line-height: 0.8\">";
					String opTipo = foros.get(i).getTipo();
					if(opTipo.equals("")) {
						opReutilizable = "Selecciona";
					}
						htmlTipo += "			<li id=\"Tipo1\" onclick=\"liSelectTipo(this.id,"+i+")\">follow</li><br>";
						htmlTipo += "			<li id=\"Tipo2\" onclick=\"liSelectTipo(this.id,"+i+")\">nofollow</li><br>";
						htmlTipo += "		</ul>";
					//-----------------------------------------------------
						
					//-----------DESPLEGABLE PARA EDITAR LA DESCRIPCION---
					String htmlDescripcion = "      <div class=\"tdWeb\" id=\"divDescripcion_"+i+"\" style=\"display:inline-block\">";	
						htmlDescripcion += "            <textarea id=\"desc_"+i+"\" onchange=\"cambiarDescripcion("+i+", spDesc_"+i+")\"  class=\"slCt slT effect7\" style=\"border-radius: 10px\" cols=\"40\" rows=\"3\">"+foros.get(i).getDescripcion()+"</textarea>";
						htmlDescripcion += "        </div>";
					String opDescripcion = foros.get(i).getDescripcion();
					//----------------------------------------------------
						
					//------------------------Insertamos las filas de la tabla de foros----------------------------------------------------------
					out.println("<tr id=\""+i+"\">");			

		
					//Columna WEB con animacion
					out.println("	<td class=\"cCWeb\">");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvWeb_"+i+"\" onclick=\"selectWeb("+i+")\">");
					out.println("			<input class=\"inLink\" type=\"text\" id=\"inputWeb_"+i+"\" onclick=\"openUrl(this, event)\" onchange=\"guardarWeb("+i+")\" value=\""+foros.get(i).getWebForo()+"\">");
					out.println("		</div>");
					out.println("	</td>");
					//Columna TIPO con el desplegable
					out.println("	<td class=\"tdCat cCTipo\" id=\"td_Tipo"+i+"\">");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvTipo_"+i+"\" onclick=\"selectTipo("+i+")\">");
					out.println("			<span id=\"spTipo_"+i+"\" class=\"tdCat\">"+opTipo+"</span>");
					out.println("		</div>");
					out.println(		htmlTipo);
					out.println("   </td>");
					//Columna DR
					out.println("	<td class=\"cCDR\" >");
					out.println("			<input class=\"inLink\" type=\"text\" id=\"inputDR_"+i+"\" onchange=\"guardarDR("+i+")\" value=\""+foros.get(i).getDR()+"\">");				
					out.println("   </td>");
					//Columna DA
					out.println("	<td class=\"cCDA\" >");
					out.println("			<input class=\"inLink\" type=\"text\" id=\"inputDA_"+i+"\" onchange=\"guardarDA("+i+")\" value=\""+foros.get(i).getDA()+"\">");				
					out.println("   </td>");
					
					//Columna TEMATICA con el desplegable-----------------
					out.println("   <td class=\"tdCat cCTem\" id=\"td_"+i+"\">");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvCat_"+i+"\" onclick=\"selectTematica("+i+")\">");
					out.println("			<span id=\"spTem_"+i+"\" onmouseover=\"viewCampo(this)\" class=\"tdCat\">"+opTematica+"</span>");
					out.println("		</div>");
					out.println(		htmlTematica);
					out.println("	</td>");
					//Fin columna tematica--------------------------------
					//Columna DESCRIPCION
					out.println("	<td class=\"cCDesc\" id=\"td_Desc"+i+"\" >");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvDesc_"+i+"\" onclick=\"editDescripcion("+i+")\">");
					out.println("			<span id=\"spDesc_"+i+"\" class=\"tdCat tdWeb\">"+opDescripcion+"</span>");
					out.println("		</div>");
					out.println(        htmlDescripcion);
					out.println("	</td>");
					
					//Columna REUTILIZABLE
					out.println("	<td class=\"tdCat cCReu pr\" id=\"td_Reu"+i+"\">");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvReu_"+i+"\" onclick=\"selectReutilizable("+i+")\">");
					out.println("			<span id=\"spReu_"+i+"\" onmouseover=\"viewCampo(this)\" class=\"tdCat\">"+opReutilizable+"</span>");
					out.println("		</div>");
					out.println(		htmlReutilizable);
					out.println("	</td>");
					//BORRAR
					//BORRAR
					
					//Columna REQUIERE
					out.println("	<td class=\"tdCat cCReq\" id=\"td_R"+i+"\">");
					out.println("		<div class=\"tdCat tdWeb\" id=\"dvReq_"+i+"\" style=\"margin-left: 20px;\">");
					out.println("			<i class=\"material-icons md-24 rositaGuay\" onclick=\"selectRequiere("+i+")\">settings</i>");
					out.println("		</div>");
					out.println(		htmlRequiere);
					out.println("	</td>");
					
					/*out.println("	<td class\"cCRegi\" >"+foros.get(i).getReq_registro()+"</td>");
					out.println("	<td class=\"cCFecha\">"+foros.get(i).getAparece_fecha()+"</td>");*/
		
					out.println("</tr>");		
					
					
				}
			

		        
			/*	if(keyword.contains(k)) {
					if(j==0) {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id)\" class=\"item\"><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");j++;
					}else {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id)\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");}
				}*/

			}
			
			out.println("			</tbody>");
			out.println("		</table>");
			out.println("	</div>");
			out.println("</div>");

			//out.println("<script type=\"text/javascript\">setK();</script>");

			System.out.println("Lista cargada");


		}
		
		private void guardarDescripcion(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			String descripcion = request.getParameter("cosa");
			int id_tr = Integer.parseInt(request.getParameter("id_tr"));
			
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "descripcion", descripcion, "updateForo.php");
			foros.get(id_tr).setDescripcion(descripcion);
			System.out.println("Insertado descripcion --> "+descripcion);
		}	
		
		private void guardarReutilizable(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			String reutilizable = request.getParameter("cosa");
			int id_tr = Integer.parseInt(request.getParameter("id_tr"));		
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "reutilizable", reutilizable, "updateForo.php");
			foros.get(id_tr).setReutilizable(reutilizable);
			System.out.println("Insertado reutiliable --> "+reutilizable);
		}
		
		private void guardarTematica(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			System.out.println(tematicas.size());
			String tematica = request.getParameter("cosa");
			int id_tr = Integer.parseInt(request.getParameter("id"));		
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "tematica", tematica, "updateForo.php");
			foros.get(id_tr).setTematica(tematica);
			System.out.println("Insertado tematica --> "+tematica);
			
		}
		
		private void guardarRequiere(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int id_tr = Integer.parseInt(request.getParameter("id_tr"));
			String id = request.getParameter("id");
			String state = request.getParameter("state");
			
			if(id.equals("R0")) {
				if(state.equals("true")) {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "req_aprobacion", "1", "updateForo.php");
					foros.get(id_tr).setReqAprobacion(1);
					System.out.println("Insertado req_aprobacion --> "+state);
				}else {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "req_aprobacion", "0", "updateForo.php");
					foros.get(id_tr).setReqAprobacion(0);
					System.out.println("Insertado req_aprobacion --> "+state);
				}
				
			}else if(id.equals("R1")) {
				if(state.equals("true")) {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "req_registro", "1", "updateForo.php");
					foros.get(id_tr).setReqRegistro(1);
					System.out.println("Insertado req_registro --> "+state);
				}else {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "req_registro", "0", "updateForo.php");
					foros.get(id_tr).setReqRegistro(0);
					System.out.println("Insertado req_registro --> "+state);
				}
			}else if(id.equals("R2")) {
				if(state.equals("true")) {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "aparece_fecha", "1", "updateForo.php");
					foros.get(id_tr).setApareceFecha(1);
					System.out.println("Insertado aparece_fecha --> "+state);
				}else {
					//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
					Webservice ws = new Webservice();
					ws.updateForo(id_tr+"", "aparece_fecha", "0", "updateForo.php");
					foros.get(id_tr).setApareceFecha(0);
					System.out.println("Insertado aparece_fecha --> "+state);
				}
			}
		}
		
		private void guardarWebForo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			String url = request.getParameter("cosa");
			int id_tr = Integer.parseInt(request.getParameter("id"));
			
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "web_foro", url, "updateForo.php");
			foros.get(id_tr).setWebForo(url);
			System.out.println("Insertado urlWebForo --> "+url);
		}
		
		private void guardarDA(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int da = Integer.parseInt(request.getParameter("cosa"));
			int id_tr = Integer.parseInt(request.getParameter("id"));

			
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "DA", da+"", "updateForo.php");
			foros.get(id_tr).setDA(da);
			System.out.println("Insertado DA --> "+da);
			
		}

		private void guardarDR(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			int dr = Integer.parseInt(request.getParameter("cosa"));
			int id_tr = Integer.parseInt(request.getParameter("id"));
			
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "DR", dr+"", "updateForo.php");
			foros.get(id_tr).setDR(dr);
			System.out.println("Insertado DR --> "+dr);
			
		}
		
		private void guardarTipo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
			String tipo = request.getParameter("cosa");
			int id_tr = Integer.parseInt(request.getParameter("id_tr"));		
			//Enviamos los datos a la base de datos y los reflejamos en nuestro array de foros
			Webservice ws = new Webservice();
			ws.updateForo(id_tr+"", "tipo", tipo, "updateForo.php");
			foros.get(id_tr).setTipo(tipo);
			System.out.println("Insertado tipo --> "+tipo);	
		}

	}




































