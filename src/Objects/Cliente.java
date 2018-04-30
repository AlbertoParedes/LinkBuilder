package Objects;

import java.util.ArrayList;

public class Cliente {
	
	private int id_cliente;
	private String web;
	private String nombre;
	private String servicio;
	private int follows;
	private int nofollows;
	private String anchor;
	private int blog;
	private String idioma;
	private int follows_done;
	private int nofollows_done;
	private String linkbuilder;
	
	private ArrayList <Resultado> resultados;
	private ArrayList <Foro> foros;
	
	
	
	public Cliente() {
		super();
	}

	public Cliente(int id_cliente, String web, String nombre, String servicio, int follows, int nofollows,
			String anchor, int blog, String idioma, int follows_done, int nofollows_done, String linkbuilder,
			ArrayList<Resultado> resultados, ArrayList<Foro> foros) {
		super();
		this.id_cliente = id_cliente;
		this.web = web;
		this.nombre = nombre;
		this.servicio = servicio;
		this.follows = follows;
		this.nofollows = nofollows;
		this.anchor = anchor;
		this.blog = blog;
		this.idioma = idioma;
		this.follows_done = follows_done;
		this.nofollows_done = nofollows_done;
		this.linkbuilder = linkbuilder;
		this.resultados = resultados;
		this.foros = foros;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
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

	public int getBlog() {
		return blog;
	}

	public void setBlog(int blog) {
		this.blog = blog;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public int getFollows_done() {
		return follows_done;
	}

	public void setFollows_done(int follows_done) {
		this.follows_done = follows_done;
	}

	public int getNofollows_done() {
		return nofollows_done;
	}

	public void setNofollows_done(int nofollows_done) {
		this.nofollows_done = nofollows_done;
	}

	public String getLinkbuilder() {
		return linkbuilder;
	}

	public void setLinkbuilder(String linkbuilder) {
		this.linkbuilder = linkbuilder;
	}

	public ArrayList<Resultado> getResultados() {
		return resultados;
	}

	public void setResultados(ArrayList<Resultado> resultados) {
		this.resultados = resultados;
	}

	public ArrayList<Foro> getForos() {
		return foros;
	}

	public void setForos(ArrayList<Foro> foros) {
		this.foros = foros;
	}

	



	
	
	
}
