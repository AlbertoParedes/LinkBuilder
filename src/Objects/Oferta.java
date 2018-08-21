package Objects;

public class Oferta {
	
	private String url;
	private int precio;
	
	
	
	
	public Oferta(String url, int precio) {
		super();
		this.url = url;
		this.precio = precio;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
	
	

}
