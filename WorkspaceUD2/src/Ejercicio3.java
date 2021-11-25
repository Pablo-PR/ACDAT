import java.sql.*;
import java.util.Properties;

public class Ejercicio3 {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from ALUMNOS order by apellido1, apellido2, nombre");
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()){ //rs.next() recorre los registros(filas)
                for (int i = 1; i <= rsmd.getColumnCount(); i++) { //rsmd.getCoulmnCount() devuelve el número de columnas de cada registro.
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println("");
            }
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
