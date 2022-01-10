import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import utilidades.Utilidades;

public class Ejercicio15 {
	private static String sentInsert = 
			" INSERT INTO empleado (NOMEMP, APE1EMP, APE2EMP) " +
			" VALUES(?, ?, ?) ";			
		
	public static void main(String[] args) {
		Connection con = null;
		Properties prop = new Properties();
		prop.put("useSSL", "false");
		prop.put("user", "root");
		prop.put("password", "root");

		try {
			// Se conecta con la base de datos. En este caso es externa y el usuario nos viene dado. Es un usuario únicamente con función de ejecución de funciones
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

			try {
				PreparedStatement psInsert = con.prepareStatement(sentInsert);
				con.setAutoCommit(false);
				
				psInsert.setString(1, "Ángela");
				psInsert.setString(2, "Ruiz");
				psInsert.setString(3, "Peláez");
				psInsert.executeUpdate();
				
				//psInsert.setString(1, "María Eugeniaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				psInsert.setString(1, "María Eugenia");
				psInsert.setString(2, "González");
				psInsert.setString(3, "Martínez");
				psInsert.executeUpdate();
				
				psInsert.setString(1, "Francisco");
				psInsert.setString(2, "Paz");
				psInsert.setString(3, "Peña");
				psInsert.executeUpdate();
				
				con.commit();
				
				System.out.println("Inserción realizada y transacción consolidada (commit realizado).");
				
			}catch (SQLException e) {				
				Utilidades.muestraErrorSQL(e);
				try {
					con.rollback();
					System.out.println("Rollback realizado");
				}catch(SQLException re) {
					Utilidades.muestraErrorSQL(re);
					System.out.println("Error realizando Rollback");
				}
			}			

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