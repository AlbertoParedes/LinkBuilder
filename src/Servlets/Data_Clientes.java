package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gson.reflect.TypeToken;

import Classes.OrdenarObjetos;
import Classes.ParsingJson;
import Classes.ReadFactura;
import Classes.Webservice;
import Objects.Enlace;
import Objects.Gson.Cliente;
import Objects.Gson.ClienteGson;
import Objects.Gson.Empleado;
import Objects.Gson.ForoGson;

/**
 * Servlet implementation class Data_Clientes
 */
@WebServlet("/Data_Clientes")
public class Data_Clientes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Webservice ws = new Webservice();
	private ParsingJson pj = new ParsingJson();
	private OrdenarObjetos ob = new OrdenarObjetos();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private Empleado empleado = new Empleado();
	private String listaEmpleados = "";

	public Data_Clientes() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Do get CLIENTES");

		
		Empleado empleado = (Empleado) request.getAttribute("empleado");
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("empleado", empleado);
		
		System.out.println("Yo soy: "+empleado.getName());

		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(empleado.getId()+""));
		String json = ws.clientes(wbList, "getClientes", "clientes.php");
		String[] jsonArray = json.split(";;");

		System.out.println("Traza 1");
		this.clientes.clear();
		clientes = pj.parsearClientesMap(jsonArray[1]);
		ob.clientesByDomain(clientes);
		System.out.println("Traza 2");

		request.setAttribute("name_user", empleado.getName());
		request.setAttribute("ventana",empleado.getPanel());
		request.getRequestDispatcher("Data.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		Empleado empleado = (Empleado) session.getAttribute("empleado");
		
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();
		String metodo = request.getParameter("metodo");
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();
		
		

		if(metodo.equals("cargarListaClientes")) {
			try {cargarListaClientes(request, response, out, id_user,role, "web", empleado);} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("guardarValoresCliente")) {
			guardarValoresCliente(request, response, out);
		}else if(metodo.equals("guardarNuevoCliente")) {
			guardarNuevoCliente(request, response);
		}else if(metodo.equals("eliminarCliente")) {
			eliminarCliente(request, response, out);
		}else if(metodo.equals("addDestino")) {
			addDestino(request, response, out);
		}else if(metodo.equals("guardarEmpleado")) {
			guardarEmpleado(request, response, out); 
		}else if(metodo.equals("removeDestino")) {
			removeDestino(request, response, out);
		}else if((metodo.equals("ordenarListaClientes"))) {
			try {ordenarListaClientes(request, response, out, id_user,role);} catch (ParseException e) {e.printStackTrace();}
		}else if((metodo.equals("filtrarLista"))) {
			filtrarLista(request, response, out);
		}else if(metodo.equals("modificarEnlacesEmpleado")) {
			modificarEnlacesEmpleado(request, response, out); 
		}

	}
	
	private void modificarEnlacesEmpleado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		int idCliente = 0;
		String json = request.getParameter("json");
		System.out.println(json);
		Object jsonObject =JSONValue.parse(json.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			int id_cliente = Integer.parseInt(row.get("id_cliente").toString());
			int id_empleado = Integer.parseInt(row.get("id_empleado").toString());
			int valor = Integer.parseInt(row.get("valor").toString());
			String tipo_empleado = row.get("tipo_empleado").toString();
			idCliente = id_cliente;

			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente+"", id_empleado+"", valor+"", Data.fecha, 0+""));
			ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
			
			wbList = new ArrayList<>(Arrays.asList(id_cliente+"",id_empleado+"",tipo_empleado,valor+""));
			ws.clientes(wbList, "updateEmpleadoEnlaces", "clientes.php");
		}
		if(idCliente>0) {
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(idCliente+"", 0+"", 0+"", Data.fecha, 1+""));
			ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
		}
		
		
	}

	private void filtrarLista(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String json = request.getParameter("json");
		String servicios="", usuarios="", estados="";
		Object jsonObject =JSONValue.parse(json.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			String tipo = row.get("tipo").toString(), valor = row.get("valor").toString();
			if(tipo.equals("servicio"))servicios += "{"+valor+"}";
			if(tipo.equals("empleado"))usuarios += "{"+valor+"}";
			if(tipo.equals("estado"))estados += "{"+valor+"}";
		}
		System.out.println("filtrar por: -> "+json);
		
		System.out.println("servicio: "+servicios);
		System.out.println("empleado: "+usuarios);
		System.out.println("estado: "+estados);
		
		for (int c = 0; c < clientes.size(); c++) {
			Cliente cliente = clientes.get(c);
			boolean mostrarServicio = false, mostrarUsario=false,mostrarEstado=false;
			if(usuarios.equals("")) {
				mostrarUsario = true;
			}else {
				for(Map.Entry<String, Empleado> e : cliente.getEmpleados().entrySet()) {
				    if(usuarios.contains("{"+e.getValue().getName().toLowerCase()+"}")) {
				    	mostrarUsario = true;
				    	break;
				    }
				}
			}
			if(servicios.contains("{"+cliente.getServicio()+"}") || servicios.equals("")) mostrarServicio = true;
			if(estados.contains("{"+cliente.getStatus()+"}") || estados.equals("")) mostrarEstado = true;
			if(mostrarServicio && mostrarUsario && mostrarEstado) setContenidoTabla(empleado, c, cliente, out);
		}
	}

	private void ordenarListaClientes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role) throws IOException, ParseException {

		String campo = request.getParameter("campo");
		String tipo = request.getParameter("tipo");

		if(campo.equals("web")) {
			if(tipo.equals("asc")) ob.clientesByDomain(clientes);
			else ob.clientesByDomainDes(clientes);
		}else if(campo.equals("nombre")) {
			if(tipo.equals("asc")) ob.clientesByName(clientes);
			else ob.clientesByNameDes(clientes);
		}else if(campo.equals("servicio")) {
			if(tipo.equals("asc")) ob.clientesByServicio(clientes);
			else ob.clientesByServicioDes(clientes);
		}else if(campo.equals("user")) {
			if(tipo.equals("asc")) ob.clientesByUser(clientes);
			else ob.clientesByUserDes(clientes);
		}
		
		filtrarLista(request, response, out);
	}

	private void removeDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {

		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();

		String id_cliente = request.getParameter("id_cliente");
		String url = request.getParameter("url");

		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente,url));
		ws.clientes(wbList, "removeDestino", "clientes.php");

		out.print(obj);

		System.out.println("Eliminado");

	}

	private void guardarEmpleado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {

		String id_cliente = request.getParameter("id_cliente");
		int posicion = Integer.parseInt(request.getParameter("posicion"));
		int enlacesDisponibles = Integer.parseInt(request.getParameter("enlacesDisponibles"));
		String estado = request.getParameter("estado");
		String id_empleado = request.getParameter("id_empleado");
		String tipo_empleado = request.getParameter("tipo_empleado");
		String nameEmpleado = request.getParameter("nameEmpleado");
		String json = request.getParameter("json");
		
		
		Object jsonObject =JSONValue.parse(json.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			int idEmpleado = Integer.parseInt(row.get("id_empleado").toString()), enlaces = Integer.parseInt(row.get("enlaces").toString());
			String tipoEmpleado = row.get("tipo_empleado").toString();
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente+"", idEmpleado+"", enlaces+"", Data.fecha, 0+""));
			ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
		}
		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente+"", 0+"", 0+"", Data.fecha, 1+""));
		ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
		
		
		if(estado.equals("true")) {
			
			wbList = new ArrayList<>(Arrays.asList(id_cliente,estado, id_empleado, tipo_empleado,Data.fecha,enlacesDisponibles+""));
			ws.clientes(wbList, "insertOrDeleteClienteEmpleado", "clientes.php");
			
			out.println("<div data-id-empleado='"+id_empleado+"' data-tipo-empleado='"+tipo_empleado+"' class='pr mg_top_10'>"
					+ "		<span>"+nameEmpleado+"</span>"
					+ "		<div class='div_n_follows'>"
					+ "			<div class='n_follows_remove' onclick='modifyEnlacesEmpleado(this)' data-action='decrease'>"
					+ "				<i class='material-icons f_size_17 noselect'> remove </i>"
					+ "			</div>"
					+ "			<input class='input_n_follows inLink noselect' type='number' data-id-empleado='"+id_empleado+"' data-tipo-empleado='"+tipo_empleado+"' value='"+enlacesDisponibles+"' readonly=''>"
					+ "			<div class='n_follows_add' onclick='modifyEnlacesEmpleado(this)' data-action='increase'>"
					+ "				<i class='material-icons f_size_17 noselect'> add </i>"
					+ "			</div>"
					+ "		</div>"
					+ "	</div>");
		}else {
			wbList = new ArrayList<>(Arrays.asList(id_cliente,estado, id_empleado, tipo_empleado,Data.fecha,0+""));
			ws.clientes(wbList, "insertOrDeleteClienteEmpleado", "clientes.php");
		}
		
		System.out.println(json);

	}

	private void guardarValoresCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String valor = request.getParameter("valor");
		String campo = request.getParameter("campo");
		String id_cliente = request.getParameter("id_cliente");
		int posicion = Integer.parseInt(request.getParameter("posicion"));
		System.out.println(valor+"  "+campo+"   "+id_cliente);
		ws.updateCliente(id_cliente, campo, valor, "updateCliente.php");
		
		if(campo.equals("web")) {
			clientes.get(posicion).setWeb(valor);
			clientes.get(posicion).setDominio(valor.replace("https://", "").replace("http://", "").replace("www.", ""));
		}else if(campo.equals("nombre")) {
			clientes.get(posicion).setNombre(valor);
		}else if(campo.equals("follows")) {
			clientes.get(posicion).setFollows(Integer.parseInt(valor));
		}else if(campo.equals("nofollows")) {
			clientes.get(posicion).setNofollows(Integer.parseInt(valor));
		}else if(campo.equals("anchor")) {
			clientes.get(posicion).setAnchor(valor);
		}else if(campo.equals("blog")) {
			clientes.get(posicion).setBlog(valor);
		}else if(campo.equals("idioma")) {
			clientes.get(posicion).setIdioma(valor);
		}else if(campo.equals("servicio")) {
			clientes.get(posicion).setServicio(valor);
		}else if(campo.equals("status")) {
			clientes.get(posicion).setStatus(valor);
		}else if(campo.equals("enlaces_de_pago")) {
			clientes.get(posicion).setEnlacesDePago(valor);
		}
		
		System.out.println("guardado, posicion: "+posicion);
		
		

	}
	
	private void guardarNuevoCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();

		String web = request.getParameter("web");
		String nombre = request.getParameter("nombre");
		String servicio = request.getParameter("servicio");
		String follow = request.getParameter("follow");
		String nofollow = request.getParameter("nofollow");
		String anchor = request.getParameter("anchor");			
		String blog = request.getParameter("blog");
		String idioma = request.getParameter("idioma");
		String jsonEmpleados = request.getParameter("jsonEmpleados");
		System.out.println(jsonEmpleados); 
		
		int sumaEnlaces = 0;
		ArrayList<Empleado> empleados = pj.parsearClienteEmpleado(jsonEmpleados);
		for (Empleado e : empleados) {sumaEnlaces += e.getN_follows();}

		boolean enlacesCompletos = false;
		if(servicio.equals("lite")) {if(sumaEnlaces==3) {enlacesCompletos=true;}}
		else if(servicio.equals("pro"))  {if(sumaEnlaces==4) {enlacesCompletos=true;}}
		else if(servicio.equals("premium"))  {if(sumaEnlaces==6) {enlacesCompletos=true;}}
		else if(servicio.equals("medida"))  {if(sumaEnlaces>0) {enlacesCompletos=true;}}
		
		
		obj.put("status", "0");
		if(!web.startsWith("http://") && !web.startsWith("https://")) {
			obj.put("text", "Introuce una web correcta");
		}else if(!web.contains(".")){
			obj.put("text", "Debe contener al menos un .");
		}else if(!servicio.trim().equals("lite") && !servicio.trim().equals("pro") && !servicio.trim().equals("premium") && !servicio.trim().equals("medida")){
			obj.put("text", "Introduce un Servicio");
		}else if(jsonEmpleados.equals("[]")){
			obj.put("text", "Selecciona almenos un usuario");
		}else if(enlacesCompletos == false){
			obj.put("text", "Reparte todos los follows entre los empleados");
		}else {

			//comprobamos que el cliente ya esta insertado en la base de datos
			String dominio = web.replace("http://", "").replace("https://","").replace("www.","");
			if(!dominio.contains("/")) dominio=dominio+"/";
			dominio = dominio.substring(0, dominio.indexOf("/"));
			dominio = dominio.substring(0, dominio.lastIndexOf("."));



			String json = ws.getClienteByPieceDominio(dominio, "getClienteByPieceDominio.php");
			System.out.println(dominio+"   "+json);
			ArrayList<ClienteGson> clientesGson = new Gson().fromJson(json, new TypeToken<List<ClienteGson>>(){}.getType());

			boolean coincidenciaExacta =false;
			String coincidenciaParcial="";
			for (ClienteGson c : clientesGson) {
				if(c.getWeb().equals(web)) {coincidenciaExacta=true;break;
				}else {coincidenciaParcial=c.getWeb();}
			}

			String status="",text="";
			if(clientesGson.size()==0) {//si esto es igual a 0 significa que en la bbdd no hay ningun cliente con este dominio
				status="1";
				
				ArrayList<String> wbList = new ArrayList<>(Arrays.asList(web,nombre,servicio,follow,nofollow,anchor,blog,idioma));
				json = ws.clientes(wbList, "insertNuevoCliente", "clientes.php");
				System.out.println("##: "+json );
				int idCliente = pj.parsearIdCliente(json);
				for (Empleado e : empleados) {
					
					wbList = new ArrayList<>(Arrays.asList(idCliente+"", e.getId()+"",e.getCategoria(), e.getN_follows()+""));
					ws.clientes(wbList, "insertClienteEmpleado", "clientes.php");
					
					wbList = new ArrayList<>(Arrays.asList(idCliente+"", e.getId()+"", e.getN_follows()+"", Data.fecha, 0+""));
					ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
					
				}
				if(idCliente>0) {
					wbList = new ArrayList<>(Arrays.asList(idCliente+"", 0+"", 0+"", Data.fecha, 1+""));
					ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
				}
				
			}else {
				if(coincidenciaExacta) {status="2";text="Este cliente ya existe";}
				else {status="3";text="Coincidencia parcial en el dominio con el cliente:  ";}
			}
			obj.put("status", status);
			obj.put("text", text);
			obj.put("c", coincidenciaParcial);


		}
		out.print(obj);
		
	} 
	
	private void eliminarCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String json = request.getParameter("json");
		System.out.println(json);

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
	
	private void addDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();

		String id_cliente = request.getParameter("id_cliente");
		String url = request.getParameter("url");

		ws.addDestino(id_cliente, url, "addDestino.php");

		String html = "<li class='pdd_h_17 pr inner_pop_up'><span>"+url+"</span><i onclick='deleteDestino(this)' class='material-icons inner_pop_up'> remove </i></li>";
		obj.put("html", html);
		out.print(obj);

		System.out.println(id_cliente +"  "+url);
	}
	
	

	private void cargarListaClientes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role, String campoOrdenar, Empleado empleado) throws IOException, ParseException {

		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_user+"", empleado.getCategoria()));
		String json = ws.clientes(wbList, "getClientes", "clientes.php");
		String[] jsonArray = json.split(";;");
		//System.out.println("0- :"+jsonArray[0]);
		//System.out.println("1- :"+jsonArray[1]);
		empleado = new Gson().fromJson(jsonArray[0].substring(1, jsonArray[0].length()-1), Empleado.class);

		//----------------------------------------------------------------------------------------------------------------------------------------------
		String listaEmpleados="",listaFiltroEmpleado="", listaEnlacesEmpleado="<div onclick='stopPropagation()' class='div_elem_empl'>";
		//obtenemos los empleados que participan en el cliente
		for (int i = 0; i < Data.empleados.size(); i++) {
			Empleado e = Data.empleados.get(i);
			String checked="";
			if(e.getCategoria().equals("free")) { 
				listaEmpleados +=  
				"<li data-id-empleado='"+e.getId()+"' data-tipo-empleado='"+e.getCategoria()+"' class='inner_pop_up' onclick='stopPropagation()'>"+
					"<div class='pretty p-icon p-smooth chkbx_filter cbx_users'><input onclick='updateSpanNames(this)' class='slT' type='checkbox' "+checked+"><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div>"+
					"<span class='f_s_12'>"+e.getName()+"</span>"+ 
				"</li>";
				
				listaFiltroEmpleado +="<div data-filter='empleado' data-valor='"+e.getName().toLowerCase()+"' data-name='"+e.getId()+"' class='txt_opciones_filter'>"+e.getName()+"<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>";
			}
		}
		listaEnlacesEmpleado+="</div>";
		//----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		
		this.clientes.clear();
		
		clientes = pj.parsearClientesMap(jsonArray[1]);
		ob.clientesByDomain(this.clientes);
		
		


		String arrowWeb="", arrowNombre="",arrowServicio="",arrowuser="";
		arrowWeb = "<i class='material-icons arrowOrdenar visible'> arrow_downward </i>";
		arrowNombre = "<i class='material-icons arrowOrdenar'> arrow_downward </i>";
		arrowServicio = "<i class='material-icons arrowOrdenar'> arrow_downward </i>";
		arrowuser = "<i class='material-icons arrowOrdenar' style='float: right; margin-left:0px;'> arrow_downward </i>";
		
		//***************************************************************************************************

		String selectDelete = "<div class='pretty p-icon p-smooth div_select_cliente'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div>";

		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>CLIENTES</div>");//"+categorias.get(posicion).getEnlace()+"
		out.println("	<div class='btnAdd' onclick='openNewCliente(this)'>Nuevo cliente</div>");
		out.println("</div>"); 
		out.println("<div class='ctoolbar'>");
		out.println("	<div "+empleado.getClientesEliminar().get("onClick")+" class='delete_client'><i class='material-icons gris'>delete_outline</i></div>");
		out.println("	<div onclick='openFilter(this)' class='filter_client tdCat'>");
		out.println("		<i class='tdCat material-icons gris'>filter_list</i>");
		out.println("		<div class='div_filtro effect7 pop_up' onclick='stopPropagation()'>");
		out.println("			<div class='title_filter'>Filtros <i class='material-icons i_title_filter'> clear </i></div>");
		
		out.println("				<div class='float_left'>");
		out.println("					<div class='section_filter'>Estado cliente</div>");
		out.println("					<div data-filter='estado' data-valor='new' class='txt_opciones_filter'>Cliente nuevo<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='estado' data-valor='old' class='txt_opciones_filter'>Cliente normal<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='estado' data-valor='our' class='txt_opciones_filter'>Cliente YoSEO<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("				</div>");
		
		out.println("				<div class='float_left'>");
		out.println("					<div class='section_filter'>Servicios</div>");
		out.println("					<div data-filter='servicio' data-valor='lite' class='txt_opciones_filter'>SEO Lite<div class='pretty p-icon p-smooth chkbx_filter '><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='pro' class='txt_opciones_filter'>SEO Pro<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='premium' class='txt_opciones_filter'>SEO Premium<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='medida' class='txt_opciones_filter'>SEO a medida<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("				</div>");

		out.println("				<div class='float_left'>");
		out.println("					<div class='section_filter'>Empleados</div>");
		out.println(					listaFiltroEmpleado);
		out.println("				</div>");
		
		
		out.println("			<div class='btn_filter' onclick='aplicarFiltro(this)'>Aplicar</div>");
		out.println("		</div>");
		out.println("	</div>");
		out.println("	<div class='div_group_filter'></div>");
		out.println("	<div id='websGuardar' class='zoom'>guardar</div></div>"); 
		out.println("</div>");
		out.println("<div class='keywordsClient listaClientes'>");
		out.println("	<div id='results_Clients' class='contentTable'>");
		out.println("		<table id='tClients' class='table'>");
		out.println("			<thead class='head_fixed'>");
		out.println("				<tr>");
		out.println("					<th class='cabeceraTable select_client '>"+selectDelete+"</th>");
		out.println("					<th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></div></th>");
		out.println("					<th class='cabeceraTable cCWebCliente cursor_pointer' onclick='aplicarFiltro(this)' data-tipo='web'><span class='cabeceraTable spanCabecera' >Web</span>"+arrowWeb+"</th>");
		out.println("					<th class='cabeceraTable cCNombre cursor_pointer' onclick='aplicarFiltro(this)' data-tipo='nombre'><span class='cabeceraTable spanCabecera' >Nombre</span>"+arrowNombre+"</th>");
		out.println("					<th class='cabeceraTable cCTipo cursor_pointer'  onclick='aplicarFiltro(this)' data-tipo='servicio'><span class='cabeceraTable spanCabecera' >Servicio</span>"+arrowServicio+"</th>");
		out.println("					<th class='cabeceraTable cFollow'><i class='material-icons lf'>link</i></th>");
		out.println("					<th class='cabeceraTable cNoFollow'><i class='material-icons lnf'>link</i></th>");
		out.println("					<th class='cabeceraTable anchorC'>Anchor</th>");
		out.println("					<th class='cabeceraTable cCBlog txt_center_mg_0'>Blog</th>");
		out.println("					<th class='cabeceraTable cCPaid txt_center_mg_0'><i class='material-icons'> monetization_on </i></th>");
		out.println("					<th class='cabeceraTable cCIdioma'><i class='material-icons'> g_translate </i></th>");
		out.println("					<th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer' onclick='aplicarFiltro(this)' data-tipo='user'><i class='material-icons f_size26'>account_circle</i>"+arrowuser+"</th>");
		out.println("					<th class='cabeceraTable cell_destino txt_center_mg_0'>Destinos</th>");
		out.println("				</tr>");
		
		out.println("			</thead>");
		out.println("			<tbody>");
		for (int c = 0; c < clientes.size(); c++) {
			Cliente cliente = clientes.get(c);
			setContenidoTabla(empleado, c, cliente, out);
		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");
		out.println("</div>");
		//nuevo cliente****************************************************************************************************************************************************
		
		out.println("<div class='newSomething effect7'>");
		out.println("	<div class='cancelNew' onclick='cancelNewCliente(this)' ><i class='material-icons i_cancel'> clear </i></div>");
		out.println("	<table id='tNuevoCliente' class='table'>");
		out.println("			<thead><tr><th class='cabeceraTable cCWebCliente'>Web</th><th class='cabeceraTable cCNombre'>Nombre</th><th class='cabeceraTable tipoNew'>Servicio</th><th class='cabeceraTable cFollow wdth_45'><i class='material-icons lf'>link</i></th><th class='cabeceraTable cNoFollow wdth_45'><i class='material-icons lnf'>link</i></th><th class='cabeceraTable anchorC'>Anchor</th><th class='cabeceraTable cCBlog txt_center_mg_0'>Blog</th><th class='cabeceraTable cCIdioma'>Idioma</th>"
				
				+ "<th class='cabeceraTable cCUser txt_center_mg_0 wdth_110' data-tipo='user'><i class='material-icons f_size26'>account_circle</i></th></tr></thead>");

		out.println("			<tbody>");
		out.println("				<td class='cCWebCliente'><input class='inLink'  placeholder='Introduce una web' type='text' value=''></td>");
		out.println("				<td class='cCNombre'><input class='inLink' placeholder='Introduce un nombre' type='text' value=''></td>");
		out.println("				<td class='tipoNew pr'  onclick='openServicio(this)'>");
		out.println("					<div class='tdCat tdWeb'><span id='-' class='tdCat'>-</span><i class='material-icons arrow'>arrow_drop_down</i></div>");
		out.println("					<ul class='slCt effect7 nuevaWeb margin_top_6'>");
		out.println("						<li id='lite' data-follows='3' data-nofollows='5' "+empleado.getClientesServicio().get("onClick")+">SEO Lite</li>");
		out.println("						<li id='pro' data-follows='4' data-nofollows='10' "+empleado.getClientesServicio().get("onClick")+">SEO Pro</li>");
		out.println("						<li id='premium' data-follows='6' data-nofollows='15' "+empleado.getClientesServicio().get("onClick")+">SEO Premium</li>");
		out.println("						<li id='medida' data-follows='0' data-nofollows='0' "+empleado.getClientesServicio().get("onClick")+"'>SEO a medida</li>");
		out.println("					</ul>");
		out.println("			   	</td>");
		out.println("			   	<td class='cFollow'><input class='inLink' type='text' onchange='guardarFollows(this)' value='0'></td>");			
		out.println("   		   	<td class='cNoFollow'><input class='inLink' type='text' onchange='guardarNoFollows(this)' value='0'></td>");
		out.println("				<td class='anchorC'><input type='text' class='inLink' placeholder='Introduce un anchor' onchange='guardarAnchorCliente(this)' value=''></td>");
		out.println("				<td class='tdCat cCBlog txt_center_mg_0 pr'>");
		out.println("					<div class='tdCat tdWeb'><label  class='switch label_switch'><input class='slT' type='checkbox' onchange='guardarBlog(this)'><span class='slider round'></span></label></div>");
		out.println("				</td>");
		out.println("				<td class='tdCat cCIdioma'><input class='inLink' type='text' onchange='guardarIdioma(this)' value='ESP'></td>");
		
		//Columna USER
		out.println("				<td class='tdCat cCUser pr txt_center_mg_0' onclick='opentUser(this)'>");
		out.println("					<div class='tdCat tdWeb txt_center_mg_0'><span data-id-empleado='0' data-tipo-empleado='0' class='tdCat' data-list-user='' type='text'>-</span></div>");
		out.println("					<ul class='slCt effect7 nuevaWeb txt_algn_left margin_top_6'>"+listaEmpleados+"<div class='algn_center'><i class='material-icons i_more noselect' onclick='openEmpleadoEnlaces(this)'>more_horiz</i></div>"+listaEnlacesEmpleado+"</ul>");
		out.println("				</td>");

		out.println("			</tbody>");
		out.println("	</table>");
		out.println("	<div class='infoNew'></div>"); 
		out.println("	<div class='guardarNew' onclick='guardarNewCliente(this)'>guardar</div>");
		out.println("</div>");
		//********************************************************************************************************************************************************************
		out.println("<div class='divBlockClientes'></div>");
		out.println("<div class='resize_head_table_clientes'><script> resize_head_table_clientes() </script></div>");
		
		reseatValores(request, response, out, id_user, user_role, campoOrdenar); 
		
		System.out.println("pagina cargada");
		 
	}

	private void setContenidoTabla (Empleado empleado, int c,Cliente cliente, PrintWriter out) {
		String listaEmpleados="", listaEnlacesEmpleado="<div onclick='stopPropagation()' class='div_elem_empl effect7_left'>";
		//obtenemos los empleados que participan en el cliente
		for (int i = 0; i < Data.empleados.size(); i++) {
			Empleado e = Data.empleados.get(i);
			String checked="";
			

			if(cliente.getEmpleados().get(e.getId()+"") != null) {
				checked="checked";//cliente.getEmpleados().get(e.getId()).getN_follows()
				listaEnlacesEmpleado += "<div data-id-empleado='"+e.getId()+"' data-tipo-empleado='"+e.getCategoria()+"' class='pr mg_top_10'><span>"+e.getName()+"</span>"+"<div class='div_n_follows'><div class='n_follows_remove' onclick='modifyEnlacesEmpleado(this)' data-action='decrease'><i class='material-icons f_size_17 noselect'> remove </i></div>"
						+ "<input class='input_n_follows inLink noselect' type='number' data-id-empleado='"+e.getId()+"' data-tipo-empleado='"+e.getCategoria()+"' value='"+cliente.getEmpleados().get(e.getId()+"").getN_follows()+"' readonly >"
				+ "<div class='n_follows_add' onclick='modifyEnlacesEmpleado(this)' data-action='increase'><i class='material-icons f_size_17 noselect'> add </i></div></div>"+"</div>"; 
			}
			if(e.getCategoria().equals("free")) { 
				listaEmpleados += 
				"<li data-id-empleado='"+e.getId()+"' data-tipo-empleado='"+e.getCategoria()+"' class='inner_pop_up' onclick='stopPropagation()'>"+
					"<div class='pretty p-icon p-smooth chkbx_filter cbx_users'><input "+empleado.getClientesEmpleado().get("onClick")+" class='slT' type='checkbox' "+checked+"><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div>"+
					"<span class='f_s_12'>"+e.getName()+"</span>"+ 
				"</li>";
			}
		}
		listaEnlacesEmpleado+="<div class='btn_guardar_n_follows' onclick='saveEnlacesEmpleado(this)'>guardar</div></div>";
		
		String htmlServicio;
		ArrayList<String> destinos = new ArrayList<>(Arrays.asList((cliente.getUrls_a_atacar()+",").split(",")));
		String status = "sPendiente";
		if(cliente.getStatus().equals("new"))status = "sOK";
		else if(cliente.getStatus().equals("old"))status = "sPendiente";
		if(cliente.getStatus().equals("our"))status = "sYS";

		String listaDestinos="";
		for (String destino : destinos) {
			if(destino.equals(cliente.getWeb())) listaDestinos +="<li class='pdd_h_17 pr inner_pop_up' ><span>"+destino+"</span></li>";
			else listaDestinos +="<li class='pdd_h_17 pr inner_pop_up' ><span>"+destino+"</span><i "+empleado.getClientesDestinos().get("onClickRemove")+" class='material-icons inner_pop_up'> remove </i></li>";
		}

		int id_cliente = cliente.getIdCliente();//este es el ID REAL DE LA BBDD
		//------------DESPLEGABLE COLUMNA SERVICIO-----------------
		String claseLite="",clasePro="",clasePremium="", claseMedida="", readonly="";
		String opServicio = cliente.getServicio();
		if(cliente.getServicio().equals("lite"))         { opServicio = "SEO Lite"; 	  claseLite="class='liActive'";	  readonly="readonly='true'";}
		else if(cliente.getServicio().equals("pro"))    { opServicio = "SEO Pro";       clasePro="class='liActive'";      readonly="readonly='true'";}
		else if(cliente.getServicio().equals("premium")){ opServicio = "SEO Premium";   clasePremium="class='liActive'";  readonly="readonly='true'";}
		else if(cliente.getServicio().equals("medida")) { opServicio = "SEO a medida";  claseMedida="class='liActive'"; }
		htmlServicio = "			<li id='lite' "+claseLite+" data-follows='3' data-nofollows='5' "+empleado.getClientesServicio().get("onClick")+">SEO Lite</li>";
		htmlServicio += "			<li id='pro' "+clasePro+" data-follows='4' data-nofollows='10' "+empleado.getClientesServicio().get("onClick")+">SEO Pro</li>";
		htmlServicio += "			<li id='premium' "+clasePremium+" data-follows='6' data-nofollows='15' "+empleado.getClientesServicio().get("onClick")+">SEO Premium</li>";
		htmlServicio += "			<li id='medida' "+claseMedida+" data-follows='0' data-nofollows='0' "+empleado.getClientesServicio().get("onClick")+"'>SEO a medida</li>";
		String blogChecked ="checked"; if(Integer.parseInt(cliente.getBlog())==0) blogChecked="";
		String paidChecked ="checked"; if(Integer.parseInt(cliente.getEnlacesDePago())==0) paidChecked="";

		out.println("<tr id='"+id_cliente+"' posicion='"+c+"' class='pr'>");
		out.println("	<td class='select_client'>");
		out.println("		<div class='pretty p-icon p-smooth div_select_cliente'>");
		out.println("			<input class='slT' type='checkbox'/>"); 
		out.println("			<div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div>");
		out.println("		</div>");
		out.println("	</td>");
		//columna Estado del cliente
		out.println("<td class='cStatus pr tdCat' onclick='openEstadoCliente(this)'>");
		out.println("	<div class='divStatus "+status+" tdCat'></div>");
		out.println("	<div class='div_Estado_Cliente pop_up effect7 text_left' >");
		out.println("		<ul class=' pdd_v_12 '>");
		out.println("			<li data-status-cliente='new' "+empleado.getClientesEstado().get("onClick")+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente nuevo</span><div data-class='sOK' class='divStatus sOK st_div_cli'></div></li>");
		out.println("			<li data-status-cliente='old' "+empleado.getClientesEstado().get("onClick")+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente normal</span><div data-class='sPendiente' class='divStatus sPendiente st_div_cli'></div></li>");
		out.println("			<li data-status-cliente='our' "+empleado.getClientesEstado().get("onClick")+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente YoSeo</span><div data-class='sYS' class='divStatus sYS st_div_cli'></div></li>");
		out.println("		</ul>");
		out.println("	</div>");
		out.println("</td>");
		//Columna WEB
		out.println("	<td class='cCWebCliente'><input class='inLink' "+empleado.getClientesWeb().get("onInput")+" type='text' "+empleado.getClientesWeb().get("onChange")+" value='"+cliente.getWeb()+"' "+empleado.getClientesWeb().get("estado")+"></td>");
		//Columna NOMBRE
		out.println("	<td class='cCNombre'><input class='inLink' "+empleado.getClientesNombre().get("onInput")+" type='text' "+empleado.getClientesNombre().get("onChange")+" value='"+cliente.getNombre()+"' "+empleado.getClientesNombre().get("estado")+"></td>");
		//Columna SERVICIO
		out.println("	<td class='cCTipo pr' onclick='openServicio(this)'>");
		out.println("		<div class='tdCat tdWeb'><span id='"+cliente.getServicio()+"' class='tdCat'>"+opServicio+"</span><i class='material-icons arrow'>arrow_drop_down</i></div>");		
		out.println("		<ul class='slCt effect7 pop_up'>"+htmlServicio+"</ul>");
		out.println("   </td>");
		//Columna FOLLOW
		out.println("	<td class='cFollow'><input class='inLink' "+empleado.getClientesFolows().get("onInput")+" type='text' "+empleado.getClientesFolows().get("onChange")+" value='"+cliente.getFollows()+"' "+readonly+"></td>");			
		//Columna NOFOLLOW
		out.println("   <td class='cNoFollow'><input class='inLink' "+empleado.getClientesNofollows().get("onInput")+" type='text' "+empleado.getClientesNofollows().get("onChange")+" value='"+cliente.getNofollows()+"' "+readonly+"></td>");	
		//Columna ANCHOR
		out.println("	<td class='anchorC'><input type='text' "+empleado.getClientesAnchor().get("onInput")+" class='inLink' "+empleado.getClientesAnchor().get("onChange")+" value='"+cliente.getAnchor()+"' "+empleado.getClientesAnchor().get("estado")+"></td>");
		//Columna BLOG
		out.println("	<td class='tdCat cCBlog txt_center_mg_0 pr'>");
		out.println("		<div class='tdCat tdWeb'>");
		out.println("			<label  class='switch label_switch'><input class='slT' type='checkbox' "+empleado.getClientesBlog().get("onChange")+" "+blogChecked+" "+empleado.getClientesBlog().get("estado")+"><span class='slider round'></span></label>");
		out.println("		</div>");
		out.println("	</td>");
		//ENLACE DE PAGO
		out.println("	<td class='tdCat cCBlog txt_center_mg_0 pr'>");
		out.println("		<div class='tdCat tdWeb'>");
		out.println("			<label  class='switch label_switch'><input class='slT' type='checkbox' "+empleado.getClientesPaid().get("onChange")+" "+paidChecked+" "+empleado.getClientesPaid().get("estado")+"><span class='slider round'></span></label>");
		out.println("		</div>");
		out.println("	</td>");
		//Columna IDIOMA
		out.println("	<td class='tdCat cCIdioma'><input "+empleado.getClientesIdioma().get("onInput")+" class='inLink' type='text' "+empleado.getClientesIdioma().get("onChange")+" value='"+cliente.getIdioma()+"' "+empleado.getClientesIdioma().get("estado")+"></td>");
		//Columna USER
		out.println("	<td class='tdCat cCUser pr txt_center_mg_0' onclick='opentUser(this)'>");
		out.println("		<div class='tdCat tdWeb txt_center_mg_0'>");
		out.println("			<span data-id-empleado='"+cliente.getId_empleado()+"' data-tipo-empleado='"+cliente.getTipoEmpleado()+"' class='tdCat' type='text'>"+cliente.getName_empleado()+"</span>");
		//out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
		out.println("		</div>"); 
		out.println("		<ul class='slCt effect7 pop_up txt_algn_left inner_pop_up'>"+listaEmpleados+"<div class='algn_center'><i class='material-icons i_more noselect' onclick='openEmpleadoEnlaces(this)'>more_horiz</i></div>"+listaEnlacesEmpleado+"</ul>");
		out.println("	</td>");
		//Destinos
		out.println("	<td class='tdCat cell_destino pr text_center' onclick='openDestinos(this)'>");
		out.println("			<i class='material-icons inner_pop_up'> directions </i>");
		out.println("			<div data-id='lista_destinos' class='div_destinos pop_up effect7 inner_pop_up pop_up_move2left  text_left' >");
		out.println("				<div class='nuevo_destino inner_pop_up' onclick='stopPropagation()'>");
		out.println("					<span class='inner_pop_up'>Destino: </span><input type='text' class='inLink inner_pop_up' value='' placeholder='Introduce una nueva url a atacar'><i "+empleado.getClientesDestinos().get("onClickAdd")+" class='material-icons inner_pop_up'>add</i>");
		out.println("				</div>");
		out.println("				<ul class='scroll_115 pdd_v_12 inner_pop_up'>"+listaDestinos+"</ul>");
		out.println("			</div>");
		out.println("		</td>");
		out.println("</tr>");
		
		
	}

	private void reseatValores(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role, String campoOrdenar) {
		/*System.out.println("Fecha: "+Data.fecha);
		
		for (int i = 0; i < clientes.size(); i++) {
			Cliente c = clientes.get(i);
			System.out.println(c.getWeb());
			
			for (Map.Entry<String, Empleado> empleado : c.getEmpleados().entrySet()) {
				int enlaces = empleado.getValue().getN_follows();
				int idEmpleado = empleado.getValue().getId();
				ArrayList<String> wbList = new ArrayList<>(Arrays.asList(c.getIdCliente()+"", idEmpleado+"", enlaces+"", Data.fecha, 0+""));
				String json = ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
			    System.out.println("\t"+idEmpleado+"  "+empleado.getValue().getName()+"  "+enlaces);
			}
			
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(c.getIdCliente()+"", 0+"", 0+"", Data.fecha, 1+""));
			String json = ws.clientes(wbList, "resetearValoresMensuales", "clientes.php");
			
		}
		
		*/
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
