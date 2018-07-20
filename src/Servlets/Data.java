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
import java.util.HashMap;
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

import Classes.ParsingJson;
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

	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ParsingJson pj = new ParsingJson();
	private Cliente2 cliente = new Cliente2();
	private ArrayList<ForoGson> foros = new ArrayList<ForoGson>();
	public static ArrayList<CategoriaGson> categorias = new ArrayList<CategoriaGson>();
	public static ArrayList<TematicaGson> tematicas = new ArrayList<TematicaGson>();
	public static ArrayList<Empleado> empleados = new ArrayList<Empleado>();
	public static HashMap<String, Empleado> empleadosHashMap;
	//private Empleado empleado = new Empleado();

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
	private void cargarPage(HttpServletRequest request, HttpServletResponse response, int idEmpleado, String name_user, String role) throws ServletException, IOException, ParseException {
		
		//obtenemos todos los datos comunes: categorias, empleados, tematicas
		
		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(idEmpleado+""));
		String json = ws.data(wbList, "getCommonData", "data.php");
		//System.out.println(json);
		String[] jsonArray = json.split(";;");
		//System.out.println("0-Categorias :"+jsonArray[0]);
		//System.out.println("1-Tematicas :"+jsonArray[1]);
		//System.out.println("2-Empleados :"+jsonArray[2]);
		System.out.println("3-Mis datos :"+jsonArray[3]);
		
		this.categorias.clear();
		this.categorias = new Gson().fromJson(jsonArray[0], new TypeToken<List<CategoriaGson>>(){}.getType());
		if(categorias.get(0).getIdCategoria()==0) categorias.remove(0);
		
		this.tematicas.clear();
		this.tematicas = new Gson().fromJson(jsonArray[1], new TypeToken<List<TematicaGson>>(){}.getType());

		this.empleados.clear();
		this.empleados = new Gson().fromJson(jsonArray[2], new TypeToken<List<Empleado>>(){}.getType());
		
		Empleado empleado = new Gson().fromJson(jsonArray[3].substring(1, jsonArray[3].length()-1), Empleado.class);
		
		empleadosHashMap = pj.parsearEmpleadosMap(empleados);
		
		System.out.println("Panel: "+empleado.getPanel());
		if(empleado.getPanel().equals("enlaces")) {
			request.setAttribute("empleado", empleado);
			request.setAttribute("empleados", empleados);
			request.getRequestDispatcher("Data_Enlaces").forward(request, response);
		
		}else if(empleado.getPanel().equals("medios")) {
			//cambiar
			request.setAttribute("empleado", empleado);
			request.setAttribute("empleados", empleados);
			request.getRequestDispatcher("Data_Medios").forward(request, response);
			
		}else if(empleado.getPanel().equals("clientes")) {
			request.setAttribute("empleado", empleado);
			request.getRequestDispatcher("Data_Enlaces").forward(request, response);
		}
		
		//obtenemosForos();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int id_empleado = Integer.parseInt(session.getAttribute("id_user").toString());
		String role = session.getAttribute("role_user").toString();
		
		String metodo = request.getParameter("metodo");

		response.setContentType( "text/html; charset=iso-8859-1" );
		PrintWriter out = response.getWriter();


		if(metodo.equals("guardarPanelSesion")) {
			guardarPanelSesion(request, response, out, id_empleado);
		}
		
		/*
		else if(metodo.equals("guardarForoCompleto")) {
			guardarForoCompleto(request, response);
		}else if(metodo.equals("cats")) {
			mostrarCategorias(request, response, out);
		}else if(metodo.equals("selectCat")) {
			mostrarForos(request, response, out);
		}else if(metodo.equals("guardarForo")) {
			guardarForo(request, response, out);
		}*/
	}

	private void guardarPanelSesion(HttpServletRequest request, HttpServletResponse response, PrintWriter out, int id_empleado) {
		String panel = request.getParameter("panel");
		ArrayList<String> wbList = new ArrayList<>(Arrays.asList(id_empleado+"",panel+""));
		ws.data(wbList, "guardarPanelSesion", "data.php");
	}



	
}