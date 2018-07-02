package Objects.Gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enlace {

	@SerializedName("Disponibilidad")
	@Expose
	private int disponibilidad;

	@SerializedName("id_resultado")
	@Expose
	private String idResultado;

	@SerializedName("id_cliente")
	@Expose
	private String idCliente;

	@SerializedName("id_foro")
	@Expose
	private int idForo;

	@SerializedName("enlace")
	@Expose
	private String enlace;

	@SerializedName("fecha")
	@Expose
	private Date fecha;

	@SerializedName("tipo")
	@Expose
	private String tipo;

	@SerializedName("destino")
	@Expose
	private String destino;

	@SerializedName("categoria")
	@Expose
	private int categoria;

	@SerializedName("estado")
	@Expose
	private String estado;

	@SerializedName("anchor")
	@Expose
	private String anchor;

	@SerializedName("hecho_por")
	@Expose
	private String hechoPor;

	@SerializedName("enlace_de_pago")
	@Expose
	private String enlaceDePago;

	@SerializedName("precio_compra")
	@Expose
	private String precioCompra;

	@SerializedName("precio_venta")
	@Expose
	private String precioVenta;

	@SerializedName("urls_a_atacar")
	@Expose
	private String urlsAAtacar;

	@SerializedName("nombreCategoria")
	@Expose
	private String nombreCategoria;
	
	@SerializedName("web_foro")
	@Expose
	private String webForo;
	
	@SerializedName("UserEditando")
	@Expose
	private String userEditando;
	
	@SerializedName("DescripcionForo")
	@Expose
	private String descripcionForo;
	
	@SerializedName("id_empleado")
	@Expose
	private String idEmpleado;
	
	@SerializedName("name")
	@Expose
	private String nameEmpleado;
	
	

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getDescripcionForo() {
		return descripcionForo;
	}

	public void setDescripcionForo(String descripcionForo) {
		this.descripcionForo = descripcionForo;
	}

	public int getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(int disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public String getIdResultado() {
		return idResultado;
	}

	public void setIdResultado(String idResultado) {
		this.idResultado = idResultado;
	}


	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}


	public int getIdForo() {
		return idForo;
	}

	public void setIdForo(int idForo) {
		this.idForo = idForo;
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

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
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

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getHechoPor() {
		return hechoPor;
	}

	public void setHechoPor(String hechoPor) {
		this.hechoPor = hechoPor;
	}

	public String getEnlaceDePago() {
		return enlaceDePago;
	}

	public void setEnlaceDePago(String enlaceDePago) {
		this.enlaceDePago = enlaceDePago;
	}

	public String getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(String precioCompra) {
		this.precioCompra = precioCompra;
	}

	public String getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(String precioVenta) {
		this.precioVenta = precioVenta;
	}

	public ArrayList<String> getUrlsAAtacar() {
		ArrayList<String> urls_a_atacar = new ArrayList<String>();
		
		if(urlsAAtacar.contains(",")) {
			String[] urls = urlsAAtacar.split(",");
			for (String u : urls) 
				urls_a_atacar.add(u);
		}else {
			urls_a_atacar.add(urlsAAtacar);
		}
		
		return urls_a_atacar;
	}

	public void setUrlsAAtacar(String urlsAAtacar) {
		this.urlsAAtacar = urlsAAtacar;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public String getWebForo() {
		return webForo;
	}

	public void setWebForo(String webForo) {
		this.webForo = webForo;
	}

	public String getUserEditando() {
		return userEditando;
	}

	public void setUserEditando(String userEditando) {
		this.userEditando = userEditando;
	}

	public String getNameEmpleado() {
		return nameEmpleado;
	}

	public void setNameEmpleado(String nameEmpleado) {
		this.nameEmpleado = nameEmpleado;
	}
	
	

}


