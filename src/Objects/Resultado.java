package Objects;

import java.sql.Date;

public class Resultado {
	
	private String nombre;
	private int id_resultado;
	private int id_cliente;
	private int id_foro;
	private String enlace;
	private Date fecha;
	private String tipo;
	private String destino;
	private int categoria;
	private String estado;
	private String anchor;
	
	//Nombre del foro al que se hace referencia con su id_foro
	private String web_foro;
	
	public Resultado() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public Resultado(int id_resultado, int id_foro, String enlace, Date fecha, String tipo, String destino,
			int categoria, String estado, String anchor, String web_foro) {
		super();
		this.id_resultado = id_resultado;
		this.id_foro = id_foro;
		this.enlace = enlace;
		this.fecha = fecha;
		this.tipo = tipo;
		this.destino = destino;
		this.categoria = categoria;
		this.estado = estado;
		this.anchor = anchor;
		this.web_foro = web_foro;
	}




	public Resultado(String nombre, int id_resultado, int id_cliente, int id_foro, String enlace, Date fecha,
			String tipo, String destino, int categoria, String estado, String anchor, String web_foro) {
		super();
		this.nombre = nombre;
		this.id_resultado = id_resultado;
		this.id_cliente = id_cliente;
		this.id_foro = id_foro;
		this.enlace = enlace;
		this.fecha = fecha;
		this.tipo = tipo;
		this.destino = destino;
		this.categoria = categoria;
		this.estado = estado;
		this.anchor = anchor;
		this.web_foro = web_foro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setId_resultado(int id_resultado) {
		this.id_resultado = id_resultado;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public int getId_resultado() {
		return id_resultado;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public int getId_foro() {
		return id_foro;
	}

	public void setId_foro(int id_foro) {
		this.id_foro = id_foro;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	@Override
	public String toString() {
		return "Resultado [nombre=" + nombre + ", id_resultado=" + id_resultado + ", id_cliente=" + id_cliente
				+ ", id_foro=" + id_foro + ", enlace=" + enlace + ", fecha=" + fecha + ", tipo=" + tipo + ", destino="
				+ destino + ", categoria=" + categoria + ", estado=" + estado + ", anchor=" + anchor + "]";
	}
	public String getWeb_foro() {
		return web_foro;
	}
	public void setWeb_foro(String web_foro) {
		this.web_foro = web_foro;
	}
	
	
	
	
	
	

}
