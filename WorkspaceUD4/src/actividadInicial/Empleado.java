package actividadInicial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Empleado {
    int id;
    String nombre;
    String apellido1;
    String apellido2;

    public Empleado(String nombre, String apellido1, String apellido2) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void insertar(String nombre, String apellido1, String apellido2, Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement("INSERT INTO empleado (nombreEmpleado, ape1Empleado, ape2Empleado) VALUES (?, ?, ?)");
        con.setAutoCommit(false);

        pst.setString(1, nombre);
        pst.setString(2, apellido1);
        pst.setString(3, apellido2);

        pst.addBatch();
        pst.executeBatch();
        con.commit();
    }
}
