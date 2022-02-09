package actividadInicial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Departamento {
    private int id;
    String nombre;
    List<Empleado> listEmpleados;

    public Departamento(String nombre) {
        this.nombre = nombre;
        listEmpleados = new ArrayList<>();
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

    public List<Empleado> getListEmpleados() {
        return listEmpleados;
    }

    public void addEmpleado(Empleado empleado) {
        listEmpleados.add(empleado);
    }

    public void insertar(String nombre, Connection con) throws SQLException {
        PreparedStatement pst = con.prepareStatement("INSERT INTO departamento (nombreDepartamento) VALUES (?)");

        pst.setString(1, nombre);
        pst.executeBatch();
    }
}
