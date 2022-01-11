import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

//import utilidades.Utilidades;

public class Main {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanidad", prop);
            cargaDatos();
            try {
                PreparedStatement psInsert = con.prepareStatement("INSERT INTO empleado (NOMEMP, APE1EMP, APE2EMP) VALUES(?, ?, ?)");
                con.setAutoCommit(false);

//                for (int i = 0; i < datosEmpleados.length; i++){
//                    for (int x = 0; x < datosEmpleados.length; x++){
//                        psInsert.setString(x + 1, datosEmpleados[i][x]);
//                    }
//                    psInsert.addBatch();
//                }
                psInsert.executeBatch();
                con.commit();

                System.out.println("Inserción realizada y transacción consolidada (commit realizado).");

            }catch (SQLException e) {
                //Utilidades.muestraErrorSQL(e);
                try {
                    con.rollback();
                    System.out.println("Rollback realizado");
                }catch(SQLException re) {
                    //Utilidades.muestraErrorSQL(re);
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

    private static void cargaDatos() {
        String[][] datosPacientes = {
                {"ANTONIO PEREZ", "32"},
                {"ROSA ALVAREZ", "25"},
                {"JUAN GONZALEZ", "26"},
                {"MARIA JIMENEZ", "30"}
        };
        //HACER INSERTS NORMALES
    }
}