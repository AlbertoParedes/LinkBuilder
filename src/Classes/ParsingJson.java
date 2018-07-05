package Classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import Objects.Gson.Cliente;
import Objects.Gson.Empleado;

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

	public ArrayList<Cliente> parsearClientesMap(String json) {

		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		Object obj = JSONValue.parse(json);
		JSONArray array = (JSONArray) obj;



		for (int i = 0; i < array.size(); i++) {
			JSONObject c = (JSONObject) array.get(i);


			HashMap<String, Empleado> empleados = new HashMap<String,Empleado>();
			//obtenemos todos los empleados que etsan utilizando este client
			String nombres ="";
			try {


				String es = c.get("ids_empleados").toString().replaceAll(";,", ";");
				String[] arrayEmpleados = es.split(";");

				for (String e : arrayEmpleados) {
					String[] datosAux = e.split(",");
					Empleado empleado = new Empleado(Integer.parseInt(datosAux[0]), datosAux[1], toInt(datosAux[2]), toInt(datosAux[3]));			
					empleados.put(datosAux[0], empleado);
					nombres += empleado.getName()+",";
				}
				nombres+=";";nombres = nombres.replace(",;", "");

			} catch (Exception e) {
				// TODO: handle exception
			}
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
					c.get("urls_a_atacar").toString(),
					nombres,
					c.get("status").toString(), 
					c.get("web").toString().replace("https://", "").replace("http://", "").replace("www.", ""),
					empleados
					);
			clientes.add(cliente);

		}
		return clientes;
	}


	public HashMap<String, Empleado> parsearEmpleadosMap(ArrayList<Empleado> empleados){
		
		HashMap<String, Empleado> es = new HashMap<String,Empleado>();
		
		for (Empleado empleado : empleados) {
			es.put(empleado.getId()+"", empleado);
		}
		
		return es;

	}
	private int toInt(String string) {
		return Integer.parseInt(string); 
	}



}
