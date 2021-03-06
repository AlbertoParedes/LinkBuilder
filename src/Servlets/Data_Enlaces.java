package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String fecha;
	//private Empleado empleado = new Empleado();


	private ArrayList<Enlace> enlaces =  new ArrayList<Enlace>();
	private ArrayList<Foro> forosDisponibles = new ArrayList<Foro>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();



	public Data_Enlaces() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Do get ENLACES");
		
	// obtenemos la fecha sin el dia del hoy-------------------------------------------------------------------------------------
		Date today = new Date(); 
		Calendar cal = Calendar.getInstance(); cal.setTime(today);
		int month = cal.get(Calendar.MONTH)+1, year = cal.get(Calendar.YEAR);
		String mes = month+""; if(month<10) mes= "0"+mes;
		fecha = year+"-"+mes;
	//---------------------------------------------------------------------------------------------------------------------------
		
		
		Empleado empleado = (Empleado) request.getAttribute("empleado");
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("empleado", empleado);
		
	//obtenemos todos los cliente ---------------------------------------------------------------------------------------------
		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(empleado.getId()+"", empleado.getCategoria()));
		String json = ws.clientes(wbList, "getClientes", "clientes.php");
		String[] jsonArray = json.split(";;");
		this.clientes.clear();
		clientes = pj.parsearClientesMap(jsonArray[1]);
		ob.clientesByDomain(clientes);
	//---------------------------------------------------------------------------------------------------------------------------
		
		System.out.println("Yo soy: "+empleado.getName()+"  ->  "+empleado.getCategoria());

		//empezar a filtrar por tipo de empleado
		request.setAttribute("name_user", empleado.getName());
		request.setAttribute("ventana",empleado.getPanel());
		request.getRequestDispatcher("Data.jsp").forward(request, response);
		
	}

	
	private void cargarVistaEnlaces(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String empleados, Empleado empleado) {
		System.out.println("Metodo: cargarVistaEnlaces");
		HashMap<String, String> json = listaClientes(empleados,"","","",0, empleado);
		out.println("<div id='lcc' class='listClients'>");
		out.println("	<div class='titleCategory'>");
		out.println("		<div class='titleInner'>Clientes<div class='horDiv wa'><div onclick='searchCliente(this,event)'><div id='ipCLient' onclick='stopPropagation()' class='srchI'><i onclick='searchCliente(this,event)' class='material-icons addKi'>search</i><input id='searchC' class='searchI' type='text' oninput='aplicarFiltroEnlaces(this)'></div></div></div></div>");
		out.println("	</div>");
		out.println("	<div class='infoCategory pr'>");
		out.println("		<div  class='info'>"+json.get("nClientes")+" clientes</div>");
		out.println("		<i class='tdCat material-icons gris fil_enl' onclick='openFilterEnlaces(this)'>filter_list</i>");
		out.println("	</div>");
		
		out.println("	<div class='div_filtros_enlaces'>");
		out.println("		<div class='filtros_Enlaces_seleccionados float_left'><div class='item_filter_group mgn_top' data-filter='empleado' data-valor='"+empleado.getId()+"'>"+empleado.getName().toLowerCase()+"<svg onclick='deteleItemFilterEnlaces(this)' class='delete_item_filter' height='24' viewBox='0 0 24 24' width='17'><path class='btn_detele_item_filter' d='M12 2c-5.53 0-10 4.47-10 10s4.47 10 10 10 10-4.47 10-10-4.47-10-10-10zm5 13.59l-1.41 1.41-3.59-3.59-3.59 3.59-1.41-1.41 3.59-3.59-3.59-3.59 1.41-1.41 3.59 3.59 3.59-3.59 1.41 1.41-3.59 3.59 3.59 3.59z'></path></svg></div></div>");
		out.println("		<div class='filtros_Enlaces effect7_insset float_left'>");
		out.println("				<div class='float_left'>");
		out.println("					<div class='section_filter'>Empleados</div>");
		for (int j = 0; j < Data.empleados.size(); j++) { 
			if(Data.empleados.get(j).getCategoria().equals(empleado.getCategoria()) && Data.empleados.get(j).getId()==empleado.getId()) {
				out.println("			<div data-filter='empleado' data-valor='"+Data.empleados.get(j).getId()+"' data-name='"+Data.empleados.get(j).getName().toLowerCase()+"' class='txt_opciones_filter'>"+Data.empleados.get(j).getName()+"<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox' checked/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
			}else if(Data.empleados.get(j).getCategoria().equals("free")) {
				out.println("			<div data-filter='empleado' data-valor='"+Data.empleados.get(j).getId()+"' data-name='"+Data.empleados.get(j).getName().toLowerCase()+"' class='txt_opciones_filter'>"+Data.empleados.get(j).getName()+"<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
			}
		}
		out.println("				</div>");
		
		out.println("				<div class='float_left pdd_left_5'>");
		out.println("					<div class='section_filter'>Estado cliente</div>");
		out.println("					<div data-filter='estado' data-valor='new' class='txt_opciones_filter'>Cliente nuevo<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='estado' data-valor='old' class='txt_opciones_filter'>Cliente normal<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='estado' data-valor='our' class='txt_opciones_filter'>Cliente YoSEO<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("				</div>");
		
		out.println("				<div class='float_left pdd_top_15'>");
		out.println("					<div class='section_filter'>Servicios</div>");
		out.println("					<div data-filter='servicio' data-valor='lite' class='txt_opciones_filter'>SEO Lite<div class='pretty p-icon p-smooth chkbx_filter '><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='pro' class='txt_opciones_filter'>SEO Pro<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='premium' class='txt_opciones_filter'>SEO Premium<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("					<div data-filter='servicio' data-valor='medida' class='txt_opciones_filter'>SEO a medida<div class='pretty p-icon p-smooth chkbx_filter'><input class='slT' type='checkbox'/><div class='state p-success paTem'><i class='icon material-icons'>done</i><label></label></div></div></div>");
		out.println("				</div>");
		
		out.println("			<div class='btn_filter' onclick='aplicarFiltroEnlaces(this)'>Aplicar</div>");
		out.println("		</div>");
		out.println("	</div>");
		
		out.println("	<div id='lstC' class='listItems'>");
		out.println(		json.get("html"));
		out.println("	</div>");
		out.println("</div>");
		out.println("	<div id='uniqueClient' class='uniqueClient'>");
		out.println("		<div class='containerApp'>");
		out.println("			<div class='msgSaludo'>Hola "+empleado.getName()+", </div>");
		out.println("			<div><span class='msg'>Selecciona un cliente.</span></div>");
		out.println("		</div>");
		out.println("	</div>");
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Empleado empleado = (Empleado) session.getAttribute("empleado");

		String metodo = request.getParameter("metodo");
		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		System.out.println("\nDo post:  "+ empleado.getId()+" - "+ empleado.getIdEmpleado()+" - "+metodo);
		
		if(metodo.equals("cargarVistaEnlaces")) {
			cargarVistaEnlaces(request, response, out, "{"+empleado.getId()+"}", empleado);
		}else if(metodo.equals("enlaces_SelectClient")) { 
			try {selectClient(request, response, out,empleado.getId(), empleado.getRole(), empleado);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_CheckClients")) {
			//try {checkClients(request, response, out,id_user,role);} catch (ParseException e) {}
		}else if(metodo.equals("enlaces_ResultadosMes")) {
			resultadosMes(request, response, out, empleado.getRole(), empleado);
		}else if(metodo.equals("guardarEnlaceResultado")) {
			guardarEnlaceResultado(request, response, out,empleado.getId(), empleado);
		}else if(metodo.equals("mostrarMedios")) {
			mostrarMedios(request, response, out, empleado);
		}else if(metodo.equals("borrarCategoriaResultado")) {
			borrarCategoriaResultado(request, response, out);
		}else if(metodo.equals("enlaces_guradarDestino")) {
			guradarDestino(request, response, out);
		}else if(metodo.equals("guardarCategoriaResultado")) {
			guardarCategoriaResultado(request, response, out);
		}else if(metodo.equals("guardarMedio")) {
			guardarMedio(request, response, out);
		}else if(metodo.equalsIgnoreCase("enlaces_buscarMedio")) {
			buscarMedio(request, response, out, empleado);
		}else if(metodo.equals("enlaces_nuevoDestino")) {
			nuevoDestino(request, response, out);
		}else if(metodo.equals("enlaces_GuardarAnchor")) {
			guardarAnchor(request, response, out);
		}/*else if(metodo.equals("enlaces_BuscarCliente")) {
			buscarCliente(request, response, out);
		}*/else if(metodo.equals("aplicarFiltrosEnlaces")) {
			aplicarFiltrosEnlaces(request, response, out, empleado);
		}else if(metodo.equals("addNewEnlacePaid")) {
			addNewEnlacePaid(request, response, out, empleado);
			
		}else if(metodo.equals("enlaces_guardarPrecio")) {
			enlaces_guardarPrecio(request, response, out);
		}


		
	}

	private void aplicarFiltrosEnlaces(HttpServletRequest request, HttpServletResponse response, PrintWriter out, Empleado empleado) throws IOException {
		System.out.println("Metodo: aplicarFiltrosEnlaces");
		String jsonEnlaces = request.getParameter("jsonEnlaces");
		String busquedaCliente = request.getParameter("busquedaCliente");
		int clienteSeleccionado = Integer.parseInt(request.getParameter("clienteSeleccionado"));
		
		
		String servicios="", usuarios="", estados="";
		
		
		
		Object jsonObject =JSONValue.parse(jsonEnlaces.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			String tipo = row.get("tipo").toString(), valor = row.get("valor").toString();
			if(tipo.equals("servicio"))servicios += "{"+valor+"}";
			if(tipo.equals("empleado"))usuarios += "{"+valor+"}";
			if(tipo.equals("estado"))estados += "{"+valor+"}";
		}
		
		HashMap<String, String> json = listaClientes(usuarios, estados,servicios,busquedaCliente,clienteSeleccionado, empleado);
		
		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();
		obj.put("listaClientes", json.get("html"));
		obj.put("n_clientes", json.get("nClientes"));
		out.print(obj);
		
	}
	
	private HashMap<String, String> listaClientes(String empleados, String estados, String servicios, String clienteBusqueda, int clienteSeleccionado, Empleado e) {
		System.out.println("Metodo: listaClientes");
		
		int nClientes = 0;
		int enlacesEmpleados = 0;
		
		String html="";
		int first = 0;
		for (int i = 0; i < clientes.size(); i++){
			Cliente c = clientes.get(i);
			//comprobamos que el cliente tiene asignado almenos un empkleado del filtro que le pasamos como paramatro 
			boolean mostrarUusario = false, mostrarEstado=false, mostrarServicio=false, mostrarClientePorBusqueda=false;
			int enlacesPorCliente=0, enlacesPorClienteHechos=0;
			
			for (Map.Entry<String, Empleado> empleado : c.getEmpleados().entrySet()) {
				int enlaces = empleado.getValue().getN_follows(), 
					idEmpleado = empleado.getValue().getId(),
					enlaces_hechos = empleado.getValue().getN_follows_done()
					;
				if(empleados.contains("{"+idEmpleado+"}")) {
					enlacesEmpleados += enlaces; 
					enlacesPorCliente += enlaces;
					enlacesPorClienteHechos += enlaces_hechos;
					mostrarUusario = true;
				}
			}
			
			//Aplicamos los filtros
			if(estados.contains("{"+c.getStatus()+"}") || estados.equals(""))mostrarEstado = true;
			if(servicios.contains("{"+c.getServicio()+"}") || servicios.equals(""))mostrarServicio = true;
			if(empleados.equals("")) mostrarUusario = true;
			if(clienteBusqueda.equals("") || c.getWeb().toLowerCase().contains(clienteBusqueda.toLowerCase()) || c.getNombre().toLowerCase().contains(clienteBusqueda.toLowerCase()) ) mostrarClientePorBusqueda = true;
			
			if(mostrarUusario && mostrarEstado && mostrarServicio && mostrarClientePorBusqueda) { 
				nClientes++;
				String itemSleccionado="";
				if(clienteSeleccionado == c.getIdCliente()) {itemSleccionado="item_select";}
				html+="		<div id='"+c.getIdCliente()+"' onclick='enlaces_SelectClient(this.id, this)' class='item "+itemSleccionado+"'>";
				if(first>0){html+="<div class='line'></div>";}
				html+="			<div class='itemChild";if(c.getEditando()==1)html+=" blur"; html+="'>";
				html+="				<div class='dominioItem'>";
				html+="					<span class='dominioItem' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+c.getDominio()+"</span>";
				html+="				</div>";
				html+="				<div class='nameItem sName'>"+c.getNombre()+"</div>";
				String opacity="";
				if(c.getEditando()==1){opacity="opacity_0";}
				
				
				
				if(e.getCategoria().equals("free")) {
					if((enlacesPorCliente-enlacesPorClienteHechos) == 0){
						html+="		<div class='noti notiPos "+opacity+"'><i class='material-icons lf'>done</i></div>";
					}else{
						html+="		<div class='noti "+opacity+"'>"+(enlacesPorCliente-enlacesPorClienteHechos)+"</div>";
					}
				}else if(e.getCategoria().equals("paid")) {
					
				}
				
				
				
				
				
				if(c.getStatus().equals("our")){ 		
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sYS_color'>bookmark</i><div class='div_line_bookmark sYS'></div></div>";
				}
				else if(c.getStatus().equals("new")){ 
					html+="			<div class='div_bookmark'><i class='material-icons div_i_bookmark sOK_color'>bookmark</i><div class='div_line_bookmark sOK'></div></div>";
				}
				html+="			</div>";
				html+="			<div class='blockClient"; if(c.getEditando()==1)html+=" visible";html+="'><div class='lockDiv'><i class='material-icons lf blur'> lock </i></div></div>";
				html+="			<div class='pop_up_info_blocked'>";
				html+="				<div class='msg_blocked'>Editando cliente por</div>";
				html+="				<div class='msg_name_blocked'>Guillermo</div>";
				html+="				<div class='lockDiv'><i class='material-icons lf'> lock </i></div>";
				html+="				<div class='lockDiv' style='left:23px;'><i class='material-icons lf'> lock </i></div>";
				html+="			</div>";
				html+="			<div class='loader'><div class='l_d1'></div><div class='l_d2'></div><div class='l_d3'></div><div class='l_d4'></div><div class='l_d5'></div></div>";
				html+="		</div>";
				first=1;
			}
		}
		HashMap<String, String> json = new HashMap<String,String>();
		json.put("html", html);
		json.put("nClientes", nClientes+"");
		
		//System.out.println("Total enlaces: "+enlacesEmpleados);
		return json;
	}



	@SuppressWarnings({ "unchecked", "unused" })
	private void selectClient(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_empleado, String user_role, Empleado empleado) throws IOException, ParseException {
		System.out.println("Metodo: selectClient");
		
		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();
		String blocked="",userEditando="";
		String id_client = request.getParameter("id_client");
		String users = request.getParameter("users");
		
		
		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_client+"", id_empleado+"", fecha+"",users, empleado.getCategoria()));
		String json = ws.clientes(wbList, "getEnlaces", "enlaces.php");
		
		System.out.println("json enlaces: "+json);
		String[] jsonArray = json.split(";;");
		System.out.println(jsonArray[0]);
		//System.out.println("0- "+jsonArray[0]);//array de enlaces
		//System.out.println("2- "+jsonArray[3]);//array de los enlaces hecho y por hacer de cada empoleado en el cliente seleccionado 
 
		int disponibilidad=0;
		//parseamos los enlaces (resultados)
		ArrayList<Enlace> enlaces = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonArray[0], new TypeToken<List<Enlace>>(){}.getType());
		if(enlaces.size() != 0) {
			disponibilidad = enlaces.get(0).getDisponibilidad();
			userEditando = enlaces.get(0).getUserEditando();	
		}else if(enlaces.size() == 0) {//si es igual a cero significa que no se ha devuelto ningun resultado
			disponibilidad = 2;
		}
		if(disponibilidad==1) {
			
			//parseamos los foros disponibles
			ArrayList<Foro> forosDisponibles = new Gson().fromJson(jsonArray[1], new TypeToken<List<Foro>>(){}.getType());
			this.forosDisponibles.clear();
			this.forosDisponibles = forosDisponibles;

			this.enlaces.clear();
			this.enlaces = enlaces;
			//parseamos al cliente seleccionado
			Cliente cliente = new Gson().fromJson(jsonArray[2].substring(1, jsonArray[2].length()-1), Cliente.class);
			this.cliente = cliente;
			
			if(empleado.getCategoria().equals("free")) {
				String noti="";
				int porHacer = pj.parsearEnlacesPorClienteEmpleado(jsonArray[3]) ;
				if(porHacer == 0){noti="<div class='noti notiPos'><i class='material-icons lf'>done</i></div>";}else{noti="<div class='noti'>"+porHacer+"</div>";}
				obj.put("noti", noti);
			}
			
			
			
			mostrarResultados(request, response, out, obj, empleado);
			
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

	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out,JSONObject obj, Empleado empleado) throws IOException, ParseException {
		System.out.println("Metodo: mostrarResultados");
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
		html+="<div class='ctoolbar'><div class='add_div_toolbar_enlaces' onclick='addNewEnlacePaid(this)' ><i class='material-icons gris'>add_circle_outline</i></div><div id='cGuardar' class='zoom'>guardar</div></div>";
		html+="<div class='div_table'>";
		//html+="	<div class='titleTable'>Keywords<div class='horDiv'></div></div>";
		html+="	<div id='results_Enlaces' class='contentTable'>";
		//tabla
		html+="		<table id='tClientes' class='table'>";
		if(empleado.getCategoria().equals("free")) {
			html+="		<thead class=''><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer' onclick='viewEmpleadoDone(this)' ><i class='material-icons f_size26'>account_circle</i></th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable cCateg'>Categoria</th><th class='cabeceraTable cWeb'>Medio</th><th class='cabeceraTable cLink'>Enlace</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cTipo'>Tipo</th></tr></thead>";
		}else if(empleado.getCategoria().equals("paid")){
			html+="		<thead class=''><tr><th class='cabeceraTable cStatus'><div class='divStatus sPendiente'></th><th class='cabeceraTable cCUser txt_center_mg_0 cursor_pointer' onclick='viewEmpleadoDone(this)' ><i class='material-icons f_size26'>account_circle</i></th><th class='cabeceraTable cDest'>Destino</th><th class='cabeceraTable pago_web'>Medio</th><th class='cabeceraTable cAnchor'>Anchor</th><th class='cabeceraTable cLinkPaid'>Enlace</th><th class='cabeceraTable cPrecio'><i class='material-icons lnf'>attach_money</i></th><th class='cabeceraTable cPrecio'><i class='material-icons sOK_color'>attach_money</i></th><th class='cabeceraTable cPrecio'><i class='material-icons lf'> trending_up </i></th><th class='cabeceraTable cPrecio'><i class='material-icons warning_color'> bar_chart </i></th></tr></thead>";
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

	private void resultadosMes(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String user_role, Empleado empleado) throws IOException {
		System.out.println("Metodo: resultadosMes");
		boolean respuesta = true;//con esto comprobamos que tenemos algun resultado en el mes seleccionado
		String mes = request.getParameter("mes");
		String year = request.getParameter("year");
		
		String usuarios="";
		
		//mostramos solos los enlaces corespondiente a cada usurios seleccionado en el filtro
		String jsonEnlaces = request.getParameter("jsonEnlaces");
		Object jsonObject =JSONValue.parse(jsonEnlaces.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			String tipo = row.get("tipo").toString(), valor = row.get("valor").toString();
			if(tipo.equals("empleado"))usuarios += "{"+valor+"}";
		}

		String categoriasLi = "<i onclick='borrarCategoria(this)' class='material-icons crossReset'> clear </i>";
		for (int j = 0; j < Data.categorias.size(); j++) {
			if(Data.categorias.get(j).getType().equals(empleado.getCategoria())) {
				categoriasLi += "<li id='"+Data.categorias.get(j).getIdCategoria()+"' class='' onclick='guardarCategoriaResultado(this)'>"+Data.categorias.get(j).getEnlace()+"</li>";
			}
		} 
					 
		
		if(enlaces.get(0).getFecha().toString().startsWith(year+"-"+mes)) {//COMPOBAMOS QUE LA FEMCHA MESUAL COINCIDE PORQUE SINO DEBEMOS PEDIR AL SERVIDOR LOS DATOS DEL MES SELECCIONADO
		}else {//si el mes no coincide 
			
			//parseamos los enlaces (resultados)
			System.out.println("@@"+year+"-"+mes);
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(cliente.getIdCliente()+"", year+"-"+mes, empleado.getCategoria()));
			String json = ws.clientes(wbList, "getEnlacesAnteriores", "enlaces.php");
			
			//String json = ws.getEnlaces(cliente.getIdCliente()+"", year+"-"+mes ,"","getEnlacesAnteriores","enlaces.php");
			
			String[] jsonArray = json.split(";;"); 
			System.out.println("**"+jsonArray[1]);
			//Esto es irrelevante, en un futuro hacer mas pruebas y ver si conviene quitar esta peticion 
			ArrayList<Foro> forosDisponibles = new Gson().fromJson(jsonArray[0], new TypeToken<List<Foro>>(){}.getType());
			this.forosDisponibles.clear();
			this.forosDisponibles = forosDisponibles;

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
				
				if(empleado.getCategoria().equals("free")) {
					tablaEnlacesFREE(out, empleado, usuarios, categoriasLi, urlsAAtacarLi, onHoverGoLink, i, e);
				}else if(empleado.getCategoria().equals("paid")) {
					tablaEnlacesPAID(out, empleado, usuarios, urlsAAtacarLi, onHoverGoLink, i, e);
				}
				
			}
		}else {
			
		}
		

	}
	
	private void tablaEnlacesPAID(PrintWriter out, Empleado empleado, String usuarios, String urlsAAtacarLi, String onHoverGoLink, int i, Enlace e) {
		if(usuarios.contains("{"+e.getIdEmpleado()+"}") || e.getIdEmpleado().equals("0")) {
			int id_resultado = Integer.parseInt(e.getIdResultado());
			int posicionForo=-1;
			for (Foro f : forosDisponibles) {
				if(e.getIdForo()==f.getIdForo() && f.getType().equals(empleado.getCategoria())) {
					posicionForo = forosDisponibles.indexOf(f);break;
				}
			}

			String claseStatus="";
			if(!e.getEnlace().trim().equalsIgnoreCase("")) claseStatus="sOK";
			else claseStatus="sPendiente";

			String claseTipo="",tipo="";
			if(e.getTipo().equalsIgnoreCase("follow")) {claseTipo="lf";tipo="follow";}
			else if(e.getTipo().equalsIgnoreCase("nofollow")) {claseTipo="lnf";tipo="nofollow";}

			//lista de categorias-----------------------------
			String nameCategoria = "Selecciona una categor&iacute;a";
			if(e.getCategoria()!=0)nameCategoria = e.getNombreCategoria(); 

			String botonDescripcion="";
			if(!e.getDescripcionForo().trim().equals("")) botonDescripcion="<i onclick='enlace_openDescription(this)' class='material-icons description_enlace goLink'>notes</i>";
			
			//opcion de poder modificar el enlace siempre y cuando no se haya registrado ningun enlace por otro usuario
			String readonly="readonly",openDestinos="", openCategorias="", editEnlace="",editAnchor="",openMedios="";
			boolean editar=false;
			//System.out.println("Traz 1 = "+ Integer.parseInt(e.getHechoPor()) +" == "+ empleado.getId()+"  "+e.getEnlace());
			if(Integer.parseInt(e.getHechoPor()) == -1 || Integer.parseInt(e.getHechoPor()) == empleado.getId()) {
				readonly = "";openDestinos="onclick='openDestinos(this)'";openCategorias="onclick='openCategoriaResultado(this)'";editEnlace="onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)'";
				editAnchor="onchange='guardarAnchor(this)' oninput='saveClient(this)'" ; openMedios="onclick='openMediosPaid(this)'";
				editar=true;
			}
			
			String empleadoDone = "";
			try {empleadoDone = Data.empleadosHashMap.get(e.getHechoPor()+"").getName();} catch (java.lang.NullPointerException e2) {}
			
			//TABLA----------------------
			out.println("<tr id='"+id_resultado+"' posicion='"+i+"' >");
			out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");

			out.println("	<td class='tdCat cCUser pr txt_center_mg_0'>");
			out.println("		<div class='tdCat tdWeb txt_center_mg_0'>");
			out.println("			<span data-info='empCli' data-id-empleado='"+e.getIdEmpleado()+"' class='tdCat empleadoDefault visible' type='text'>"+e.getNameEmpleado()+"</span>");
			out.println("			<span data-info='empDone' data-id-empleado-done='"+e.getHechoPor()+"' class='tdCat empleadoDone' type='text'>"+empleadoDone+"</span>");
			out.println("		</div>"); 
			out.println("	</td>");
			
			out.println("	<td class='tdCat cell_destino pr' "+openDestinos+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>"+e.getDestino()+"</span>");
			if (editar)out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println("		</div>");
			if (editar) {
			out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
			out.println(			"<i onclick='enlaces_guradarDestino(this)' class='material-icons crossReset'> clear </i>");
			out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text'  onkeypress='enlaces_nuevoDestinoEnter(event,this)' placeholder='Nuevo destino'><i onclick='enlaces_nuevoDestino(this)' class='material-icons p_a add_destinos_enlace'>add</i></div>");
			out.println(			"<ul class='slWeb pdd_v_10'>"+urlsAAtacarLi+"</ul>");
			out.println(		"</div>");
			}
			out.println("	</td>");
			
			out.println("	<td class='tdCat tdWeb cWeb pr' "+openMedios+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span data-id-foro='"+e.getIdForo()+"' data-posicion-foro='"+posicionForo+"' data-id-categoria='"+e.getCategoria()+"' data-type='"+e.getType()+"'  onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb goLink'>"+e.getWebForo()+"</span>");
			if (editar)out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println(			botonDescripcion); 
			out.println("		</div>");
			if (editar) {
			out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
			out.println(			"<i onclick='guardarMedioPaid(this)' class='material-icons crossReset'> clear </i>");
			out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text' oninput='buscarMedio(this)' placeholder='Busca un medio'><i class='material-icons p_a lupa_medios'>search</i></div>");
			out.println(			"<ul class='slWeb'></ul>");
			out.println(		"</div>");
			}
			out.println("		<textarea class='div_enlaces_pop_up pop_up effect7 div_enlaces_descripcion_pop_up'  wrap='hard' onclick='stopPropagation()' readonly>");
			out.println(			e.getDescripcionForo().trim().replace("<", "&lt;").replace(">", "&gt;"));
			out.println(		"</textarea>");
			out.println("	</td>");

			out.println("	<td class='cAnchor'><input class='inLink' "+editAnchor+" type='text' value='"+e.getAnchor()+"' "+readonly+"></td>");
			
			out.println("	<td class='cLinkPaid'>");
			out.println("		<input class='inLink' "+editEnlace+" onclick='goLink(this)' "+onHoverGoLink+"  type='text' value='"+e.getEnlace()+"' "+readonly+">");
			out.println("	</td>");

			
			int precioCompra = Integer.parseInt(e.getPrecioCompra());
			int precioVenta  = Integer.parseInt(e.getPrecioVenta());
			
			int beneficio = precioVenta - precioCompra;
			double incremento = (beneficio / (precioCompra*1.0))*100;
			DecimalFormat f = new DecimalFormat("##");
			int inc = 0;
			try {inc= Integer.parseInt(f.format(incremento));} catch (Exception e2) {}
			
			out.println("	<td class='txt_center_mg_0 pr precio_compra'><span class='span_precio' onclick='openPrecio(this)'>"+precioCompra+" &euro;</span><input class='input_precio' data-tipo='compra' onfocusout='closePrecio(this)' onchange='guardarPrecio(this)' type='text'/></td>");
			out.println("	<td class='txt_center_mg_0 pr precio_venta '><span class='span_precio' onclick='openPrecio(this)'>"+ precioVenta+" &euro;</span><input class='input_precio' data-tipo='venta'  onfocusout='closePrecio(this)' onchange='guardarPrecio(this)' type='text'/></td>");
			out.println("	<td class='txt_center_mg_0 beneficio'>"+beneficio+" &euro;</td>");
			out.println("	<td class='txt_center_mg_0 incremento'>"+inc+" %</td>");
			out.println("</tr>");
		
		
		}
	}

	private void tablaEnlacesFREE(PrintWriter out, Empleado empleado, String usuarios, String categoriasLi, String urlsAAtacarLi, String onHoverGoLink, int i, Enlace e) {
		if(usuarios.contains("{"+e.getIdEmpleado()+"}") || e.getIdEmpleado().equals("0")) {
			int id_resultado = Integer.parseInt(e.getIdResultado());
			int posicionForo=-1;
			for (Foro f : forosDisponibles) {
				if(e.getIdForo()==f.getIdForo() && f.getType().equals(empleado.getCategoria())) {
					posicionForo = forosDisponibles.indexOf(f);break;
				}
			}

			String claseStatus="";
			if(!e.getEnlace().trim().equalsIgnoreCase("")) claseStatus="sOK";
			else claseStatus="sPendiente";

			String claseTipo="",tipo="";
			if(e.getTipo().equalsIgnoreCase("follow")) {claseTipo="lf";tipo="follow";}
			else if(e.getTipo().equalsIgnoreCase("nofollow")) {claseTipo="lnf";tipo="nofollow";}

			//lista de categorias-----------------------------
			String nameCategoria = "Selecciona una categor&iacute;a";
			if(e.getCategoria()!=0)nameCategoria = e.getNombreCategoria(); 

			String botonDescripcion="";
			if(!e.getDescripcionForo().trim().equals("")) botonDescripcion="<i onclick='enlace_openDescription(this)' class='material-icons description_enlace goLink'>notes</i>";
			
			//opcion de poder modificar el enlace siempre y cuando no se haya registrado ningun enlace por otro usuario
			String readonly="readonly",openDestinos="", openCategorias="", editEnlace="",editAnchor="",openMedios="";
			boolean editar=false;
			//System.out.println("Traz 1 = "+ Integer.parseInt(e.getHechoPor()) +" == "+ empleado.getId()+"  "+e.getEnlace());
			if(Integer.parseInt(e.getHechoPor()) == -1 || Integer.parseInt(e.getHechoPor()) == empleado.getId()) {
				readonly = "";openDestinos="onclick='openDestinos(this)'";openCategorias="onclick='openCategoriaResultado(this)'";editEnlace="onchange='guardarEnlaceResultado(this)' oninput='saveClient(this)'";
				editAnchor="onchange='guardarAnchor(this)' oninput='saveClient(this)'" ; openMedios="onclick='openMedios(this)'";
				editar=true;
			}
			
			String empleadoDone = "";
			try {empleadoDone = Data.empleadosHashMap.get(e.getHechoPor()+"").getName();} catch (java.lang.NullPointerException e2) {}
			
			//TABLA----------------------
			out.println("<tr id='"+id_resultado+"' posicion='"+i+"' >");
			out.println("	<td class='cStatus'><div class='divStatus "+claseStatus+"'></div></td>");

			out.println("	<td class='tdCat cCUser pr txt_center_mg_0'>");
			out.println("		<div class='tdCat tdWeb txt_center_mg_0'>");
			out.println("			<span data-info='empCli' data-id-empleado='"+e.getIdEmpleado()+"' class='tdCat empleadoDefault visible' type='text'>"+e.getNameEmpleado()+"</span>");
			out.println("			<span data-info='empDone' data-id-empleado-done='"+e.getHechoPor()+"' class='tdCat empleadoDone' type='text'>"+empleadoDone+"</span>");
			out.println("		</div>"); 
			out.println("	</td>");
			
			out.println("	<td class='tdCat cell_destino pr' "+openDestinos+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' class='tdCat tdWeb'>"+e.getDestino()+"</span>");
			if (editar)out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println("		</div>");
			if (editar) {
			out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
			out.println(			"<i onclick='enlaces_guradarDestino(this)' class='material-icons crossReset'> clear </i>");
			out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text'  onkeypress='enlaces_nuevoDestinoEnter(event,this)' placeholder='Nuevo destino'><i onclick='enlaces_nuevoDestino(this)' class='material-icons p_a add_destinos_enlace'>add</i></div>");
			out.println(			"<ul class='slWeb pdd_v_10'>"+urlsAAtacarLi+"</ul>");
			out.println(		"</div>");
			}
			out.println("	</td>"); 
			
			out.println("	<td class='tdCat cCateg pr' "+openCategorias+">");
			out.println("		<div class='tdCat'>");
			out.println("			<span class='tdCat' data-id-categoria='"+e.getCategoria()+"'  data-type='"+e.getType()+"'>"+nameCategoria+"</span>");
			if (editar)out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println("		</div>");
			if (editar)out.println(		"<ul class='slCt effect7 pop_up'>"+categoriasLi+"</ul>");
			out.println("	</td>");
			
			out.println("	<td class='tdCat tdWeb cWeb pr' "+openMedios+">");
			out.println("		<div class='tdCat tdWeb'>");
			out.println("			<span data-id-foro='"+e.getIdForo()+"' data-posicion-foro='"+posicionForo+"' data-id-categoria='"+e.getCategoria()+"' data-type='"+e.getType()+"'  onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb goLink'>"+e.getWebForo()+"</span>");
			if (editar)out.println("			<i class='material-icons arrow'>arrow_drop_down</i>");
			out.println(			botonDescripcion); 
			out.println("		</div>");
			if (editar) {
			out.println(		"<div class='div_enlaces_pop_up effect7 pop_up'>");
			out.println(			"<i onclick='guardarMedio(this)' class='material-icons crossReset'> clear </i>");
			out.println(			"<div onclick='stopPropagation(this)' class='border_bottom_gris inner_pop_up' ><input class='inLink inner_pop_up input_search_medio' type='text' oninput='buscarMedio(this)' placeholder='Busca un medio'><i class='material-icons p_a lupa_medios'>search</i></div>");
			out.println(			"<ul class='slWeb'></ul>");
			out.println(		"</div>");
			}
			out.println("		<textarea class='div_enlaces_pop_up pop_up effect7 div_enlaces_descripcion_pop_up'  wrap='hard' onclick='stopPropagation()' readonly>");
			out.println(			e.getDescripcionForo().trim().replace("<", "&lt;").replace(">", "&gt;"));
			out.println(		"</textarea>");
			out.println("	</td>");

			out.println("	<td class='cLink'>");
			out.println("		<input class='inLink' "+editEnlace+" onclick='goLink(this)' "+onHoverGoLink+"  type='text' value='"+e.getEnlace()+"' "+readonly+">");
			out.println("	</td>");

			out.println("	<td class='cAnchor'><input class='inLink' "+editAnchor+" type='text' value='"+e.getAnchor()+"' "+readonly+"></td>");
			out.println("	<td tipo='"+tipo+"' class='cTipo'><i class='material-icons "+claseTipo+"'>link</i></td>");
			out.println("</tr>");
		}
	}

	private void guardarEnlaceResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_user, Empleado empleado) {
		System.out.println("Metodo: guardarEnlaceResultado");
		String link = request.getParameter("link");
		//System.out.println("\nEnlace a subir:  "+link);
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int id_empleado = Integer.parseInt(request.getParameter("id_empleado"));
		int follows_done_empleado = Integer.parseInt(request.getParameter("follows_done_empleado"));
		int follows_por_hacer = Integer.parseInt(request.getParameter("follows_por_hacer"));
		
		//int follows_done = Integer.parseInt(request.getParameter("follows_done"));
		int nofollows_done = Integer.parseInt(request.getParameter("nofollows_done"));
		String mes_js = request.getParameter("mes");
		String year_js = request.getParameter("year");

		/*
		Date date= new Date();Calendar cal = Calendar.getInstance();cal.setTime(date);
		int mes = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);

		String mes_string = mes+"";
		if(mes<10) mes_string = "0"+mes;
		*/
		
		int vacio = 0;
		if(link.trim().equals("")) vacio = 1;
		
		int sameEmpleado = 0;
		if(id_empleado == empleado.getId()) {
			sameEmpleado = 1;  
		}
		System.out.println("Enlaces hechos por el empleado que deberia hacer el enlace: "+follows_done_empleado);
		
		
		//id resultado, elimar o a�adir enlace, enlace, de quien es el enlace, quien lo ha hecho
		
		/*
		ws.updateResultado(id_resultado+"", "enlace", link+"" , "updateResultado.php");
		ws.updateResultado(id_resultado+"", "hecho_por", id_user+"" , "updateResultado.php");
		*/
		
		System.out.println(fecha +" <-> "+year_js+"-"+mes_js);

		if(fecha.equals(year_js+"-"+mes_js)) {
			//String fecha = year+"-"+mes_string;
			System.out.println("@: Id enlace:"+id_resultado +"  Enlace de: "+id_empleado +"   Hecho por:"+ empleado.getId());
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_resultado+"",sameEmpleado+"", link.trim(), id_empleado+"", empleado.getId()+"", vacio+"", follows_done_empleado+"", fecha));
			String json = ws.clientes(wbList, "guardarEnlacesDone", "enlaces.php");
			//System.out.println("@:"+json);
			
			
			//cliente.setFollowsDone(follows_done);
			//cliente.setNofollowsDone(nofollows_done);
			//System.out.println(cliente.getFollows());
			//System.out.println(cliente.getNofollows());
			//ws.updateCliente(cliente.getIdCliente()+"","follows_done",cliente.getFollowsDone()+"","updateCliente.php");
			//ws.updateCliente(cliente.getIdCliente()+"","nofollows_done",cliente.getNofollowsDone()+"","updateCliente.php");
		}
		
		//System.out.println(cliente.getFollows()-cliente.getFollowsDone()+"�");


		if(follows_por_hacer==0) {
			out.println("<div class='noti notiPos'><i class='material-icons lf'>done</i></div>");
		}else {
			out.println("<div class='noti'>"+(follows_por_hacer)+"</div>");
		}

	}


	private void borrarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		System.out.println("Metodo: guardarCategoriaResultado");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int id_foro_anterior = Integer.parseInt(request.getParameter("id_foro_anterior"));
		int categoria_foro_anterior = Integer.parseInt(request.getParameter("categoria_foro_anterior"));
		String medio_anterior = request.getParameter("medio_anterior");
		String descripcion_foro_anterior = request.getParameter("descripcion_foro_anterior");
		String type_foro_anterior = request.getParameter("type_foro_anterior");

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "categoria", "0" , "updateResultado.php");
		ws.updateResultado(id_resultado+"", "id_foro", "0" , "updateResultado.php");
		out.println("<span class='tdCat' data-id-categoria='0'>Selecciona una categor&iacute;a</span> <i class='material-icons arrow'>arrow_drop_down</i>");

		if(id_foro_anterior>0) {
			forosDisponibles.add(new Foro(id_foro_anterior, medio_anterior, categoria_foro_anterior, descripcion_foro_anterior, type_foro_anterior));
		}
		System.out.println("Insertado");

	}
	private void guardarCategoriaResultado(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		System.out.println("Metodo: guardarCategoriaResultado");
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "categoria", idCategoria+"" , "updateResultado.php");
		System.out.println("Insertado");
	}
	
	private void enlaces_guardarPrecio(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		System.out.println("Metodo: guardar Precio");
		String campo = request.getParameter("campo");
		String valor = request.getParameter("valor");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", campo, valor , "updateResultado.php");
		System.out.println("Insertado");
	}

	private void mostrarMedios(HttpServletRequest request, HttpServletResponse response, PrintWriter out, Empleado empleado) {
		System.out.println("Metodo: mostrarMedios");
		int idCategoria = Integer.parseInt(request.getParameter("id_categoria"));

		ob.forosByWeb(forosDisponibles);
		
		for (Foro f : forosDisponibles) {
			if(empleado.getCategoria().equals(f.getType()) && empleado.getCategoria().equals("free")) {
				if(idCategoria==0) {//si la categoria es 0 mostramos todos los enlaces 
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"'  data-type='"+f.getType()+"'  onclick='guardarMedio(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}else if(f.getCategoria()==idCategoria) {
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"'  data-type='"+f.getType()+"'  onclick='guardarMedio(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}
			}else if(empleado.getCategoria().equals(f.getType()) && empleado.getCategoria().equals("paid")) {
				if(idCategoria==0) {//si la categoria es 0 mostramos todos los enlaces 
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"'  data-type='"+f.getType()+"'  onclick='guardarMedioPaid(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}else if(f.getCategoria()==idCategoria) {
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"'  data-type='"+f.getType()+"'  onclick='guardarMedioPaid(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}
			}
		}
	}
	
	private void guardarMedio(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		System.out.println("Metodo: guardarMedio");
		response.setContentType("application/json");
		out = response.getWriter();
		JSONObject obj = new JSONObject();

		int id_categoria_tdCategoria = Integer.parseInt(request.getParameter("id_categoria_tdCategoria"));

		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		int id_foro = Integer.parseInt(request.getParameter("id_foro"));//id en la base de datos del foro seleccionado
		int posicion_foro = Integer.parseInt(request.getParameter("posicion_foro"));
		int id_categoria = Integer.parseInt(request.getParameter("categoria_foro"));
		String type = request.getParameter("type_foro");

		int id_foro_anterior = Integer.parseInt(request.getParameter("id_foro_anterior"));
		int posicion_foro_anterior = Integer.parseInt(request.getParameter("posicion_foro_anterior"));
		int id_categoria_anterior = Integer.parseInt(request.getParameter("categoria_foro_anterior"));
		String descripcion_foro_anterior = request.getParameter("descripcion_foro_anterior");
		String medio_anterior = request.getParameter("medio_anterior");
		String type_anterior = request.getParameter("type_foro_anterior");

		//Aplicamos los cambios en la bbdd y en el array
		ws.updateResultado(id_resultado+"", "id_foro", id_foro+"" , "updateResultado.php");

		String medio ="";
		if(posicion_foro==-1 && id_categoria==0 ) 	medio = "";
		else medio = forosDisponibles.get(posicion_foro).getWebForo();

		String html="",descripcion="";
		html+="<span  data-id-foro='"+id_foro+"' data-posicion-foro='-1' data-id-categoria='"+id_categoria+"' data-type='"+type+"' onmouseover='viewCampo(this)' onmouseout='restartCampo(this)'  onclick='openUrl(this, event)' class='tdCat tdWeb goLink'>"+medio+"</span>";
		html+="<i class='material-icons arrow'>arrow_drop_down</i>";
		if(id_foro>0) {
			html+="<i onclick='enlace_openDescription(this)' class='material-icons description_enlace goLink'>notes</i>";
			descripcion = forosDisponibles.get(posicion_foro).getDescripcion();
		}

		if(posicion_foro!=-1) forosDisponibles.remove(posicion_foro);

		if(posicion_foro_anterior==-1 && id_foro_anterior>0) forosDisponibles.add(new Foro(id_foro_anterior, medio_anterior, id_categoria_anterior, descripcion_foro_anterior, type_anterior));
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
	private void buscarMedio(HttpServletRequest request, HttpServletResponse response, PrintWriter out, Empleado empleado) {
		System.out.println("Metodo: buscarMedio");
		String texto = request.getParameter("texto");
		int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
		for (Foro f : forosDisponibles) {
			if(empleado.getCategoria().equals(f.getType())) {
				if(id_categoria==0 && f.getWebForo().toLowerCase().contains(texto.toLowerCase())) {//si la categoria es 0 mostramos todos los enlaces 
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' data-type='"+f.getType()+"' onclick='guardarMedio(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}else if(f.getCategoria()==id_categoria && f.getWebForo().toLowerCase().contains(texto.toLowerCase())) {
					out.println("<li data-id-foro='"+f.getIdForo()+"' data-posicion-foro='"+forosDisponibles.indexOf(f)+"' data-id-categoria='"+f.getCategoria()+"' data-type='"+f.getType()+"' onclick='guardarMedio(this)'><span onmouseover='viewCampo(this)' onmouseout='restartCampo(this)' >"+f.getWebForo()+"</span></li>");
				}
			}
		}
	}
	private void guradarDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		System.out.println("Metodo: guradarDestino");
		String destino = request.getParameter("destino");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "destino", destino+"" , "updateResultado.php");
		System.out.println("Insertado");

	}

	private void nuevoDestino(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		System.out.println("Metodo: nuevoDestino");
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
		System.out.println("Metodo: guardarAnchor");
		String anchor = request.getParameter("anchor");
		int id_resultado = Integer.parseInt(request.getParameter("id_resultado"));
		ws.updateResultado(id_resultado+"", "anchor", anchor+"" , "updateResultado.php");
		System.out.println("Insertado");
	}

	private void addNewEnlacePaid(HttpServletRequest request, HttpServletResponse response, PrintWriter out,Empleado empleado) {
		
		int id_cliente = Integer.parseInt(request.getParameter("id_cliente"));
		
		System.out.println("Vamos a a�idar un nuevo enlace para el cliente numero: "+id_cliente);
		
		if(empleado.getCategoria().equals("paid")) {
			ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_cliente+"", empleado.getId()+"",empleado.getCategoria()));
			String json = ws.clientes(wbList, "addNewEnlacePaid", "enlaces.php");
			System.out.println("-> "+json);
		}
		
		
	}













}



















