package Objects.Gson;

import java.util.Collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Foro {


	@SerializedName("id_foro")
	@Expose
	private int idForo;
	@SerializedName("web_foro")
	@Expose
	private String webForo;
	@SerializedName("categoria")
	@Expose
	private int categoria;
	
	@SerializedName("descripcion")
	@Expose
	private String descripcion;
	
	@SerializedName("type")
	@Expose
	private String type;

	public Foro() {
	}

	

	


	public Foro(int idForo, String webForo, int categoria, String descripcion, String type) {
		super();
		this.idForo = idForo;
		this.webForo = webForo;
		this.categoria = categoria;
		this.descripcion = descripcion;
		this.type = type;
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






	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
	

}
