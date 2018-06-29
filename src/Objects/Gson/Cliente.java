package Objects.Gson;

import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cliente {

	@SerializedName("id_cliente")
	@Expose
	private int idCliente;
	@SerializedName("web")
	@Expose
	private String web;
	@SerializedName("nombre")
	@Expose
	private String nombre;
	@SerializedName("servicio")
	@Expose
	private String servicio;
	@SerializedName("follows")
	@Expose
	private int follows;
	@SerializedName("nofollows")
	@Expose
	private int nofollows;
	@SerializedName("anchor")
	@Expose
	private String anchor;
	@SerializedName("blog")
	@Expose
	private String blog;
	@SerializedName("idioma")
	@Expose
	private String idioma;
	@SerializedName("follows_done")
	@Expose
	private int followsDone;
	@SerializedName("nofollows_done")
	@Expose
	private int nofollowsDone;
	@SerializedName("linkbuilder")
	@Expose
	private String linkbuilder;
	@SerializedName("Editando")
	@Expose
	private int editando;
	@SerializedName("userEditando")
	@Expose
	private int userEditando;
	@SerializedName("Eliminado")
	@Expose
	private String eliminado;
	@SerializedName("enlaces_de_pago")
	@Expose
	private String enlacesDePago;
	@SerializedName("id_empleado")
	@Expose
	private String id_empleado;
	@SerializedName("urls_a_atacar")
	@Expose
	private String urls_a_atacar;
	
	@SerializedName("name_empleado")
	@Expose
	private String name_empleado;
	
	@SerializedName("tipo_empleado")
	@Expose
	private String tipoEmpleado;
	
	@SerializedName("status")
	@Expose
	private String status;
	
	private String dominio;
	
	
	//------------------
	private HashMap<String, Empleado> empleados;
	
	
	
	public Cliente(int idCliente, String web, String nombre, String servicio, int follows, int nofollows, String anchor,
			String blog, String idioma, int followsDone, int nofollowsDone, String linkbuilder, int editando,
			int userEditando, String eliminado, String enlacesDePago, String id_empleado, String urls_a_atacar,
			String name_empleado, String tipoEmpleado, String status, String dominio) {
		super();
		this.idCliente = idCliente;
		this.web = web;
		this.nombre = nombre;
		this.servicio = servicio;
		this.follows = follows;
		this.nofollows = nofollows;
		this.anchor = anchor;
		this.blog = blog;
		this.idioma = idioma;
		this.followsDone = followsDone;
		this.nofollowsDone = nofollowsDone;
		this.linkbuilder = linkbuilder;
		this.editando = editando;
		this.userEditando = userEditando;
		this.eliminado = eliminado;
		this.enlacesDePago = enlacesDePago;
		this.id_empleado = id_empleado;
		this.urls_a_atacar = urls_a_atacar;
		this.name_empleado = name_empleado;
		this.tipoEmpleado = tipoEmpleado;
		this.status = status;
		this.dominio = dominio;
	}
	
	


	public Cliente(int idCliente, String web, String nombre, String servicio, int follows, int nofollows, String anchor,
			String blog, String idioma, int followsDone, int nofollowsDone, String linkbuilder, int editando,
			int userEditando, String eliminado, String enlacesDePago, String urls_a_atacar,String name_empleado, String status,
			String dominio, HashMap<String, Empleado> empleados) {
		super();
		this.idCliente = idCliente;
		this.web = web;
		this.nombre = nombre;
		this.servicio = servicio;
		this.follows = follows;
		this.nofollows = nofollows;
		this.anchor = anchor;
		this.blog = blog;
		this.idioma = idioma;
		this.followsDone = followsDone;
		this.nofollowsDone = nofollowsDone;
		this.linkbuilder = linkbuilder;
		this.editando = editando;
		this.userEditando = userEditando;
		this.eliminado = eliminado;
		this.enlacesDePago = enlacesDePago;
		this.urls_a_atacar = urls_a_atacar;
		this.name_empleado = name_empleado;
		this.status = status;
		this.dominio = dominio;
		this.empleados = empleados;
	}




	public Cliente() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getDominio() {
		return dominio;
	}




	public void setDominio(String dominio) {
		this.dominio = dominio;
	}




	
	
	

	public String getTipoEmpleado() {
		return tipoEmpleado;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}

	public String getName_empleado() {
		return name_empleado;
	}

	public void setName_empleado(String name_empleado) {
		this.name_empleado = name_empleado;
	}

	public String getUrls_a_atacar() {
		return urls_a_atacar;
	}

	public void setUrls_a_atacar(String urls_a_atacar) {
		this.urls_a_atacar = urls_a_atacar;
	}

	public String getId_empleado() {
		return id_empleado;
	}

	public void setId_empleado(String id_empleado) {
		this.id_empleado = id_empleado;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public int getFollows() {
		return follows;
	}

	public void setFollows(int follows) {
		this.follows = follows;
	}

	public int getNofollows() {
		return nofollows;
	}

	public void setNofollows(int nofollows) {
		this.nofollows = nofollows;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public int getFollowsDone() {
		return followsDone;
	}

	public void setFollowsDone(int followsDone) {
		this.followsDone = followsDone;
	}

	public int getNofollowsDone() {
		return nofollowsDone;
	}

	public void setNofollowsDone(int nofollowsDone) {
		this.nofollowsDone = nofollowsDone;
	}

	public String getLinkbuilder() {
		return linkbuilder;
	}

	public void setLinkbuilder(String linkbuilder) {
		this.linkbuilder = linkbuilder;
	}

	public int getEditando() {
		return editando;
	}

	public void setEditando(int editando) {
		this.editando = editando;
	}

	public int getUserEditando() {
		return userEditando;
	}

	public void setUserEditando(int userEditando) {
		this.userEditando = userEditando;
	}

	public String getEliminado() {
		return eliminado;
	}

	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	public String getEnlacesDePago() {
		return enlacesDePago;
	}

	public void setEnlacesDePago(String enlacesDePago) {
		this.enlacesDePago = enlacesDePago;
	}
	
	

	public HashMap<String, Empleado> getEmpleados() {
		return empleados;
	}




	public void setEmpleados(HashMap<String, Empleado> empleados) {
		this.empleados = empleados;
	}




	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", web=" + web + ", nombre=" + nombre + ", servicio=" + servicio
				+ ", follows=" + follows + ", nofollows=" + nofollows + ", anchor=" + anchor + ", blog=" + blog
				+ ", idioma=" + idioma + ", followsDone=" + followsDone + ", nofollowsDone=" + nofollowsDone
				+ ", linkbuilder=" + linkbuilder + ", editando=" + editando + ", userEditando=" + userEditando
				+ ", eliminado=" + eliminado + ", enlacesDePago=" + enlacesDePago + ", id_empleado=" + id_empleado
				+ ", urls_a_atacar=" + urls_a_atacar + ", name_empleado=" + name_empleado + ", tipoEmpleado="
				+ tipoEmpleado + "]";
	}

}