package Servlets;

import java.io.IOException;
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
		
		Object jsonObject =JSONValue.parse(json.toString());
		JSONArray arrayData = (JSONArray)jsonObject;
		System.out.println(json);
		
		
		int id_user =-1;
		String userName="null";
		String userPassword="null";
		
		
		for(int i=0;i<arrayData.size();i++){
			JSONObject row =(JSONObject)arrayData.get(i);
			id_user = Integer.parseInt(row.get("id").toString());
			userName = row.get("user").toString();
			userPassword = row.get("password").toString();
			
		}
		
		if(userName.equalsIgnoreCase(user)) {
			if(userPassword.equalsIgnoreCase(password)) {
				HttpSession session = request.getSession();
				session.setAttribute("id_user", id_user);
				session.setAttribute("name_user", userName);
				response.sendRedirect("Data");
			}else {
				System.out.println("La contraseña no es esa");
			}
		}else {
			System.out.println("No existe ese usuario");
		}
		
		
		
	}

}
