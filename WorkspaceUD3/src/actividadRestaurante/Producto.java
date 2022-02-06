package actividadRestaurante;

public class Producto {
    private String nombre;
    private Categoria categoria;
    private double precio;


    public Producto(String nombre, Categoria categoria, double precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "{nombre='" + nombre + '\'' +
                ", categoria=" + categoria +
                ", precio=" + precio +
                '}';
    }
}
