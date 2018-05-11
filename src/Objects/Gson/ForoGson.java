package Objects.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForoGson {

	@SerializedName("id_foro")
	@Expose
	private int idForo;

	@SerializedName("web_foro")
	@Expose
	private String webForo;

	@SerializedName("tipo")
	@Expose
	private String tipo;

	@SerializedName("DR")
	@Expose
	private int dR;

	@SerializedName("DA")
	@Expose
	private int dA;

	@SerializedName("tematica")
	@Expose
	private String tematica;

	@SerializedName("descripcion")
	@Expose
	private String descripcion;

	@SerializedName("categoria")
	@Expose
	private int categoria;

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

	
	
	
	

	public ForoGson(int idForo, String webForo, String tipo, int dR, int dA, String tematica, String descripcion,
			int categoria, int reqAprobacion, int reqRegistro, int apareceFecha, String reutilizable) {
		super();
		this.idForo = idForo;
		this.webForo = webForo;
		this.tipo = tipo;
		this.dR = dR;
		this.dA = dA;
		this.tematica = tematica;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.reqAprobacion = reqAprobacion;
		this.reqRegistro = reqRegistro;
		this.apareceFecha = apareceFecha;
		this.reutilizable = reutilizable;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getDR() {
		return dR;
	}

	public void setDR(int dR) {
		this.dR = dR;
	}

	public int getDA() {
		return dA;
	}

	public void setDA(int dA) {
		this.dA = dA;
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

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
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

}