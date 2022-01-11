import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Ejercicio17 {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionario", prop);
            DatabaseMetaData dbmd = con.getMetaData();

            String nombre = dbmd.getDatabaseProductName();
            String driver = dbmd.getDriverName();
            String url = dbmd.getURL();
            String usuario = dbmd.getUserName();

            System.out.println("INFORMACIÓN SOBRE LA BASE DE DATOS:");
            System.out.println("===================================");
            System.out.printf("Nombre: %s %n", nombre);
            System.out.printf("Driver: %s %n", driver);
            System.out.printf("URL: %s %n", url);
            System.out.printf("Usuario: %s %n", usuario);
        } catch (SQLException e) {
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
