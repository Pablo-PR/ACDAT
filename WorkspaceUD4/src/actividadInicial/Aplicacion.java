package actividadInicial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Aplicacion {
    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/oficina", prop);
            crearDatos(con);
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

    private static void crearDatos(Connection con) throws SQLException {
        Departamento departamento = new Departamento("Informática");
        departamento.addEmpleado(new Empleado("Perico", "Rodríguez", "Suárez"));
        departamento.addEmpleado(new Empleado("María", "Jiménez", "Páez"));
        departamento.addEmpleado(new Empleado("Jesús", "Ruiz", "Ruiz"));

        Departamento departamento2 = new Departamento("Comercial");
        departamento2.addEmpleado(new Empleado("Gema", "Sánchez", "Rodríguez"));
        departamento2.addEmpleado(new Empleado("Andrea", "Ortiz", "Márquez"));

        insertarDatos(departamento, departamento2, con);
    }

    private static void insertarDatos(Departamento departamento, Departamento departamento2, Connection con) throws SQLException {
        departamento.insertar(departamento.getNombre(), con);
        departamento2.insertar(departamento2.getNombre(), con);

        for (Empleado e: departamento.getListEmpleados()) {
            e.insertar(e.getNombre(), e.getApellido1(), e.getApellido2(), con);
        }

        for (Empleado e: departamento2.getListEmpleados()) {
            e.insertar(e.getNombre(), e.getApellido1(), e.getApellido2(), con);
        }
    }
}
