package modelo;

public class Artista {
	// Artibutos
	private String nif; // es la clave primaria
	private String nombre;
	private String apellidos;
	private String nifJefe;

	// Constructores
	//Constructor con la clave primaria
	public Artista(String nif) {
		this.nif = nif;
	}
	
	// Constructor con todos los parametros
	public Artista(String nif, String nombre, String apellidos, String nifJefe) {
		super();
		this.nif = nif;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.nifJefe = nifJefe;
	}

	// Getters and Setters
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNifJefe() {
		return nifJefe;
	}

	public void setNifJefe(String nifJefe) {
		this.nifJefe = nifJefe;
	}

	
}
