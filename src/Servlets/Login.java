package Servlets;

import java.io.IOException;
import java.util.ArrayList;
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

import Classes.Webservice;
import Objects.Gson.ClienteGson;
import Objects.Gson.Empleado;



@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("Login.jsp").forward(request, response);
		//request.getRequestDispatcher("Data").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		
		System.out.println(user);
		System.out.println(password);
		
		Webservice ws = new Webservice();
		String json = ws.getUser(user, "getUser.php");
		Empleado empleado = new Gson().fromJson(json.substring(1, json.length()-1), Empleado.class);
		
		
		
		if(empleado.getUser().equalsIgnoreCase(user)) {
			if(empleado.getPassword().equalsIgnoreCase(password)) {
				HttpSession session = request.getSession();
				session.setAttribute("id_user", empleado.getId());
				session.setAttribute("name_user", empleado.getName());
				session.setAttribute("role_user", empleado.getRole());
				session.setAttribute("categoria_user", empleado.getCategoria());
				response.sendRedirect("Data");
			}else {
				System.out.println("La contraseña no es esa");
			}
		}else {
			System.out.println("No existe ese usuario");
		}
		
		
		
	}

}
