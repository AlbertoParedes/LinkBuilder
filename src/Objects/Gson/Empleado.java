package Objects.Gson;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Empleado {
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("user")
	@Expose
	private String user;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("role")
	@Expose
	private String role;
	@SerializedName("categoria")
	@Expose
	private String categoria;
	@SerializedName("id_empleado")
	@Expose
	private String idEmpleado;
	@SerializedName("clientes_estado")
	@Expose
	private int clientesEstado;
	@SerializedName("clientes_web")
	@Expose
	private int clientesWeb;
	@SerializedName("clientes_nombre")
	@Expose
	private int clientesNombre;
	
	@SerializedName("clientes_servicio")
	@Expose
	private int clientesServicio;
	@SerializedName("clientes_folows")
	@Expose
	private int clientesFolows;
	@SerializedName("clientes_nofollows")
	@Expose
	private int clientesNofollows;
	@SerializedName("clientes_anchor")
	@Expose
	private int clientesAnchor;
	@SerializedName("clientes_blog")
	@Expose
	private int clientesBlog;
	@SerializedName("clientes_idioma")
	@Expose
	private int clientesIdioma;
	@SerializedName("clientes_empleado")
	@Expose
	private int clientesEmpleado;
	@SerializedName("clientes_destinos")
	@Expose
	private int clientesDestinos;
	@SerializedName("clientes_ver_todos")
	@Expose
	private int clientesVerTodos;

	@SerializedName("clientes_eliminar")
	@Expose
	private int clientesEliminar;
	
	
	private int n_follows;
	
	//sesion
	@SerializedName("panel")
	@Expose
	private String panel;
	
	

	public Empleado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Empleado(int id, String name, int n_follows) {
		super();
		this.id = id;
		this.name = name;
		this.n_follows = n_follows;
	}

	public int getN_follows() {
		return n_follows;
	}

	public void setN_follows(int n_follows) {
		this.n_follows = n_follows;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	
	
	
	
	
	

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	public Map getClientesEstado() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesEstado==1) {
			map.put("onClick", "onclick='guardarEstadoCliente(this)'");
			return map;
		}else if(clientesEstado==0) {
			map.put("onClick", "");
			return map;
		}
		return map;
	}

	public Map getClientesWeb() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesWeb==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarWebCliente(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesWeb==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}


	public Map getClientesNombre() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesNombre==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarNombreCliente(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesNombre==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}


	public Map getClientesServicio() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesServicio==1) {
			map.put("onClick", "onclick='guardarServicio(this)'");
			return map;
		}else if(clientesServicio==0) {
			map.put("onClick", "");
			return map;
		}
		return map;
		
	}


	public Map getClientesFolows() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesFolows==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarFollows(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesFolows==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}

	public Map getClientesNofollows() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesNofollows==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarNoFollows(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesNofollows==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}


	public Map getClientesAnchor() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesAnchor==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarAnchorCliente(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesAnchor==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}


	public Map getClientesBlog() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesBlog==1) {
			map.put("onChange", "onchange='guardarBlog(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesBlog==0) {
			map.put("onChange", "");
			map.put("estado", "disabled");
			return map;
		}
		return map;
	}


	public Map getClientesIdioma() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesIdioma==1) {
			map.put("onInput", "oninput='showGuardar()'");
			map.put("onChange", "onchange='guardarIdioma(this)'");
			map.put("estado", "");
			return map;
		}else if(clientesIdioma==0) {
			map.put("onInput", "");
			map.put("onChange", "");
			map.put("estado", "readonly");
			return map;
		}
		return map;
	}


	public Map getClientesEmpleado() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesEmpleado==1) {
			map.put("onClick", "onclick='guardarEmpleado(this)'");
			return map;
		}else if(clientesEmpleado==0) {
			map.put("onClick", "");
			return map;
		}
		return map;
	}


	public Map getClientesDestinos() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesDestinos==1) {
			map.put("onClickAdd", "onclick='addDestino(this)'");
			map.put("onClickRemove", "onclick='deleteDestino(this)'");
			return map;
		}else if(clientesDestinos==0) {
			map.put("onClickAdd", "");
			map.put("onClickRemove", "");
			return map;
		}
		return map;
	}
	
	


	public Map getClientesEliminar() {
		Map<String, String> map = new HashMap<String, String>();
		if(clientesEliminar==1) {
			map.put("onClick", "onclick='deleteClient(this)'");
			return map;
		}else if(clientesEliminar==0) {
			map.put("onClick", "");
			return map;
		}
		return map;
	}

	public String getClientesVerTodos() {
		return clientesVerTodos+"";
	}

	@Override
	public String toString() {
		return "Empleado [id=" + id + ", name=" + name + ", user=" + user + ", password=" + password + ", role=" + role
				+ ", categoria=" + categoria + ", idEmpleado=" + idEmpleado + ", n_follows=" + n_follows + ", panel="
				+ panel + "]";
	}


	
	
	

}
