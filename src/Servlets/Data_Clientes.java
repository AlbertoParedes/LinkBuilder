package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
	ArrayList<Cliente> clientes = new ArrayList<Cliente>();

	public Data_Clientes() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int id_user = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();
		String metodo = request.getParameter("metodo");
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		if(metodo.equals("cargarListaClientes")) {
			try {cargarListaClientes(request, response, out, id_user,role, false, "web");} catch (ParseException e) {e.printStackTrace();}
		}else if(metodo.equals("guardarValoresCliente")) {
			guardarValoresCliente(request, response, out);
		}else if(metodo.equals("guardarNuevoCliente")) {
			guardarNuevoCliente(request, response);
		}else if(metodo.equals("eliminarCliente")) {
			eliminarCliente(request, response, out);
		}else if(metodo.equals("addDestino")) {
			addDestino(request, response, out);
		}else if(metodo.equals("guardarValoresCliente")) {
			guardarValoresCliente(request, response, out);
		}else if(metodo.equals("guardarEmpleado")) {
			guardarEmpleado(request, response, out); 
		}else if(metodo.equals("uploadFactura")) {
			subirNuevaFactura(request, response, id_user, out);
		}else if(metodo.equals("removeDestino")) {
			removeDestino(request, response, out);
		}else if((metodo.equals("ordenarListaClientes"))) {
			try {ordenarListaClientes(request, response, out, id_user,role, true);} catch (ParseException e) {e.printStackTrace();}
		}

	}

	private void ordenarListaClientes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role, boolean ordenar) throws IOException, ParseException {

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
		
		cargarListaClientes(request, response, out, id_user, user_role, ordenar, campo);
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
		String id_empleado_anterior = request.getParameter("id_empleado_anterior");
		String id_empleado_seleccionado = request.getParameter("id_empleado_seleccionado");
		String tipo_empleado_seleccionado = request.getParameter("tipo_empleado_seleccionado");


		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente,id_empleado_seleccionado, id_empleado_anterior, tipo_empleado_seleccionado));
		String json = ws.clientes(wbList, "insertClienteEmpleado", "clientes.php");
		System.out.println("->"+"   "+json);

	}

	private void guardarValoresCliente(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String valor = request.getParameter("valor");
		String campo = request.getParameter("campo");
		String id_cliente = request.getParameter("id_cliente");
		System.out.println(valor+"  "+campo+"   "+id_cliente);
		String json = ws.updateCliente(id_cliente, campo, valor, "updateCliente.php");

		if(campo.equals("linkbuilder")) {
			String empleadoAnterior = request.getParameter("empleado_anterior");

			ArrayList<String> wbList=new ArrayList<String>();wbList.add(id_cliente);wbList.add(valor);wbList.add(empleadoAnterior);
			json = ws.clientes(wbList, "insertClienteEmpleado", "clientes.php");
			System.out.println("->"+empleadoAnterior+"   "+json);
		}



		System.out.println("guardado");




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
		int user = Integer.parseInt(request.getParameter("user"));
		String user_tipo = request.getParameter("user_tipo");
		System.out.println(user_tipo+"+++");

		obj.put("status", "0");
		if(!web.startsWith("http://") && !web.startsWith("http://")) {
			obj.put("text", "Introuce una web correcta");
		}else if(!web.contains(".")){
			obj.put("text", "Debe contener al menos un .");
		}else if(!servicio.trim().equals("lite") && !servicio.trim().equals("pro") && !servicio.trim().equals("premium") && !servicio.trim().equals("medida")){
			obj.put("text", "Introduce un Servicio");
		}else if(user<1){
			obj.put("text", "Selecciona un usuario");
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

				ArrayList<String> wbList = new ArrayList<>(Arrays.asList(web,nombre,servicio,follow,nofollow,anchor,blog,idioma,user+"",user_tipo));
				json = ws.clientes(wbList, "insertNuevoCliente", "clientes.php");
				//ws.nuevoCliente(web, nombre, servicio, follow, nofollow, anchor, blog, idioma, user, "insertNuevoCliente.php");
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


	private void cargarListaClientes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, String user_role, boolean ordenar, String campoOrdenar) throws IOException, ParseException {
		String listaEmpleados = "",onClickEstado="", onClickguardarEstado="",onInputWebCliente="",onChangeWebCliente="",onInputNombreCliente="",onChangeNombreCliente="",onClickServicio="", onClickGuardarServicio="",onChangeGuardarFollows="",onChangeGuardarNoFollows="",onChangeGuardarAnchor="",onChangeGuardarBlog="",onChangeGuardarIdioma="",onClickOpenEmpleados="",onClickOpenDestinos = "", onClickAddDestino="",onClickDeleteDestino="",onInputGuardar=""; 
		for (Empleado e : Data.empleados) {
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
				onClickEstado = "onclick='openEstadoCliente(this)'";
				onClickguardarEstado="onclick='guardarEstadoCliente(this)'";

			}else if(user_role.equals("free_admin")) {
				if(e.getCategoria().equals("free")) {
					listaEmpleados += "<li data-id-empleado='"+e.getId()+"' data-tipo-empleado='"+e.getCategoria()+"' onclick='guardarEmpleado(this)'>"+e.getName()+"</li>";

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
					onClickEstado = "onclick='openEstadoCliente(this)'";
					onClickguardarEstado="onclick='guardarEstadoCliente(this)'";
				}

			}else if(user_role.equals("free_user")) {
				if(e.getCategoria().equals("free")) {
					listaEmpleados += "<li>"+e.getName()+"</li>";
					onClickOpenDestinos = "onclick='openDestinos(this)'";
					onClickEstado = "onclick='openEstadoCliente(this)'";
					onClickguardarEstado="onclick='guardarEstadoCliente(this)'";

				}
			}else if(user_role.equals("paid_user")) {
				if(e.getCategoria().equals("paid")) {
					listaEmpleados += "<li>"+e.getName()+"</li>";

				}
			}
		}
		if(ordenar==false) {
			//clientes por usuario///////////********************************************************************
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_user+"",user_role));
			String json = ws.clientes(wbList, "getClientesEmpleado", "clientes.php");
			//ArrayList<Cliente> clientes = new Gson().fromJson(json, new TypeToken<List<Cliente>>(){}.getType());
			ArrayList<Cliente> clientes = pj.parsearClientes(json);
			this.clientes.clear();
			this.clientes = clientes;
			ob.clientesByDomain(this.clientes);

		}

		String arrowWeb="", arrowNombre="",arrowServicio="",arrowuser="";
		if(campoOrdenar.equals("web")) {
			arrowWeb = "<i class='material-icons arrowOrdenar'> arrow_downward </i>";
		}else if(campoOrdenar.equals("nombre")) {
			arrowNombre = "<i class='material-icons arrowOrdenar'> arrow_downward </i>";
		}else if(campoOrdenar.equals("servicio")) {
			arrowServicio = "<i class='material-icons arrowOrdenar'> arrow_downward </i>";
		}else if(campoOrdenar.equals("user")) {
			arrowuser = "<i class='material-icons arrowOrdenar' style='float: right; margin-left:0px;'> arrow_downward </i>";
		}
		//***************************************************************************************************

		String selectDelete = "<div class='pretty p-icon p-smooth div_select_cliente'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div>";
		//COMIENZA LA TABLA CLIENTES
		out.println("<div class='infoClient'>");
		out.println("	<div class='nameClient'>CLIENTES</div>");//"+categorias.get(posicion).getEnlace()+"
		out.println("	<div class='btnAdd' onclick='openNewCliente(this)'>Nuevo cliente</div>");
		out.println("</div>");
		out.println("<div class='ctoolbar'>"
				+ "<div onclick='deleteClient(this)' class='delete_client'>"
				+ "		<i class='material-icons gris'>delete_outline</i>"
				+ "</div>"
				+ "<div onclick='openFilter(this)' class='filter_client'>"
				+ "		<i class='material-icons gris'>filter_list</i>"
				+"		<div></div>"
				+ "</div>"
				
				+ "<div id='websGuardar' class='zoom'>guardar</div></div>");
		out.println("<div class='keywordsClient listaClientes'>");
		out.println("	<div id='results_Client' class='contentTable'>");
		out.println("		<table id='tClients' class='table'>");
		out.println("			<thead><tr>"
				+ "					<th class='cabeceraTable select_client '>"+selectDelete+"</th>"
				+ "					<th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></div></th>"
				+ "					<th class='cabeceraTable cCWebCliente cursor_pointer' onclick='clientes_ordenarByWeb(this)'><span class='cabeceraTable spanCabecera' >Web</span>"+arrowWeb+"</th>"
				+ "					<th class='cabeceraTable cCNombre cursor_pointer' onclick='clientes_ordenarByName(this)' ><span class='cabeceraTable spanCabecera' >Nombre</span>"+arrowNombre+"</th>"
				+ "					<th class='cabeceraTable cCTipo cursor_pointer'  onclick='clientes_ordenarByServicio(this)'><span class='cabeceraTable spanCabecera' >Servicio</span>"+arrowServicio+"</th>"
				+ "					<th class='cabeceraTable cFollow'><i class='material-icons lf'>link</i></th>"
				+ "					<th class='cabeceraTable cNoFollow'><i class='material-icons lnf'>link</i></th>"
				+ "					<th class='cabeceraTable anchorC'>Anchor</th>"
				+ "					<th class='cabeceraTable cCBlog'>Blog</th>"
				+ "					<th class='cabeceraTable cCIdioma'><i class='material-icons'> g_translate </i></th>"
				+ "					<th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer' onclick='clientes_ordenarByUser(this)'><i class='material-icons f_size26'>account_circle</i>"+arrowuser+"</th>"
				+ "					<th class='cabeceraTable cell_destino'>Destinos</th>"
				+ "				</tr></thead>");
		out.println("			<tbody>");
		String htmlServicio="",htmlUserFinal="";

		System.out.println(clientes.size());
		System.out.println("# "+clientes.toString());
		for (int c = 0; c < clientes.size(); c++) {
			Cliente cliente = clientes.get(c);

			ArrayList<String> destinos = new ArrayList<>(Arrays.asList((cliente.getUrls_a_atacar()+",").split(",")));;
			String status = "sPendiente";
			if(cliente.getStatus().equals("new"))status = "sOK";
			else if(cliente.getStatus().equals("old"))status = "sPendiente";
			if(cliente.getStatus().equals("our"))status = "sYS";


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

			String blogChecked ="checked"; if(Integer.parseInt(cliente.getBlog())==0) blogChecked="";

			out.println("<tr id='"+id_cliente+"' posicion='"+c+"' class='pr'>");
			out.println("	<td class='select_client'>");
			out.println("		<div class='pretty p-icon p-smooth div_select_cliente'>");
			out.println("			<input class='slT' type='checkbox'/>");
			out.println("			<div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div>");
			out.println("		</div>");
			out.println("	</td>");
			//columna Estado del cliente

			out.println("<td class='cStatus pr tdCat' "+onClickEstado+">");
			out.println("	<div class='divStatus "+status+" tdCat'></div>");
			out.println("	<div class='div_Estado_Cliente pop_up effect7 text_left' >");
			out.println("		<ul class=' pdd_v_12 '>");
			out.println("			<li data-status-cliente='new' "+onClickguardarEstado+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente nuevo</span><div data-class='sOK' class='divStatus sOK st_div_cli'></div></li>");
			out.println("			<li data-status-cliente='old' "+onClickguardarEstado+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente normal</span><div data-class='sPendiente' class='divStatus sPendiente st_div_cli'></div></li>");
			out.println("			<li data-status-cliente='our' "+onClickguardarEstado+" class='pdd_h_10 pr' ><span class='mg_left_30'>Cliente YoSeo</span><div data-class='sYS' class='divStatus sYS st_div_cli'></div></li>");
			out.println("		");
			out.println("	</div>");
			out.println("</td>");
			//Columna WEB
			out.println("	<td class='cCWebCliente'><input class='inLink' "+onInputWebCliente+" type='text' "+onChangeWebCliente+" value='"+cliente.getWeb()+"'></td>");
			//Columna NOMBRE
			out.println("	<td class='cCNombre'><input class='inLink' "+onInputNombreCliente+" type='text' "+onChangeNombreCliente+" value='"+cliente.getNombre()+"'></td>");
			//Columna SERVICIO
			out.println("	<td class='cCTipo pr' "+onClickServicio+">");
			out.println("		<div class='tdCat tdWeb'><span id='"+cliente.getServicio()+"' class='tdCat'>"+opServicio+"</span><i class='material-icons arrow'>arrow_drop_down</i></div>");		
			out.println("		<ul class='slCt effect7 pop_up'>"+htmlServicio+"</ul>");
			out.println("   </td>");
			//Columna FOLLOW
			out.println("	<td class='cFollow'><input class='inLink' "+onInputGuardar+" type='text' "+onChangeGuardarFollows+" value='"+cliente.getFollows()+"' "+readonly+"></td>");			
			//Columna NOFOLLOW
			out.println("   <td class='cNoFollow'><input class='inLink' "+onInputGuardar+" type='text' "+onChangeGuardarNoFollows+" value='"+cliente.getNofollows()+"' "+readonly+"></td>");	
			//Columna ANCHOR
			out.println("	<td class='anchorC'><input type='text' "+onInputGuardar+" class='inLink' "+onChangeGuardarAnchor+" value='"+cliente.getAnchor()+"'></td>");
			//Columna BLOG
			out.println("	<td class='tdCat cCBlog pr'>");
			out.println("		<div class='tdCat tdWeb ckBlog'>");
			out.println("			<label  class='switch'>");
			out.println("				<input class='slT' type='checkbox' "+onChangeGuardarBlog+" "+blogChecked+">");
			out.println("				<span class='slider round'></span>");
			out.println("			</label>");
			out.println("		</div>");
			out.println("	</td>");
			//Columna IDIOMA
			out.println("	<td class='tdCat cCIdioma'>");
			out.println("			<input "+onInputGuardar+" class='inLink' type='text' "+onChangeGuardarIdioma+" value='"+cliente.getIdioma()+"'>");				
			out.println("	</td>");
			//Columna USER
			out.println("	<td class='tdCat cCUser pr txt_center_mg_0' "+onClickOpenEmpleados+">");
			out.println("		<div class='tdCat tdWeb txt_center_mg_0'>");
			out.println("			<span data-id-empleado='"+cliente.getId_empleado()+"' data-tipo-empleado='"+cliente.getTipoEmpleado()+"' class='tdCat' type='text'>"+cliente.getName_empleado()+"</span>");
			//out.println("			<i class='material-icons arrow'>arrow_drop_down</i>	");
			out.println("		</div>"); 
			out.println("		<ul class='slCt effect7 pop_up'>"+listaEmpleados+"</ul>");
			out.println("	</td>");
			//Destinos
			out.println("	<td class='tdCat cell_destino pr text_center' "+onClickOpenDestinos+">");
			out.println("			<i class='material-icons inner_pop_up'> directions </i>");
			out.println("			<div data-id='lista_destinos' class='div_destinos pop_up effect7 inner_pop_up pop_up_move2left  text_left' >");
			out.println("				<div class='nuevo_destino inner_pop_up' onclick='stopPropagation()'>");
			out.println("					<span class='inner_pop_up'>Destino: </span><input type='text' class='inLink inner_pop_up' value='' placeholder='Introduce una nueva url a atacar'><i "+onClickAddDestino+" class='material-icons inner_pop_up'>add</i>");
			out.println("				</div>");
			out.println("				<ul class='scroll_115 pdd_v_12 inner_pop_up'>");
			for (String destino : destinos) {
				if(destino.equals(cliente.getWeb())) 
					out.println("			<li class='pdd_h_17 pr inner_pop_up' ><span>"+destino+"</span></li>");
				else out.println("			<li class='pdd_h_17 pr inner_pop_up' ><span>"+destino+"</span><i "+onClickDeleteDestino+" class='material-icons inner_pop_up'> remove </i></li>");


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
		out.println("				<td class='cCWebCliente'><input class='inLink'  placeholder='Introduce una web' type='text' value=''></td>");
		out.println("				<td class='cCNombre'><input class='inLink' placeholder='Introduce un nombre' type='text' value=''></td>");
		out.println("				<td class='tipoNew pr'  onclick='openServicio(this)'>");
		out.println("					<div class='tdCat tdWeb'>");
		out.println("						<span id='-' class='tdCat'>-</span>");
		out.println("						<i class='material-icons arrow'>arrow_drop_down</i>	");
		out.println("					</div>");
		out.println("					<ul class='slCt effect7 nuevaWeb margin_top_6'>");
		out.println("						<li id='lite' data-follows='3' data-nofollows='5' "+onClickGuardarServicio+">SEO Lite</li>");
		out.println("						<li id='pro' data-follows='4' data-nofollows='10' "+onClickGuardarServicio+">SEO Pro</li>");
		out.println("						<li id='premium' data-follows='6' data-nofollows='15' "+onClickGuardarServicio+">SEO Premium</li>");
		out.println("						<li id='medida' data-follows='0' data-nofollows='0' "+onClickGuardarServicio+"'>SEO a medida</li>");
		out.println("					</ul>");
		out.println("			   	</td>");
		out.println("			   	<td class='cFollow'><input class='inLink' type='text' onchange='guardarFollows(this)' value='0'></td>");			
		out.println("   		   	<td class='cNoFollow'><input class='inLink' type='text' onchange='guardarNoFollows(this)' value='0'></td>");
		out.println("				<td class='anchorC'><input type='text' class='inLink' placeholder='Introduce un anchor' onchange='guardarAnchorCliente(this)' value=''></td>");
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
		out.println("						<span data-id-empleado='0' data-tipo-empleado='0' class='tdCat' data-list-user='' type='text'>-</span>");
		//out.println("						<i class='material-icons arrow'>arrow_drop_down</i>	");
		out.println("					</div>");
		out.println("					<ul class='slCt effect7 nuevaWeb margin_top_6'>"+listaEmpleados+"</ul>");
		out.println("				</td>");
		out.println("			</tbody>");
		out.println(	"</table>");
		out.println(	"<div class='infoNew'></div>"); 
		out.println(	"<div class='guardarNew' onclick='guardarNewCliente(this)'>guardar</div>");
		out.println("</div>");
		out.println("<div class='divBlockClientes'></div>");

	}

}