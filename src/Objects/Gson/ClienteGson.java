package Objects.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClienteGson {


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

}