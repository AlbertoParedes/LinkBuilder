package Objects.Gson;

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
	private String editando;
	@SerializedName("userEditando")
	@Expose
	private String userEditando;
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

	public Cliente() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getEditando() {
		return editando;
	}

	public void setEditando(String editando) {
		this.editando = editando;
	}

	public String getUserEditando() {
		return userEditando;
	}

	public void setUserEditando(String userEditando) {
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

}