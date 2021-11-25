import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Ejercicio7 {
    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        int id;

        System.out.println("Introduce el ID del alumno: ");
        id = Integer.parseInt(teclado.nextLine());

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM ALUMNOS WHERE ID = " + id);
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()){
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + " ");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
