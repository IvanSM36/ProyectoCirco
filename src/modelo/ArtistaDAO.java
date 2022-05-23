package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ArtistaDAO {

	// Metodos
	// Crea una conexion con el SGBD y la devuelve
	private static Connection conectar() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/circo";
		String usuario = "root";
		String pass = "admin";
		try {
			con = DriverManager.getConnection(url, usuario, pass);
		} catch (SQLException ex) {
			System.out.println("Error al conectar al SGBD");
		}
		return con;
	}

	// Creamos el metodo create() para poder insertar datos en la base de datos.
	public static void create(Artista artista) {
		// Si el animal pasado es nulo no haremos nada
		if (artista != null) {
			Connection conexion = conectar();
			String sql = "INSERT INTO circo.artistas (nif, apellidos, nombre, nif_jefe)"
					+ "			   VALUES ( ?,      ?,        ?,      ?    )";
			try {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, artista.getNif());
				sentencia.setString(2, artista.getApellidos());
				sentencia.setString(3, artista.getNombre());
				sentencia.setString(4, artista.getNifJefe());
				sentencia.executeUpdate();
				conexion.close(); // Cerramos la conexion
			} catch (SQLException ex) {
				System.out.println("Error al insertar.");
			}
		}
	}

	// Lee los datos con la clave primaria construye un objetocon sus datos y lo
	// devuelve.
	public static Artista read(String nif) {
		Artista artista = null;
		String sql = "SELECT * FROM artistas WHERE nif = ?";
		try {
			Connection conexion = conectar();
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			sentencia.setString(1, nif); // Asignamos la clave primaria a buscar
			ResultSet rs = sentencia.executeQuery();
			// Al estar buscando por la clave primaria. solo existen dos alternativas:
			// 1. La encuentra: el resulSet tendrá un unico registro.
			// 2. No la encuentra: el resulSet estará vacio.
			if (rs.next()) { // Si hay un registro
				String apellidos = rs.getString("apellidos");
				String nombre = rs.getString("nombre");
				String nifJefe = rs.getString("nif_jefe");
				// Creamos un objeto con los datos obtenidos
				artista = new Artista(nif, apellidos, nombre, nifJefe);
				conexion.close(); // cerramos la conexion
			}
		} catch (SQLException ex) {
			System.out.println("Error al consultar un animal.");
		}
		return artista; // Si no encuentra el nombre devolvera null
	}

	// Actualiza los valores del objeto pasado como parametros en la BD.
	// Supondremos que e l objeto ya dispone de su registro.
	// No permitimos que se modifique el identificador de un objeto.
	public static void update(Artista artista) {
		if (artista != null) {
			String sql = "UPDATE circo.artistas SET apellidos = ?, nombre = ?, nif_jefe = ? WHERE nif = ?";
			try {
				Connection conexion = conectar();
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, artista.getApellidos());
				sentencia.setString(2, artista.getNombre());
				sentencia.setString(3, artista.getNifJefe());
				sentencia.setString(4, artista.getNif()); // Asignamos la clave3 a buscar
				sentencia.executeUpdate();
				conexion.close();
			} catch (SQLException ex) {
				System.out.println("Error al actualizar el artista.");
			}
		}
	}

	// Eliminamos el registro correspondiente al registro con la clave.
	public static void delete(String nif) throws SQLException {
		String sql = "DELETE FROM circo.artistas WHERE nif = ?";
		try {
			Connection conexion = conectar();
			conexion.setAutoCommit(false); // Desactivo el commit para cada sentencia
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			sentencia.setString(1, nif); // Asignamos la clave a buscar
			sentencia.executeUpdate();
			conexion.commit();// Al finalizar sentencias hago commit
			conexion.setAutoCommit(true);
		} catch (SQLException ex) {
			System.out.println("Error al eliminar el artista.");
			conectar().rollback(); // Si algo falla hago rollback para dejarlo como antes
		} finally {
			if (conectar() != null) {
				try {
					conectar().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ArrayList<Artista> nombreConA() {

		ArrayList<Artista> nombreConA = new ArrayList<>();
		Artista a = null;
		String sql = "SELECT * FROM Artistas WHERE ucase(nombre) like '%a%'";

		try {
			Connection conexion = conectar();
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			ResultSet rs = null;
			rs = sentencia.executeQuery(sql);

			while (rs.next()) {
				String nifPK = rs.getString(1);
				String apellidos = rs.getString(2);
				String nombre = rs.getString(3);
				String nif_jefe = rs.getString(4);
				a = new Artista(nifPK, apellidos, nombre, nif_jefe);
				nombreConA.add(a);
			}
			conexion.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
		return nombreConA;
	}

}
