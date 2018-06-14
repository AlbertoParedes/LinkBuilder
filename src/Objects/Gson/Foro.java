package Objects.Gson;

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

	public Foro() {
	}

	public Foro(int idForo, String webForo) {
		super();
		this.idForo = idForo;
		this.webForo = webForo;
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



}
