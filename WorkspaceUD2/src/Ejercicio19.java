import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import utilidades.Utilidades;

public class Ejercicio19 {
    private static String[][] datosEmpleados = {
            {"Ángela", "Ruiz", "Peláez"},
            {"María Eugenia", "González", "Martínez"},
            {"Francisco", "Paz", "Peña"}
        };

    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            // Se conecta con la base de datos. En este caso es externa y el usuario nos viene dado. Es un usuario únicamente con función de ejecución de funciones
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionario", prop);

            try {
                PreparedStatement psInsert = con.prepareStatement("INSERT INTO empleado (NOMEMP, APE1EMP, APE2EMP) VALUES(?, ?, ?)");
                con.setAutoCommit(false);

                for (int i = 0; i < datosEmpleados.length; i++){
                    for (int x = 0; x < datosEmpleados.length; x++){
                        psInsert.setString(x + 1, datosEmpleados[i][x]);
                    }
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
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