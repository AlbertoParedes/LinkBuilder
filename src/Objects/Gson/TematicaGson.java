package Objects.Gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TematicaGson {

	@SerializedName("id_tematica")
	@Expose
	private String idTematica;

	@SerializedName("nombre")
	@Expose
	private String nombre;


	public String getIdTematica() {
		return idTematica;
	}

	public void setIdTematica(String idTematica) {
		this.idTematica = idTematica;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}