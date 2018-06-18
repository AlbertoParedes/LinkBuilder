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
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Classes.ReadFactura;
import Classes.Webservice;
import Objects.CategoriaObjeto;
import Objects.Cliente2;
import Objects.Enlace;
import Objects.Foro2;
import Objects.Resultado;
import Objects.Tematica;
import Objects.Gson.CategoriaGson;
import Objects.Gson.ClienteGson;
import Objects.Gson.Empleado;
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
	private Cliente2 cliente = new Cliente2();
	private ArrayList<ForoGson> foros = new ArrayList<ForoGson>();
	public static ArrayList<CategoriaGson> categorias = new ArrayList<CategoriaGson>();
	private ArrayList<TematicaGson> tematicas = new ArrayList<TematicaGson>();
	public static ArrayList<Empleado> empleados = new ArrayList<Empleado>();


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
		
		json = ws.getJSON("getEmpleados.php");
		ArrayList<Empleado> empleados = gson.fromJson(json, new TypeToken<List<Empleado>>(){}.getType());
		this.empleados.clear();
		this.empleados = empleados;
		
		

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
		
		/*
		else if(metodo.equals("ckc")) { 
			try {checkClientEdition(request, response, out,id_user, role);} catch (ParseException e) {}
		}else if (metodo.equals("lkc")) {
			try {mostrarResultados(request, response, out, role);} catch (ParseException e) {e.printStackTrace();}
		}*/
		
		
		if(metodo.equals("chd")) {
			guardarDestino(request, response, out);
		}else if(metodo.equals("guardarForoCompleto")) {
			guardarForoCompleto(request, response);
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
		}else if(metodo.equals("addDestino")) {
			addDestino(request, response, out);
		}
		
		//anadir facturas de los enlaces
		else if(metodo.equals("uploadFactura")) {
			subirNuevaFactura(request, response, id_user, out);
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
		dominio = dominio.substring(0, dominio.lastIndexOf("."));
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

		String form="";
		if(id_categoria==18) {
			form ="	<form class='formNewFactura' name='uploadFactura' id='uploadFactura' method='post' action='Data' enctype='multipart/form-data' autocomplete='off'>" + 
					"			<label class='fileContainer'>" + 
					"				<input onchange='uploadExcelFactura(this)' type='file' class='inputAddFactura' name='excelFactura' id='excelFactura' value=''/>" + 
					"			</label>" + 
					"			<input type='hidden' name='nombre' value='' />" + 
					"			<input type='hidden' name='metodo' value='uploadFactura' />" + 
					"			<i class='material-icons'>file_upload</i>" + 
					"		</form>";
		}
		
		
		
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
			String htmlTematica = 	"<ul size="+tematicas.size()+" class='slCt slT effect7 ulTem pop_up'>";
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
			String htmlRequiere="<ul class='slCt slT effect7 ulReq pop_up'>";
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
			String htmlReutilizable = "	<ul class='slCt effect7 pop_up'>";
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
			out.println(		"<ul class='slCt effect7 ulTipoForo pop_up'>");
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
							+ "<ul class='slCt effect7 ulTipoForo nuevaWeb pop_up'>"
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

				out.println("<div id='"+c.getIdCliente()+"' onclick='enlaces_SelectClient(this.id)' class='item'>");
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
		ArrayList<CategoriaGson> categorias = gson.fromJson(json, new TypeToken<List<CategoriaGson>>(){}.getType());
		this.categorias.clear();
		if(categorias.get(0).getIdCategoria()==0) {
			categorias.remove(0);
		}
		this.categorias = categorias;
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
		String htmlReutilizable = "	<ul class='slCt effect7 nuevaWeb pop_up'>";
		htmlReutilizable += "			<div class='divSeparar'>Enlazar a:</div>";
		htmlReutilizable += "			<li contenido='un_cliente_una_vez' onclick='guardarReutilizable(this)'>UN cliente UNA vez</li>";		
		htmlReutilizable += "			<li contenido='un_cliente_varias_veces' onclick='guardarReutilizable(this)'>UN cliente VARIAS veces</li>";
		htmlReutilizable += "			<li contenido='varios_clientes_una_vez' onclick='guardarReutilizable(this)'>VARIOS clientes UNA vez</li>";
		htmlReutilizable += "			<li contenido='varios_clientes_varias_veces' onclick='guardarReutilizable(this)'>VARIOS clientes VARIAS veces</li>";	
		htmlReutilizable += "		</ul>";
		return htmlReutilizable;
	}
	private String htmlTemaicas() {
		String htmlTematica = 	"<ul size="+tematicas.size()+" class='slCt slT effect7 ulTem  nuevaWeb pop_up'>";
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
		String htmlRequiere="<ul class='slCt slT effect7 ulReq  nuevaWeb pop_up'>";
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
		
		String listaEmpleados = "";
		
		//web cliente
		String onInputWebCliente="",onChangeWebCliente="";
		//nombre cliente
		String onInputNombreCliente="",onChangeNombreCliente="";
		//servicio
		String onClickServicio="", onClickGuardarServicio="";
		//follows
		String onChangeGuardarFollows="";
		//nofollows
		String onChangeGuardarNoFollows="";
		//anchor
		String onChangeGuardarAnchor="";
		//blog
		String onChangeGuardarBlog="";
		//idioma
		String onChangeGuardarIdioma="";
		//empleados
		String onClickOpenEmpleados="";
		//destino
		String onClickOpenDestinos = "", onClickAddDestino="",onClickDeleteDestino="";
		
		
		String onInputGuardar=""; 
		
		for (Empleado e : empleados) {
			if(user_role.equals("super_admin")) {
				listaEmpleados += "<li>"+e.getName()+"</li>";
				onInputWebCliente="oninput='showGuardar()'";onChangeWebCliente="onchange='guardarWebCliente(this)'";
				onInputNombreCliente="oninput='showGuardar()'";onChangeNombreCliente="onchange='guardarNombreCliente(this)'";
				onClickServicio = "onclick='openServicio(this)'";
				onInputGuardar = "oninput='showGuardar()'";
				onChangeGuardarFollows = "onchange='guardarFollows(this)'";
				onChangeGuardarNoFollows = "onchange='guardarNoFollows(this)'";
				onChangeGuardarAnchor="onchange='guardarAnchorCliente(this)'";
				onChangeGuardarBlog="onchange='guardarBlog(this)'";
				onChangeGuardarIdioma="onchange='guardarIdioma(this)'";
				onClickOpenEmpleados = "onclick='opentUser(this)'";
				onClickOpenDestinos = "onclick='openDestinos(this)'";
				onClickAddDestino ="onclick='addDestino(this)'";
				onClickDeleteDestino="onclick='deleteDestino(this)'";
				onClickGuardarServicio="onclick='guardarServicio(this)'";
			}else if(user_role.equals("free_admin")) {
				if(e.getCategoria().equals("free")) {
					listaEmpleados += "<li>"+e.getName()+"</li>";
					
				}
				
			}else if(user_role.equals("free_user")) {
				if(e.getCategoria().equals("free")) {
					listaEmpleados += "<li>"+e.getName()+"</li>";
					
				}
			}else if(user_role.equals("paid_user")) {
				if(e.getCategoria().equals("paid")) {
					listaEmpleados += "<li>"+e.getName()+"</li>";
					
				}
			}
		}
		
		//obtenemos todos los clientes que se les ha asignado a este empleado
		String json = ws.getClientsByUser(id_user+"", user_role, "getClientsByUser.php");
		Gson gson = new Gson();
		ArrayList<ClienteGson> clientesGson = gson.fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());
		
		//
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
		out.println("			<thead><tr><th class='cabeceraTable select_client '>"+selectDelete+"</th><th class='cabeceraTable cCWebCliente'>Web</th><th class='cabeceraTable cCNombre'>Nombre</th><th class='cabeceraTable cCTipo'>Servicio</th><th class='cabeceraTable cFollow'>Follow</th><th class='cabeceraTable cNoFollow'>NoFollow</th><th class='cabeceraTable anchorC'>Anchor</th><th class='cabeceraTable cCBlog'>Blog</th><th class='cabeceraTable cCIdioma'>Idioma</th><th class='cabeceraTable cCUser'>User</th><th class='cabeceraTable cell_destino'>Destinos</th></tr></thead>");
		out.println("			<tbody>");
		String htmlServicio="",htmlUserFinal="";
		
		ArrayList<ClienteGson> clientes = new ArrayList<ClienteGson>();
		int idCliente = -1;
		//Insertar las urls a atacar en un unico cliente ya que si tiene 
		for (int c = 0; c < clientesGson.size(); c++) {
			ClienteGson cliente = clientesGson.get(c);
			if(idCliente!=clientesGson.get(c).getIdCliente()) {
				ArrayList<String> destinos = new ArrayList<>();
				destinos.add(cliente.getUrlAAtacar());
				clientes.add(new ClienteGson(
						cliente.getIdCliente(),
						cliente.getWeb(), 
						cliente.getNombre(), 
						cliente.getServicio(), 
						cliente.getFollows(), 
						cliente.getNofollows(), 
						cliente.getAnchor(), 
						cliente.getBlog(), 
						cliente.getIdioma(), 
						cliente.getFollowsDone(), 
						cliente.getNofollowsDone(), 
						cliente.getLinkbuilder(), 
						cliente.getEditando(), 
						cliente.getUserEditando(), 
						cliente.getEnlacesDePago(),
						destinos));
			}else {
				clientes.get(clientes.size()-1).getUrlsAAtacar().add(cliente.getUrlAAtacar());
			}
		}
		
		
		for (int c = 0; c < clientes.size(); c++) {
			ClienteGson cliente = clientes.get(c);
			htmlUserFinal="";
			int id_cliente = cliente.getIdCliente();//este es el ID REAL DE LA BBDD
			//------------DESPLEGABLE COLUMNA SERVICIO-----------------
			String claseLite="",clasePro="",clasePremium="", claseMedida="", readonly="";
			String opServicio = cliente.getServicio();
			if(cliente.getServicio().equals("lite"))         { opServicio = "SEO Lite"; 	  claseLite="class='liActive'";	  readonly="readonly='true'";}
			else if(cliente.getServicio().equals("pro"))    { opServicio = "SEO Pro";       clasePro="class='liActive'";      readonly="readonly='true'";}
			else if(cliente.getServicio().equals("premium")){ opServicio = "SEO Premium";   clasePremium="class='liActive'";  readonly="readonly='true'";}
			else if(cliente.getServicio().equals("medida")) { opServicio = "SEO a medida";  claseMedida="class='liActive'"; }
			htmlServicio = "			<li id='lite' "+claseLite+" data-follows='3' data-nofollows='5' "+onClickGuardarServicio+">SEO Lite</li>";
			htmlServicio += "			<li id='pro' "+clasePro+" data-follows='4' data-nofollows='10' "+onClickGuardarServicio+">SEO Pro</li>";
			htmlServicio += "			<li id='premium' "+clasePremium+" data-follows='6' data-nofollows='15' "+onClickGuardarServicio+">SEO Premium</li>";
			htmlServicio += "			<li id='medida' "+claseMedida+" data-follows='0' data-nofollows='0' "+onClickGuardarServicio+"'>SEO a medida</li>";
			//------------DESPLEGABLE COLUMNA USER-----------------
			String opUser = cliente.getLinkbuilder();
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
			if(Integer.parseInt(cliente.getBlog())==0) blogChecked="";
			
			
			
			
			

			out.println("<tr id='"+id_cliente+"' posicion='"+c+"' class='pr'>");
			out.println("	<td class='select_client'>");
			out.println("		<div class='pretty p-icon p-smooth div_select_cliente'>");
			out.println("			<input class='slT' type='checkbox'/>");
			out.println("			<div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div>");
			out.println("		</div>");
			out.println("	</td>");
			//Columna WEB
			out.println("	<td class='cCWebCliente'>");
			out.println("		<input class='inLink' "+onInputWebCliente+" type='text' "+onChangeWebCliente+" value='"+cliente.getWeb()+"'>");
			out.println("   </td>");
			//Columna NOMBRE
			out.println("	<td class='cCNombre'>");
			out.println("		<input class='inLink' "+onInputNombreCliente+" type='text' "+onChangeNombreCliente+" value='"+cliente.getNombre()+"'>");
			out.println("	</td>");
			//Columna SERVICIO
			out.println("	<td class='cCTipo pr' "+onClickServicio+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span class='tdCat'>"+opServicio+"</span>");
			out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
			out.println("		</div>");		
			out.println(		"<ul class='slCt effect7 pop_up'>"+htmlServicio+"</ul>");
			out.println("   </td>");
			//Columna FOLLOW
			out.println("	<td class='cFollow'>");
			out.println("		<input class='inLink' "+onInputGuardar+" type='text' "+onChangeGuardarFollows+" value='"+cliente.getFollows()+"' "+readonly+">");				
			out.println("   </td>");			
			//Columna NOFOLLOW
			out.println("   <td class='cNoFollow'>");
			out.println("		<input class='inLink' "+onInputGuardar+" type='text' "+onChangeGuardarNoFollows+" value='"+cliente.getNofollows()+"' "+readonly+">");	
			out.println("	</td>");	
			//Columna ANCHOR
			out.println("	<td class='anchorC'>");
			out.println("			<input type='text' "+onInputGuardar+" class='inLink' "+onChangeGuardarAnchor+" value='"+cliente.getAnchor()+"'>");
			out.println("	</td>");
			//Columna BLOG
			out.println("	<td class='tdCat cCBlog pr'>");
			out.println("		<div class='tdCat tdWeb ckBlog'>");
			out.println("			<label  class='switch'>");
			out.println("			<input class='slT' type='checkbox' "+onChangeGuardarBlog+" "+blogChecked+">");
			out.println("			<span class='slider round'></span>");
			out.println("			</label>");
			out.println("		</div>");
			out.println("	</td>");
			//Columna IDIOMA
			out.println("	<td class='tdCat cCIdioma'>");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<input "+onInputGuardar+" class='inLink' type='text' "+onChangeGuardarIdioma+" value='"+cliente.getIdioma()+"'>");				
			out.println("		</div>");
			out.println("	</td>");
			//Columna USER
			out.println("	<td class='tdCat cCUser pr' "+onClickOpenEmpleados+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span data-list-user='"+opUser+"' class='tdCat' type='text'>"+name+"</span>");	
			out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
			out.println("		</div>");
			out.println("		<ul class='slCt effect7 pop_up'>"+listaEmpleados+"</ul>");
			out.println("	</td>");
			out.println("	<td class='tdCat cell_destino pr text_center' "+onClickOpenDestinos+">");
			out.println("			<i class='material-icons inner_pop_up'> directions </i>");
			out.println("			<div data-id='lista_destinos' class='div_destinos pop_up effect7 inner_pop_up pop_up_move2left  text_left' >");
			out.println("				<div class='nuevo_destino inner_pop_up'>");
			out.println("					<span class='inner_pop_up'>Destino: </span><input type='text' class='inLink inner_pop_up' value='' placeholder='Introduce una nueva url a atacar'><i "+onClickAddDestino+" class='material-icons inner_pop_up'>add</i>");
			out.println("				</div>");
			out.println("				<ul class='scroll_115 pdd_v_12 inner_pop_up'>");
			for (String destino : cliente.getUrlsAAtacar()) {
				out.println("				<li class='pdd_h_17 pr inner_pop_up' ><span>"+destino+"</span><i "+onClickDeleteDestino+" class='material-icons inner_pop_up'> remove </i></li>");
			}
			out.println("				</ul>");
			out.println("			</div>");
			out.println("		</td>");
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
		out.println(					"<ul class='slCt effect7 nuevaWeb pop_up'>"+htmlServicio+"</ul>");
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
		out.println("					<ul class='slCt effect7 nuevaWeb pop_up'>"+htmlUserFinal+"</ul>");
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
	
	private void addDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String id_cliente = request.getParameter("id_cliente");
		String url = request.getParameter("url");
		
		ws.addDestino(id_cliente, url, "addDestino.php");
		
		System.out.println(id_cliente +"  "+url);
	}
	
	@SuppressWarnings("unchecked")
	private void subirNuevaFactura(HttpServletRequest request, HttpServletResponse response, int id_user, PrintWriter out) throws IOException, ServletException {
		
		
		
		String nameFile = request.getParameter("nombre");
		System.out.println(nameFile);
		ArrayList<Enlace> enlaces = new ArrayList<Enlace>();
		ReadFactura rf = new ReadFactura();
		if(nameFile.endsWith(".xlsx")) enlaces = rf.readExcel(request);	
		else System.out.println("No se encontro ningun archivo");
		
		String contenidoTabla="";
		JSONArray json = new JSONArray();
		for (Enlace e : enlaces) {
			
			//comprobamos que el medio no se encuentra ya en la base de datos
			String dominio = e.getMedio().replace("http://", "").replace("https://","").replace("www.","");
			dominio = dominio.substring(0, dominio.lastIndexOf("."));
			String respuesta = ws.getForoByPieceDominio(dominio, "getForoByPieceDominio.php");
			System.out.println(respuesta);
			Gson gson = new Gson();
			ArrayList<ForoGson> forosGson = gson.fromJson(respuesta, new TypeToken<List<ForoGson>>(){}.getType());
			
			boolean coincidenciaExacta =false;
			String claseStatus ="",onclick="";
			String coincidenciaParcial="<ul class='slCt effect7 pop_up'><i onclick='resetEnlaceFactura(this)' class='material-icons crossReset'> cached </i>";
			
			if(forosGson.size()>0) {//si obtenemos algun resulatado analizaremos cuan se repite y cual se le parece
				for (ForoGson f : forosGson) {//recorremos los foros que nos devuelve tras coparar si exite ese medio o se encuentran coincidencias
					if(f.getWebForo().equals( e.getMedio())) {
						coincidenciaExacta=true;
						coincidenciaParcial="<span>"+e.getMedio()+"</span>";
						claseStatus = "sOK";
						break;
					}else {//rellenamos el ul
						coincidenciaParcial += "<li class='pd_rigth_28 pr' onclick='selectCoincidencia(this)' data-id-foro='"+f.getIdForo()+"' data-webForo='"+f.getWebForo()+"'>" 
						+ "<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'>"+f.getWebForo()+"</span>"
						+"						<div class='req replace_input_medio' onclick='stopPropagation()' onmouseover='showPopUpCoincidencias(this)' onmouseout='hidePopUpCoincidencias(this)'>"
						+ "							<label class='switch req'>"
						+ "								<input type='checkbox' onchange='replaceInputMedio(this)'>"
						+ "								<span class='slider round req'></span>"
						+ "							</label>"
						+ "						</div>"
						+"						</li>";
						claseStatus = "s_pendiente_opciones";
					}
				}
				if(coincidenciaExacta==false) {
					coincidenciaParcial="<div class='tdCat tdWeb pr'><span class='tdCat' data-origen='"+e.getMedio()+"'>"+e.getMedio()+"</span><i class='material-icons arrow'>arrow_drop_down</i></div>"+coincidenciaParcial
							+ "							<div class='popup effect7 pop_up_coincidencia'>"
							+ "								<span class='mensaje_coincidencia'></span>"
							+ "							</div>"
							+ "</ul>";
					onclick = "onclick='openOpcionesNuevaFactura(this)'";
				}
			}else {
				coincidenciaParcial=e.getMedio();
				claseStatus = "s_nuevo_opciones";
			}
			contenidoTabla+="<tr>"
					+ "			<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>"
					+ "			<td class='pr' "+onclick+">"
					+ 				coincidenciaParcial
					+ "			</td>"
					+ "			<td class='f_cantidad'>"
					+ "				<span>"+e.getCantidad()+"</span>"
					+ "			</td>"
					+ "			<td class='f_total'>"
					+ "				<span>"+e.getTotal()+"&euro;</span>"
					+ "			</td>"
					+ "		</tr>";
		}
		
		out.print("<div class='tableCellContent'>");
		out.print("		<div class='icono_confirmacion'><i class='material-icons'> cloud_upload </i></div>");
		out.print("		<div class='divTitle'>");
		out.print("			<div class='titleConfirmacion'>Importar factura</div>");
		out.print("		</div>");
		out.print("		<div class='separator'></div>");
		out.print("		<div class='div_pregunta_confirmacion'>");
		out.print("			<div>");
		out.print("				<div class='text_pregunta'>&iquest;Est&aacute;s seguro de que quieres importar la factura <strong>PL-3005</strong> &#63;</div>");
		out.print("			</div>");
		out.print("		</div>");
		
		
		//anadir
		out.println(	"<table id='tablaNewFactura' class='table'>");
		out.println("			<thead><tr>"
				+ "					<th class='cabeceraTable cStatus info_div' onmouseover='showPopUp(this)' onmouseout='hidePopUp(this)'>"
				+ "						<div class='divStatus sPendiente'></div>"
				+ "						<div class='popup effect7'>"
				+ "							<div class='pr pd_left_30'><div class='divStatus sOK p_a p_cicles'></div>Existe</div>"
				+ "							<div class='pr pd_left_30'><div class='divStatus s_nuevo_opciones p_a p_cicles'></div>Nuevo</div>"
				+ "							<div class='pr pd_left_30'><div class='divStatus s_pendiente_opciones p_a p_cicles'></div>Coincidencias</div>"
				+ "						</div>"
				+ "					</th>"
				+ "					<th class='cabeceraTable f_Medio'>Medio</th>"
				+ "					<th class='cabeceraTable f_cantidad'>Cantidad</th>"
				+ "					<th class='cabeceraTable f_total'>Total</th>"
				+ "				</tr></thead>");
		out.println("			<tbody>"+contenidoTabla+"</tbody>");
		out.println(	"</table>");
		
		out.print("		<div class='botonesConfirmacion'>");
		out.print("			<div>");
		out.print("				<span class='btnsConfi cancelBotonConfirmacion'>Cancelar</span>");
		out.print("				<span class='btnsConfi aceptarBotonConfirmacion'>Aceptar</span>");
		out.print("			</div>");
		out.print("		</div>");
		out.print("</div>");
		
		System.out.println(json.toString());
		//out.print(json);
		
	}
	
}