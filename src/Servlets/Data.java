package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import Objects.Gson.Cliente;
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

	@SuppressWarnings("static-access")
	private void cargarPage(HttpServletRequest request, HttpServletResponse response, int id_user, String name_user, String role) throws ServletException, IOException, ParseException {
		System.out.println(id_user+" "+name_user);
		ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");


		//ontenemos todas las categorias
		String json = ws.getJSON("getCategorias.php");
		ArrayList<CategoriaGson> categorias = new Gson().fromJson(json, new TypeToken<List<CategoriaGson>>(){}.getType());
		this.categorias.clear();
		if(categorias.get(0).getIdCategoria()==0) categorias.remove(0);
		this.categorias = categorias;
		
		json = ws.getJSON("getEmpleados.php");
		ArrayList<Empleado> empleados = new Gson().fromJson(json, new TypeToken<List<Empleado>>(){}.getType());
		this.empleados.clear();
		this.empleados = empleados;

		//obtenemos las tematicas
		json = ws.getJSON("getTematica.php");
		ArrayList<TematicaGson> tematicasGson = new Gson().fromJson(json, new TypeToken<List<TematicaGson>>(){}.getType());
		this.tematicas = tematicasGson;

		obtenemosForos();
		request.getRequestDispatcher("Data_Enlaces").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();

		String metodo = request.getParameter("metodo");

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
		}else if(metodo.equals("cleanBlocksUser")) {
			/*ws.desbloquearEditando(0,id_user, "desbloquearEditando.php");
			for (ClienteGson c : this.clientes) {
				c.setEditando(0);
			}*/
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


	
}