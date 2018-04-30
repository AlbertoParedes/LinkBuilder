package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

import Classes.Webservice;
import Objects.Cliente;
import Objects.Foro;
import Objects.Resultado;


@WebServlet("/Data")
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private Cliente cliente = new Cliente();
	private int id_user ;
	private String name_user;

	
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

		Webservice ws = new Webservice();
		
		String json = ws.getJSON("getCliRes.php");
		System.out.println(json);
	
		
		Object jsonObject =JSONValue.parse(json.toString());
	    JSONArray arrayData = (JSONArray)jsonObject;
	   
	    ArrayList<Cliente> clientes = new ArrayList<Cliente>();

	    
	    int id_c = -1; 
	    
	    for(int j=0;j<arrayData.size();j++){
	    	
	        JSONObject row =(JSONObject)arrayData.get(j);
	        
	        //para el cliente
	        int id_cliente = Integer.parseInt(row.get("id_cliente").toString());
	        String web_cliente = (String) row.get("web");
	        String nombre = (String) row.get("nombre");
	        String servicio = (String) row.get("servicio");
	        int follows = Integer.parseInt(row.get("follows").toString());
	        int nofollows = Integer.parseInt(row.get("nofollows").toString());
	        String anchorC = (String) row.get("anchorC");
	        int blog = Integer.parseInt(row.get("blog").toString());
	        String idioma = row.get("idioma").toString();
	        int follows_done = Integer.parseInt(row.get("follows_done").toString());
	        int nofollows_done = Integer.parseInt(row.get("nofollows_done").toString());
	        String linkbuilder = (String) row.get("linkbuilder");     
	        
	        //para el array de resultados del cliente
	        
	        int id_resultado = Integer.parseInt(row.get("id_resultado").toString());
	        int id_foro = Integer.parseInt(row.get("id_foro").toString());
	        String enlace = (String) row.get("enlace");
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateInString = (String) row.get("fecha");
            Date dia = formatter.parse(dateInString);
            java.sql.Date date = new java.sql.Date(dia.getTime());
	        String tipoRes = (String) row.get("tipoRes");
	        String destino = (String) row.get("destino");
	        int categoria = Integer.parseInt(row.get("categoria").toString());
	        String estado = (String) row.get("estado");
	        String anchorR = (String) row.get("anchorR");
	        
	        //para el array de foros del cliente
	        
	        String web_foro = (String) row.get("web_foro");
	        int dr = Integer.parseInt(row.get("DR").toString());
	        int da = Integer.parseInt(row.get("DA").toString());
	        String tipoF = (String) row.get("tipo");
	        String tematica = (String) row.get("tematica");
	        String descripcion = (String) row.get("descripcion");
	        int req_aprobacion =Integer.parseInt(row.get("req_aprobacion").toString());
	        int req_registro = Integer.parseInt(row.get("req_registro").toString());
	        int aparece_fecha =Integer.parseInt(row.get("aparece_fecha").toString());
	        String reutilizable = (String) row.get("reutilizable");

	        
	        if(id_c != id_cliente) {
	        	Resultado r = new Resultado(nombre, id_resultado, id_cliente, id_foro, enlace, date, tipoRes, destino, categoria, estado, anchorR);
	        	Foro f = new Foro(id_foro, web_foro, tipoF, dr, da, tematica, descripcion, categoria, req_aprobacion, req_registro, aparece_fecha, reutilizable);
	        	Cliente c = new Cliente(id_cliente, web_cliente, nombre, servicio, follows, nofollows, anchorC, blog, idioma, follows_done, nofollows_done, linkbuilder, new ArrayList<Resultado>(), new ArrayList<Foro>());
	        	c.getResultados().add(r);
	        	c.getForos().add(f);
	        	clientes.add(c);
	        	
	 	        //System.out.println(id_cliente);
	        	id_c = id_cliente;
	        }else {
	        	Resultado r = new Resultado(nombre, id_resultado, id_cliente, id_foro, enlace, date, tipoRes, destino, categoria, estado, anchorR);
	        	Foro f = new Foro(id_foro, web_foro, tipoF, dr, da, tematica, descripcion, categoria, req_aprobacion, req_registro, aparece_fecha, reutilizable);
	        	
	        	clientes.get(clientes.size()-1).getResultados().add(r);
	        	clientes.get(clientes.size()-1).getForos().add(f);
	        	
	        }
	       
	        
	    }

		this.clientes = clientes;
		
		String f = name_user.substring(0, 1).toUpperCase();
		name_user = f + name_user.substring(1,name_user.length()).toLowerCase();

		request.setAttribute("clientes", clientes);//pasamos la lista de clientes
		request.setAttribute("name_user", name_user);
		
		request.getRequestDispatcher("Data.jsp").forward(request, response);

		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		String metodo = request.getParameter("metodo");

		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();

		if(request.getParameter("posicion")!=null) {
			int posicion = Integer.parseInt(request.getParameter("posicion"));
			cliente = clientes.get(posicion);
		}

		out = response.getWriter();

		if (metodo.equals("lkc")) {
			mostrarResultados(request, response, out);
		}
		
		
		
		
		
		else if(metodo.equals("lkk")){
			mostrarKeywordsKeywords(request, response, out);
		}else if(metodo.equals("lkr")) {
			mostrarDatosKeyword(request, response, out);
		}else if(metodo.equals("sk")) {
			mostrarKeywordsInput(request, response, out);
		}else if(metodo.equals("sc")) {
			mostrarClientesInput(request, response, out);
		}else if(metodo.equals("st")) {
			mostrarStadisticas(request, response);
		}
			
		
		
		




	}

	private void mostrarStadisticas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		/*int c = Integer.parseInt(request.getParameter("id"));       
		cliente = clientes.get(c);
		Gson gson = new Gson();
		
		for (int i = 0; i < cliente.getRelaciones().size(); i++) {
			ArrayList<Resultado> resultados = cliente.getRelaciones().get(i).getResultados();
			Collections.sort(resultados, Collections.reverseOrder());
		}
		
		String json = gson.toJson(cliente);
		
		System.out.println(json);
		
		response.getWriter().print(json);
		*/
	}

	private void mostrarDatosKeyword(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		/*String ck = request.getParameter("id");
		String[] ids = ck.split("_");
		System.out.println(ck);
		System.out.println("Posicion cliente: "+ids[0]);
		System.out.println("Posicion keyword: "+ids[1]);

		int posicion = Integer.parseInt(ids[0]);
		int keyPosicion = Integer.parseInt(ids[1]);
		cliente = clientes.get(posicion);
		out = response.getWriter();	
		System.out.println(cliente.getRelaciones().get(keyPosicion));

		out.println("<div class=\"infoClient infoAdd\"><div class=\"nameClient nameClientAdd\">"+cliente.getName()+"</div><div class=\"urlClient urlAdd\"><a class=\"urlClient urlAdd link\" href=\""+cliente.getDominio()+"\">"+cliente.getDominio()+"</a></div></div> ");
		out.println("<div class=\"infoClient\"><div class=\"nameClient\">"+cliente.getRelaciones().get(keyPosicion).getKeyword()+"</div><div class=\"urlClient\"></div></div> ");

		out.println("<div class=\"keywordsClient keyCont\">");
		out.println("	<div class=\"contentTable tblKey\"><div class=\"itemTable\"><div class=\"itemKeyword cabeceraTable\">Url</div><div class=\"horDiv\"><div class=\"posiKey itemKeyword cabeceraTable\">Posicion</div><div class=\"dateKey itemKeyword cabeceraTable\">Fecha</div></div></div></div>");
		out.println("	<div id=\"ks\" class=\"contentTable\">");

		ArrayList<Resultado> resultados = cliente.getRelaciones().get(keyPosicion).getResultados();
		Collections.sort(resultados, Collections.reverseOrder());
		for (int i = 0; i < resultados.size(); i++) {
			System.out.println(resultados.get(i));	
			ArrayList<Posicion_Url> resultList = new ArrayList<Posicion_Url>();
			String results[] = resultados.get(i).getJson_resultados().split("],");
			System.out.println("Numero de resultados encontrados: "+results.length);
			for (String r : results) {
				String datos[] = (r.replaceAll("[\\[\\]]", "")).split(",");
				resultList.add(new Posicion_Url(datos[0], Integer.parseInt(datos[1])));
			}

			for (Posicion_Url r : resultList) {
				System.out.println(r.getUrl() +" "+ r.getPosicion());
			}

			//item de cada resultado --------------------------------------------------------------------------------------------------
			out.println("		<div class=\"itemTable\">");
			out.println("			<div class=\"itemKeyword urlKey\"> "+resultList.get(0).getUrl());

			if(resultList.size()>1) {
				out.println("				<p id=\"key_"+i+"_"+resultados.get(i).getId_relacion()+"\" class=\"iconFlecha\" onclick=\"abrirBox(this.id)\"><i class=\"material-icons\">arrow_drop_down</i></p>");
				out.println("				<div id=\"key_"+i+"_"+resultados.get(i).getId_relacion()+"_plus\" class=\"box\">");
				out.println("					<div class=\"itemTable repItem\">");
				for (int j = 1; j < resultList.size(); j++) {
					out.println("						<div class=\"itemKeyword urlKey repUrl\"> "+resultList.get(j).getUrl()+"</div>");
					out.println("						<div class=\"horDiv repDiv\"><div class=\"posiKey itemKeyword cp\">"+resultList.get(j).getPosicion()+"</div><div class=\"dateKey itemKeyword\">"+resultados.get(i).getDate()+"</div></div>");
				}
				out.println("					</div>");
				out.println("				</div>");
			}

			out.println("			</div>");
			out.println("			<div class=\"horDiv\"><div class=\"posiKey itemKeyword\">"+resultados.get(i).getPosicion()+"</div><div class=\"dateKey itemKeyword\">"+resultados.get(i).getDate()+"</div></div>");
			out.println("		</div>");
			//---------------------------------------------------------------------------------------------------------------------------
		}

		out.println("	</div>");
		out.println("</div>");



*/


	}
	private void mostrarResultados(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		int posicion = Integer.parseInt(request.getParameter("posicion"));
		out = response.getWriter();

		cliente = clientes.get(posicion);
		
		out.println("<div class=\"infoClient\">");
		out.println("	<div class=\"nameClient\">"+cliente.getNombre()+"</div>");
		out.println("	<div class=\"urlClient\">"+cliente.getWeb()+"</div>");
		out.println("</div>");
		out.println("<div class=\"keywordsClient\">");
		//out.println("	<div class=\"titleTable\">Keywords<div class=\"horDiv\"></div></div>");
		out.println("	<div id=\"keywords_Client\" class=\"contentTable\">");
		//tabla
		out.println("		<table class=\"table\">");
		out.println("			<thead><tr><th class=\"cabeceraTable\">Link</th><th class=\"cabeceraTable\">Categoria</th><th class=\"cabeceraTable\">Web</th><th class=\"cabeceraTable\">Destino</th><th class=\"cabeceraTable\">Tipo</th></tr></thead>");
		out.println("			<tbody>");
		for (int i = 0; i < cliente.getResultados().size(); i++) {
			out.println("			<tr> <td>"+cliente.getResultados().get(i).getEnlace()+"</td>"
									  + "<td class=\"tdCat\"><div class=\"tdCat\" id=\"dvCat_"+i+"\" onclick=\"selectCategory("+i+")\"><span id=\"spCat_"+i+"\" class=\"tdCat\">Selecciona una categoria</span>"+ "</div>"
									  + "	<ul id=\"selCat_"+i+"\" class=\"slCt effect7\">"
									  + "		<li id=\"1\" onclick=\"liSelectCat(this.id,"+i+")\">Herramientas de análisis</li>"
									  + "		<li id=\"2\" onclick=\"liSelectCat(this.id, "+i+")\">Marcadores</li>"
									  + "		<li id=\"3\" onclick=\"liSelectCat(this.id, "+i+")\">Perfiles</li>"
									  + "		<li id=\"4\" onclick=\"liSelectCat(this.id, "+i+")\">Comentarios en web</li>"
									  + "		<li id=\"5\" onclick=\"liSelectCat(this.id, "+i+")\">Foros</li>"
									  + "		<li id=\"6\" onclick=\"liSelectCat(this.id, "+i+")\">Redes sociales o agregadores</li>"
									  + "		<li id=\"7\" onclick=\"liSelectCat(this.id, "+i+")\">Directorios</li>"
									  + "		<li id=\"8\" onclick=\"liSelectCat(this.id, "+i+")\">Blog gratuitos</li>"
									  + "		<li id=\"9\" onclick=\"liSelectCat(this.id, "+i+")\">Enlaces de interés</li>"
									  + "		<li id=\"10\" onclick=\"liSelectCat(this.id, "+i+")\">Enlaces rotos</li>"
									  + "		<li id=\"11\" onclick=\"liSelectCat(this.id, "+i+")\">Enlaces contextuales</li>"
									  + "		<li id=\"12\" onclick=\"liSelectCat(this.id, "+i+")\">Guestblogging</li>"
									  + "	</ul></td>"
									  
									  
									  
									  
									  + "<td>mary@example.com</td>"
									  + "<td>"+cliente.getResultados().get(i).getFecha()+"</td>"
									  + "<td>"+cliente.getResultados().get(i).getTipo()+"</td> </tr>");						
		}
		out.println("			</tbody>");
		out.println("		</table>");
		out.println("	</div>");
		
		out.println("</div>");
		

		

	}
	private void mostrarKeywordsKeywords(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		/*int posicion = Integer.parseInt(request.getParameter("posicion"));
		out = response.getWriter();


		cliente = clientes.get(posicion);

		out.println("	<div class=\"titleCategory\"><div class=\"titleInner\">Keywords<div class=\"horDiv wa\"><div id=\"addK\" class=\"addK\"><i class=\"material-icons addKi\">add</i></div><div onclick=\"searchKey(event)\"><div id=\"ipkey\" class=\"srchI\"><i class=\"material-icons addKi\">search</i><input id=\"searchK\" class=\"searchI\" type=\"text\" oninput=\"searchK()\"></div></div></div></div></div>");
		out.println("	<div class=\"infoCategory\"><div class=\"info\">"+cliente.getRelaciones().size()+" keywords</div></div>");
		out.println("		<div id=\"lkkI\" class=\"listItems\">");


		for (int i = 0; i < cliente.getRelaciones().size(); i++) {
			String keyword = cliente.getRelaciones().get(i).getKeyword();
			if(i==0) {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id, 'lkr')\" class=\"item\"><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");
			}else {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id, 'lkr')\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");}
		}

		out.println("		</div>");

		System.out.println("Lista cargada");
		*/
	}

	private void mostrarKeywordsInput(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
		/*String k = request.getParameter("keyword");
		int posicion = Integer.parseInt(request.getParameter("posicion"));
		cliente = clientes.get(posicion);
		System.out.println(k);

		//out.println("	<div class=\"titleCategory\"><div class=\"titleInner\">Keywords<div class=\"horDiv\"><div>AGREGAR</div><div><input id=\"searchK\" type=\"text\" value=\""+k+"\" oninput=\"searchK()\" autofocus></div></div></div></div>");
		//out.println("	<div class=\"infoCategory\"><div class=\"info\">"+cliente.getRelaciones().size()+" keywords</div></div>");
		//out.println("		<div id=\"lkk\" class=\"listItems\">");

		int j = 0;
		for (int i = 0; i < cliente.getRelaciones().size(); i++) {
			String keyword = cliente.getRelaciones().get(i).getKeyword();

			if(keyword.contains(k)) {
				if(j==0) {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id, 'lkr')\" class=\"item\"><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");j++;
				}else {out.println("<div id=\""+posicion+"_"+i+"\" onclick=\"selectKeyword(this.id, 'lkr')\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild childKey\"><div class=\"nameItem nameKey\">"+keyword+"</div></div></div>");}
			}

		}

		out.println("<script type=\"text/javascript\">setK();</script>");

		System.out.println("Lista cargada");

*/
	}

	private void mostrarClientesInput(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		/*String k = request.getParameter("keyword");

		System.out.println(k);


		int j = 0;
		for (int i = 0; i < clientes.size(); i++) {
			String keyword = clientes.get(i).getDominio();

			if(keyword.contains(k)) {
				if(j==0) {out.println("<div id=\""+i+"\" onclick=\"selectClient(this.id, 'lkc')\" class=\"item\"><div class=\"itemChild\"><div class=\"nameItem\">"+clientes.get(i).getName()+"</div><div class=\"dominioItem\">"+clientes.get(i).getDominio()+"</div></div></div>");j++;
				}else {out.println(   "<div id=\""+i+"\" onclick=\"selectClient(this.id, 'lkc')\" class=\"item\"><div class=\"line\"></div><div class=\"itemChild\"><div class=\"nameItem\">"+clientes.get(i).getName()+"</div><div class=\"dominioItem\">"+clientes.get(i).getDominio()+"</div></div></div>");}
			}

		}

		out.println("<script type=\"text/javascript\">setC();</script>");

		System.out.println("Lista clientes cargada");
*/
	}


}






































