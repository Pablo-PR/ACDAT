import java.sql.*;
import java.util.Properties;

public class Ejercicio5 {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

            Statement s = con.createStatement();
            int update = s.executeUpdate("INSERT INTO ALUMNOS (id, nombre, apellido1, apellido2, email, edad) VALUES (51, 'Mikel', 'Towers', 'Fernández', 'mikel@towers.com', 21)");

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
