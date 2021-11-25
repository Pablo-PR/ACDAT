import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Ejercicio6 {
    private static Scanner teclado = new Scanner((System.in));

    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        int id, edad;
        String nombre, apellido1, apellido2, email;

        System.out.println("Introduce el ID: ");
        id = Integer.parseInt(teclado.nextLine());

        System.out.println("Introduce el nombre: ");
        nombre = teclado.nextLine();

        System.out.println("Introduce el primer apellido: ");
        apellido1 = teclado.nextLine();

        System.out.println("Introduce el segundo apellido: ");
        apellido2 = teclado.nextLine();

        System.out.println("Introduce el email: ");
        email = teclado.nextLine();

        System.out.println("Introduce la edad: ");
        edad = Integer.parseInt(teclado.nextLine());

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ACDAT", prop);

            Statement s = con.createStatement();
            int update = s.executeUpdate("INSERT INTO ALUMNOS (id, nombre, apellido1, apellido2, email, edad) VALUES (" + id + ", '" + nombre + "', '" + apellido1 + "', '" + apellido2 + "', '" + email + "', " + edad + ")");

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
