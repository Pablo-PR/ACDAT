package actividadRestaurante;

import java.util.ArrayList;

public class Restaurante {
    private String nombre;
    private ArrayList<Empleado> listEmpleados;
    private ArrayList<Producto> listProductos;


    public Restaurante(String nombre) {
        this.nombre = nombre;
    }

    public Restaurante(String nombre, ArrayList<Empleado> listEmpleados) {
        this.nombre = nombre;
        this.listEmpleados = listEmpleados;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setListProductos(ArrayList<Producto> listProductos) {
        this.listProductos = listProductos;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "nombre='" + nombre + '\'' +
                ", listEmpleados=" + listEmpleados +
                ", listProductos=" + listProductos +
                '}';
    }
}
