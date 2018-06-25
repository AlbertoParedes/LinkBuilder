package Classes;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Objects.Gson.Cliente;

public class ParsingJson {

	public ParsingJson() {
		super();
	}
	
	
	//parseamos todos los clientes 
	
	public ArrayList<Cliente> parsearClientes(String json) {
		
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		
		Object obj = JSONValue.parse(json);
		JSONArray array = (JSONArray) obj;
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject c = (JSONObject) array.get(i);
			Cliente cliente = new Cliente(
					toInt(c.get("id_cliente").toString()), 
					c.get("web").toString(), 
					c.get("nombre").toString(),
					c.get("servicio").toString(), 
					toInt(c.get("follows").toString()), 
					toInt(c.get("nofollows").toString()),  
					c.get("anchor").toString(), 
					c.get("blog").toString(), 
					c.get("idioma").toString(), 
					toInt(c.get("follows_done").toString()), 
					toInt(c.get("nofollows_done").toString()),  
					c.get("linkbuilder").toString(), 
					toInt(c.get("Editando").toString()),  
					toInt(c.get("userEditando").toString()),   
					c.get("Eliminado").toString(),  
					c.get("enlaces_de_pago").toString(), 
					c.get("id_empleado").toString(), 
					c.get("urls_a_atacar").toString(), 
					c.get("name_empleado").toString(),
					c.get("tipo_empleado").toString(),
					c.get("status").toString(), 
					c.get("web").toString().replace("https://", "").replace("http://", "").replace("www.", "")
			);
			clientes.add(cliente);
			
		}
		return clientes;
	}
	
	private int toInt(String string) {
		return Integer.parseInt(string); 
	}
	
	

}
