package modelo;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnimalDAO {
	// Metodos
	// Crea una conexion con el SGBD y la devuelve
	private static Connection conectar() {
		Connection conexion = null;
		String url = "jdbc:mysql://localhost:3306/circoivan";
		String usuario = "root";
		String pass = "carlos";
		try {
			conexion = DriverManager.getConnection(url, usuario, pass);
		} catch (SQLException ex) {
			System.out.println("Error al conectar al SGBD");
		}
		return conexion;
	}

	// Creamos el metodo create() para poder insertar datos en la base de datos.
	public static void create(Animal animal) {
		// Si el animal pasado es nulo no haremos nada
		if (animal != null) {
			Connection conexion = conectar();

			String sql = "INSERT INTO circo.animales (nombre, tipo, anhos, peso, estatura, nombre_atraccion, nombre_pista) VALUES (?, ?, ?, ?, ?, ?, ?)";
			try {

				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, animal.getNombre());
				sentencia.setString(2, animal.getTipo());
				sentencia.setInt(3, animal.getAnios());
				sentencia.setFloat(4, animal.getPeso());
				sentencia.setFloat(5, animal.getEstatura());
				sentencia.setString(6, animal.getNombreAtraccion());
				sentencia.setString(7, animal.getNombrePista());
				sentencia.executeUpdate();
				conexion.close(); // Cerramos la conexion
			} catch (SQLException ex) {
				System.out.println("Error al insertar.");
			}
		}
	}

	// Lee los datos con la clave primaria construye un objetocon sus datos y lo
	// devuelve.
	public static Animal read(String nombre) {
		Animal animal = null;
		String sql = "SELECT * FROM animales WHERE upper(nombre) = ?";
		try {
			Connection conexion = conectar();
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			sentencia.setString(1, nombre); // Asignamos la clave primaria a buscar
			ResultSet rs = sentencia.executeQuery();
			// Al estar buscando por la clave primaria. solo existen dos alternativas:
			// 1. La encuentra: el resulSet tendrá un unico registro.
			// 2. No la encuentra: el resulSet estará vacio.
			if (rs.next()) { // Si hay un registro
				String tipo = rs.getString("tipo");
				int anios = rs.getInt("anhos");
				float peso = rs.getFloat("peso");
				float estatura = rs.getFloat("estatura");
				String nombreAtraccion = rs.getString("nombre_atraccion");
				String nombrePista = rs.getString("nombre_pista");
				// Creamos un objeto con los datos obtenidos
				animal = new Animal(nombre, tipo, anios, peso, estatura, nombreAtraccion, nombrePista);
				conexion.close(); // cerramos la conexion
			}
		} catch (SQLException ex) {
			System.out.println("Error al consultar un animal.");
		}
		return animal; // Si no encuentra el nombre devolvera null
	}

	// Actualiza los valores del objeto pasado como parametros en la BD.
	// Supondremos que e l objeto ya dispone de su registro.
	// No permitimos que se modifique el identificador de un objeto.
	public static void update(Animal animal) {
		if (animal != null) {
			String sql = "UPDATE circo.animales SET tipo = ?, anhos = ?, peso = ?, estatura = ?, nombre_atraccion = ?, nombre_pista = ? WHERE nombre = ?";
			try {
				Connection conexion = conectar();
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, animal.getTipo());
				sentencia.setInt(2, animal.getAnios());
				sentencia.setFloat(3, animal.getPeso());
				sentencia.setFloat(4, animal.getEstatura());
				sentencia.setString(5, animal.getNombreAtraccion());
				sentencia.setString(6, animal.getNombrePista());
				sentencia.setString(7, animal.getNombre()); // Asignamos la clave a buscar
				sentencia.executeUpdate();
				conexion.close();
			} catch (SQLException ex) {
				System.out.println("Error al actualizar un animal.");
			}
		}
	}

	// Eliminamos el registro correspondiente al registro con la clave.
	public static void delete(String nombre) throws SQLException {
		String sql = "DELETE FROM circo.animales WHERE upper(nombre) = ?";
		try {
			Connection conexion = conectar();
			conexion.setAutoCommit(false); // Desactivo el commit para cada sentencia
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			sentencia.setString(1, nombre); // Asignamos la clave a buscar
			sentencia.executeUpdate();
			conexion.commit();// Al finalizar sentencias hago commit
			conexion.setAutoCommit(true);
		} catch (SQLException ex) {
			System.out.println("Error al eliminar un animal.");
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

	public static ArrayList<Animal> mas20() {

		ArrayList<Animal> pesoMas20 = new ArrayList<>();
		Animal a = null;
		String sql = "SELECT * FROM circo.animales WHERE peso > 20";

		try {
			Connection conexion = conectar();
			PreparedStatement sentencia = conexion.prepareStatement(sql);
			ResultSet rs = null;
			rs = sentencia.executeQuery(sql);

			while (rs.next()) {
				String nombrePK = rs.getString(1);
				String tipo = rs.getString(2);
				int anhos = rs.getInt(3);
				float peso = rs.getFloat(4);
				float estatura = rs.getFloat(5);
				String nombre_atraccion = rs.getString(6);
				String nombre_pista = rs.getString(7);
				a = new Animal(nombrePK, tipo, anhos, peso, estatura, nombre_atraccion, nombre_pista);
				pesoMas20.add(a);
			}
			conexion.close();
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
		return pesoMas20;

	}
}
