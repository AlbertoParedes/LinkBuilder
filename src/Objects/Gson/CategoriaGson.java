package Objects.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriaGson {


	@SerializedName("id_categoria")
	@Expose
	private int idCategoria;

	@SerializedName("enlace")
	@Expose
	private String enlace;

	@SerializedName("descripcion")
	@Expose
	private String descripcion;

	@SerializedName("tipo")
	@Expose
	private String tipo;

	@SerializedName("recurrente")
	@Expose
	private String recurrente;

	@SerializedName("obtencion")
	@Expose
	private String obtencion;

	@SerializedName("dificultad")
	@Expose
	private String dificultad;
	
	@SerializedName("type")
	@Expose
	private String type;

	

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}


	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRecurrente() {
		return recurrente;
	}

	public void setRecurrente(String recurrente) {
		this.recurrente = recurrente;
	}


	public String getObtencion() {
		return obtencion;
	}

	public void setObtencion(String obtencion) {
		this.obtencion = obtencion;
	}


	public String getDificultad() {
		return dificultad;
	}

	public void setDificultad(String dificultad) {
		this.dificultad = dificultad;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}