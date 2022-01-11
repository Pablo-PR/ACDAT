/**
 * 
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author JESUS
 *
 */
public class Ejercicio18 {
	public static void main(String[] args) {
		Connection con = null;
		Properties prop = new Properties();
		prop.put("useSSL", "false");
		prop.put("user", "root");
		prop.put("password", "root");

		try {
			// Se conecta con la base de datos. En este caso es externa y el usuario nos viene dado. Es un usuario únicamente con función de ejecución de funciones
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionario", prop);
			DatabaseMetaData dbmd = con.getMetaData();
			boolean admiteProcesamientoLotes = dbmd.supportsBatchUpdates();
			
			System.out.println("La BD " + (admiteProcesamientoLotes?"sí":"no") + " admite procesamiento por lotes.");
						

		} catch (Exception e) {
			System.err.println("Error de conexión.");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}