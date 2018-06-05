package Objects;

import java.sql.Date;

public class Enlace {

	private String enlace;
	private String numFactura;
	private Date fecha;
	private String medio;
	private int cantidad;
	private double precioUnidad;
	private String iva;
	private double total;
	
	public Enlace() {
		super();
	}
	
	public Enlace(String enlace, String numFactura, Date fecha, String medio, int cantidad, double precioUnidad,
			String iva, double total) {
		super();
		this.enlace = enlace;
		this.numFactura = numFactura;
		this.fecha = fecha;
		this.medio = medio;
		this.cantidad = cantidad;
		this.precioUnidad = precioUnidad;
		this.iva = iva;
		this.total = total;
	}

	public String getEnlace() {
		return enlace;
	}
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	public String getNumFactura() {
		return numFactura;
	}
	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getMedio() {
		return medio;
	}
	public void setMedio(String medio) {
		this.medio = medio;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioUnidad() {
		return precioUnidad;
	}
	public void setPrecioUnidad(double precioUnidad) {
		this.precioUnidad = precioUnidad;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Factura [enlace=" + enlace + ", numFactura=" + numFactura + ", fecha=" + fecha + ", medio=" + medio
				+ ", cantidad=" + cantidad + ", precioUnidad=" + precioUnidad + ", iva=" + iva + ", total=" + total
				+ "]";
	}
	
	
	
	
}
