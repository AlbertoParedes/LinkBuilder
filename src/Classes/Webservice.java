package Classes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Webservice {
		
	public Webservice() {
		super();
	}

	private String USER_AGENT = "Mozilla/5.0";
	private String SERVER_PATH = "http://pg.yoseomk.vps-100.netricahosting.com/Linkbuilder/";
	
	public String getJSON(String ficheroPHP){

		StringBuffer response = null;

		try {
			String url = SERVER_PATH + ficheroPHP;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setDoOutput(true);      
			int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response.toString();
	}

	
	
	public String getUser(String user, String fichero) {
		StringBuffer response = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("user", user);
		List<JSONObject>  l = new LinkedList<JSONObject>();
		l.addAll(Arrays.asList(jsonObj));

		String jsonString = JSONValue.toJSONString(l);
	
		sendPost(fichero, response, jsonString);

		return response.toString();
	}
	
	public String getResultVacios(int id,String fecha, String fichero) {
		StringBuffer response = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", id);
		jsonObj.put("fecha", fecha);
		List<JSONObject>  l = new LinkedList<JSONObject>();
		l.addAll(Arrays.asList(jsonObj));

		String jsonString = JSONValue.toJSONString(l);
	
		sendPost(fichero, response, jsonString);

		return response.toString();
	}
	
	public String insertResultadoVacio(int id,String tipo, String fichero) {
		StringBuffer response = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", id);
		jsonObj.put("tipo", tipo);
		List<JSONObject>  l = new LinkedList<JSONObject>();
		l.addAll(Arrays.asList(jsonObj));

		String jsonString = JSONValue.toJSONString(l);
	
		sendPost(fichero, response, jsonString);

		return response.toString();
	}
	
	public String updateResultado(String id_resultado,String campo,String valor, String fichero) {
		StringBuffer response = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id_resultado", id_resultado);
		jsonObj.put("campo", campo);
		jsonObj.put("valor", valor);
		List<JSONObject>  l = new LinkedList<JSONObject>();
		l.addAll(Arrays.asList(jsonObj));

		String jsonString = JSONValue.toJSONString(l);
	
		sendPost(fichero, response, jsonString);

		return response.toString();
	}
	

	private void sendPost(String fichero, StringBuffer response, String jsonString) {
		
		try {
			jsonString = URLEncoder.encode(jsonString, "UTF-8");
			
			String url = SERVER_PATH+fichero;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String urlParameters = "json="+jsonString;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			
			//int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Post parameters : " + urlParameters);
			//System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			//System.out.println("respuesta: "+response.toString());

			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//update `Keywords` set `keyword` = convert(binary convert(`keyword` using latin1) using utf8);
