package Objects;

public class Foro {
	
	private int id_foro;
	private String web_foro;
	private String tipo;
	private int DR;
	private int DA;
	private String tematica;
	private String descripcion;
	private int categoria;
	private int req_aprobacion;
	private int req_registro;
	private int aparece_fecha;
	private String reutilizable;
	
	//con esto guardo la posicion de un foro en especifico en un array no global, para saber en que posicion está en el array global de foros
	private int posArrayForos;
	
	public Foro() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Foro(int id_foro, String web_foro, String tipo, int dr, int da, String tematica, String descripcion,
			int categoria, int req_aprobacion, int req_registro, int aparece_fecha, String reutilizable) {
		super();
		this.id_foro = id_foro;
		this.web_foro = web_foro;
		this.tipo = tipo;
		DR = dr;
		DA = da;
		this.tematica = tematica;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.req_aprobacion = req_aprobacion;
		this.req_registro = req_registro;
		this.aparece_fecha = aparece_fecha;
		this.reutilizable = reutilizable;
	}

	public int getId_foro() {
		return id_foro;
	}

	public String getWeb_foro() {
		return web_foro;
	}

	public void setWeb_foro(String web_foro) {
		this.web_foro = web_foro;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getDR() {
		return DR;
	}

	public void setDR(int dr) {
		DR = dr;
	}

	public int getDA() {
		return DA;
	}

	public void setDA(int da) {
		DA = da;
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

	public int getReq_aprobacion() {
		return req_aprobacion;
	}

	public void setReq_aprobacion(int req_aprobacion) {
		this.req_aprobacion = req_aprobacion;
	}

	public int getReq_registro() {
		return req_registro;
	}

	public void setReq_registro(int req_registro) {
		this.req_registro = req_registro;
	}

	public int getAparece_fecha() {
		return aparece_fecha;
	}

	public void setAparece_fecha(int aparece_fecha) {
		this.aparece_fecha = aparece_fecha;
	}

	public String getReutilizable() {
		return reutilizable;
	}

	public void setReutilizable(String reutilizable) {
		this.reutilizable = reutilizable;
	}
	
	

	public int getPosArrayForos() {
		return posArrayForos;
	}

	public void setPosArrayForos(int posArrayForos) {
		this.posArrayForos = posArrayForos;
	}

	@Override
	public String toString() {
		return "Foro [id_foro=" + id_foro + ", web_foro=" + web_foro + ", tipo=" + tipo + ", DR=" + DR + ", DA=" + DA
				+ ", tematica=" + tematica + ", descripcion=" + descripcion + ", categoria=" + categoria
				+ ", req_aprobacion=" + req_aprobacion + ", req_registro=" + req_registro + ", aparece_fecha="
				+ aparece_fecha + ", reutilizable=" + reutilizable + "]";
	}

	
	
}
