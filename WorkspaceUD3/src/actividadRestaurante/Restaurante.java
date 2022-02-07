package actividadRestaurante;

import java.util.ArrayList;

public class Restaurante {
    private String nombre;
    private ArrayList<Empleado> listEmpleados;
    private ArrayList<Producto> listProductos;


    public Restaurante(String nombre) {
        this.nombre = nombre;
        this.listEmpleados = new ArrayList<>();
        this.listProductos = new ArrayList<>();
    }

    public Restaurante(String nombre, ArrayList<Empleado> listEmpleados) {
        this.nombre = nombre;
        this.listEmpleados = listEmpleados;
        this.listProductos = new ArrayList<>();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addProducto(Producto producto) {
        listProductos.add(producto);
    }

    public void addEmpleado(Empleado empleado) {
        listEmpleados.add(empleado);
    }

    public ArrayList<Empleado> getListEmpleados() {
        return listEmpleados;
    }

    public ArrayList<Producto> getListProductos() {
        return listProductos;
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
