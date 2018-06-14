package Objects.Gson;
import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Categoria{
	@SerializedName("id_categoria")
	@Expose
	private int idCategoria;
	@SerializedName("enlace")
	@Expose
	private String enlace;

	public Categoria() {
	}

	public Categoria(int idCategoria, String enlace) {
		super();
		this.idCategoria = idCategoria;
		this.enlace = enlace;
	}

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

}
