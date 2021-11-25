import java.sql.*;
import java.util.Properties;

public class Ejercicio1_2 {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

            Statement statement = con.createStatement();

            //Ejercicio 1
            ResultSet rs = statement.executeQuery("show tables");

            while (rs.next()){
                System.out.println(rs.getString(1));
            }

            //Ejercicio 2
            rs = statement.executeQuery("select * from ALUMNOS");

            //Obtenemos los metadatos para poder acceder al número e columnas de cada fila.
            ResultSetMetaData rsmd = rs.getMetaData();

            //Recorremos la sentencia e imprimimos por terminal los datos.
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
