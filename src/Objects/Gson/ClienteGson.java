package Objects.Gson;

import java.util.ArrayList;

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

	@SerializedName("userEditando")
	@Expose
	private int userEditando;
	
	@SerializedName("enlaces_de_pago")
	@Expose
	private int enlacesDePago;
	
	@SerializedName("url_a_atacar")
	@Expose
	private String urlAAtacar;
	
	private ArrayList<String> urlsAAtacar;
	
	

	public ClienteGson(int idCliente, String web, String nombre, String servicio, int follows, int nofollows,
			String anchor, String blog, String idioma, int followsDone, int nofollowsDone, String linkbuilder,
			int editando, int userEditando, int enlacesDePago, ArrayList<String> urlsAAtacar) {
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
		this.enlacesDePago = enlacesDePago;
		this.urlsAAtacar = urlsAAtacar;
	}

	public ArrayList<String> getUrlsAAtacar() {
		return urlsAAtacar;
	}

	public void setUrlsAAtacar(ArrayList<String> urlsAAtacar) {
		this.urlsAAtacar = urlsAAtacar;
	}

	public String getUrlAAtacar() {
		return urlAAtacar;
	}

	public void setUrlAAtacar(String urlAAtacar) {
		this.urlAAtacar = urlAAtacar;
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

	public int getEnlacesDePago() {
		return enlacesDePago;
	}

	public void setEnlacesDePago(int enlacesDePago) {
		this.enlacesDePago = enlacesDePago;
	}
	
	
	
	

}