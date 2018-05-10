package Objects.Gson;

import java.sql.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultadoGson {


	//Resultado------------------------

	@SerializedName("id_resultado")
	@Expose
	private int idResultado;

	@SerializedName("enlace")
	@Expose
	private String enlace;

	@SerializedName("fecha")
	@Expose
	private Date fecha;
	@SerializedName("tipoRes")
	@Expose
	private String tipoRes;

	@SerializedName("destino")
	@Expose
	private String destino;
	@SerializedName("categoriaResultado")
	@Expose
	private int categoriaResultado;

	@SerializedName("estado")
	@Expose
	private String estado;

	@SerializedName("anchorR")
	@Expose
	private String anchorR;

	
	//Foro---------------------------------------
	
	@SerializedName("id_foro")
	@Expose
	private int idForo;

	@SerializedName("web_foro")
	@Expose
	private String webForo;

	@SerializedName("DR")
	@Expose
	private int DR;

	@SerializedName("DA")
	@Expose
	private int DA;

	@SerializedName("tematica")
	@Expose
	private String tematica;

	@SerializedName("descripcion")
	@Expose
	private String descripcion;

	@SerializedName("req_aprobacion")
	@Expose
	private int reqAprobacion;

	@SerializedName("req_registro")
	@Expose
	private int reqRegistro;

	@SerializedName("aparece_fecha")
	@Expose
	private int apareceFecha;

	@SerializedName("reutilizable")
	@Expose
	private String reutilizable;

	@SerializedName("tipoForo")
	@Expose
	private String tipoForo;

	//Datos del usuario

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

	@SerializedName("anchorCliente")
	@Expose
	private String anchorCliente;

	@SerializedName("blog")
	@Expose
	private int blog;

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

	public int getIdResultado() {
		return idResultado;
	}

	public void setIdResultado(int idResultado) {
		this.idResultado = idResultado;
	}

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipoRes() {
		return tipoRes;
	}

	public void setTipoRes(String tipoRes) {
		this.tipoRes = tipoRes;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public int getCategoriaResultado() {
		return categoriaResultado;
	}

	public void setCategoriaResultado(int categoriaResultado) {
		this.categoriaResultado = categoriaResultado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAnchorR() {
		return anchorR;
	}

	public void setAnchorR(String anchorR) {
		this.anchorR = anchorR;
	}

	public int getIdForo() {
		return idForo;
	}

	public void setIdForo(int idForo) {
		this.idForo = idForo;
	}

	public String getWebForo() {
		return webForo;
	}

	public void setWebForo(String webForo) {
		this.webForo = webForo;
	}

	public int getDR() {
		return DR;
	}

	public void setDR(int dR) {
		DR = dR;
	}

	public int getDA() {
		return DA;
	}

	public void setDA(int dA) {
		DA = dA;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getReqAprobacion() {
		return reqAprobacion;
	}

	public void setReqAprobacion(int reqAprobacion) {
		this.reqAprobacion = reqAprobacion;
	}

	public int getReqRegistro() {
		return reqRegistro;
	}

	public void setReqRegistro(int reqRegistro) {
		this.reqRegistro = reqRegistro;
	}

	public int getApareceFecha() {
		return apareceFecha;
	}

	public void setApareceFecha(int apareceFecha) {
		this.apareceFecha = apareceFecha;
	}

	public String getReutilizable() {
		return reutilizable;
	}

	public void setReutilizable(String reutilizable) {
		this.reutilizable = reutilizable;
	}

	public String getTipoForo() {
		return tipoForo;
	}

	public void setTipoForo(String tipoForo) {
		this.tipoForo = tipoForo;
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

	public String getAnchorCliente() {
		return anchorCliente;
	}

	public void setAnchorCliente(String anchorCliente) {
		this.anchorCliente = anchorCliente;
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