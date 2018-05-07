package Objects;

public class Tematica {
	
	int id_tematica;
	String nombre;
	
	public Tematica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tematica(int id_tematica, String nombre) {
		super();
		this.id_tematica = id_tematica;
		this.nombre = nombre;
	}

	public int getId_tematica() {
		return id_tematica;
	}

	public void setId_tematica(int id_tematica) {
		this.id_tematica = id_tematica;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

	
}
