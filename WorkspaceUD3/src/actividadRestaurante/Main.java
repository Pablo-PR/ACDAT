package actividadRestaurante;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;
import java.util.Scanner;

public class Main {
    private static final String BD_RESTAURANTE = "bd_txt/restaurantes.oo";
    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Restaurante.class).cascadeOnUpdate(true);
        ObjectContainer db = Db4oEmbedded.openFile(config, BD_RESTAURANTE);
        int opc;

        introducirRestAccdat(db);

        do {
            opc = solicitarOpcion();
            tratarOpcion(opc,db);
        } while (opc != 8);

        db.close();
    }

    private static void tratarOpcion(int opc, ObjectContainer db) {
        switch (opc){
            case 0:
                mostrarBD(db);
                break;
            case 1:
                altaRestaurante(db);
                break;
            case 2:
                registrarProducto(db);
                break;
            case 3:
                asociarEmpleado(db);
                break;
            case 4:
                busquedaProductos(db);
                break;
            case 5:
                busquedaRestaurantes(db);
                break;
            case 6:
                busquedaEmpleadosPorCadena(db);
                break;
            case 7:
                busquedaEmpleadosPorSalario(db);
                break;
        }
    }

    private static void busquedaEmpleadosPorSalario(ObjectContainer db) {
        Restaurante restaurante = controlRestauranteExistente(db);
        double salarioMin = Double.parseDouble(solicitarCadena("Introduce el salario mínimo a filtrar: "));
        double salarioMax = Double.parseDouble(solicitarCadena("Introduce el salario máximo a filtrar: "));

        ObjectSet<Empleado> resultEmpleado = db.query(
                new Predicate<Empleado>() {
                    @Override
                    public boolean match(Empleado e) {
                        return (e.getNomina().getSueldo() >= salarioMin && e.getNomina().getSueldo() <= salarioMax && restaurante.getListEmpleados().contains(e));
                    }
                }
                , (QueryComparator<Empleado>) (e1, e2) -> Double.compare(e2.getNomina().getSueldo(), e1.getNomina().getSueldo())
        );

        listarBusquedaEmpleados(resultEmpleado);
    }

    private static void busquedaEmpleadosPorCadena(ObjectContainer db) {
        String cadena = solicitarCadena("Introduce el nombre o algún apellido del empleado: ");

        ObjectSet<Empleado> result = db.query(
                new Predicate<Empleado>() {
                    @Override
                    public boolean match(Empleado e) {
                        return (e.getNombre().contains(cadena) || e.getApellido1().contains(cadena) || e.getApellido2().contains(cadena));
                    }
                }
                , (QueryComparator<Empleado>) (e1, e2) -> Double.compare(e2.getNomina().getSueldo(), e1.getNomina().getSueldo())
        );

        listarBusquedaEmpleados(result);
    }

    private static void listarBusquedaEmpleados(ObjectSet<Empleado> result) {
        Empleado empleado;

        if (result.size() == 0) {
            System.out.println("No hay empleados coincidentes.");
        }
        else {
            while (result.hasNext()) {
                empleado = result.next();
                System.out.println(empleado);
            }
        }
    }

    private static void busquedaRestaurantes(ObjectContainer db) {
        String nombreRestaurante = solicitarCadena("Introduce el nombre del restaurante buscado: ");

        ObjectSet<Restaurante> result = db.query(
                new Predicate<Restaurante>() {
                    @Override
                    public boolean match(Restaurante r) {
                        return (r.getNombre().contains(nombreRestaurante));
                    }
                }
                , (QueryComparator<Restaurante>) (r1, r2) -> Integer.compare(r2.getListEmpleados().size(), r1.getListEmpleados().size())
        );

        listarBusquedaRestaurantes(result);
    }

    private static void listarBusquedaRestaurantes(ObjectSet<Restaurante> result) {
        Restaurante restaurante;

        if (result.size() == 0) {
            System.out.println("No hay restaurantes con ese nombre.");
        }
        else {
            while (result.hasNext()) {
                restaurante = result.next();
                System.out.println(restaurante);
            }
        }
    }

    private static void busquedaProductos(ObjectContainer db) {
        String consulta;
        Restaurante restaurante = controlRestauranteExistente(db);

        consulta = solicitarCadena("Introduce el nombre del producto buscado: ");

        ObjectSet<Producto> resultProducto = db.query(
                new Predicate<Producto>() {
                    @Override
                    public boolean match(Producto p) {
                        return (p.getNombre().contains(consulta) && restaurante.getListProductos().contains(p));
                    }
                }
                , (QueryComparator<Producto>) (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre())
        );

        listarBusquedaProductos(resultProducto);
    }

    private static void listarBusquedaProductos(ObjectSet<Producto> result) {
        Producto producto;

        if (result.size() == 0) {
            System.out.println("No hay productos con ese nombre en el restaurante.");
        }
        else {
            while (result.hasNext()) {
                producto = result.next();
                System.out.println(producto);
            }
        }
    }

    private static void asociarEmpleado(ObjectContainer db) {
        Restaurante restaurante = controlRestauranteExistente(db);

        restaurante.addEmpleado(crearEmpleado(db));

        db.store(restaurante);
    }

    //Método para controlar que el restaurante insertado exista.
    private static Restaurante controlRestauranteExistente(ObjectContainer db) {
        String nombreRestaurante;
        Restaurante restauranteAux;
        ObjectSet<Restaurante> result;

        do {
            nombreRestaurante = solicitarCadena("Nombre del restaurante: ");

            restauranteAux = new Restaurante(nombreRestaurante);
            result = db.queryByExample(restauranteAux);
        } while (result.size()==0);

        return result.get(0);
    }

    private static void registrarProducto(ObjectContainer db) {
        String opc;
        Restaurante restaurante = controlRestauranteExistente(db);

        do {
            opc = solicitarCadena("¿Desea introducir un producto? (S/N)").toUpperCase();

            if (opc.equals("S")) {
                restaurante.addProducto(crearProducto(db, restaurante.getNombre()));
            }
        } while (opc.equals("S"));

        db.store(restaurante);
    }

    private static Producto crearProducto(ObjectContainer db, String nombreRestaurante) {
        String nombre, nomCategoria;
        double precio;
        Producto producto;
        Categoria[] listCategorias = Categoria.values();
        boolean categoriaCorrecta = false;

        nombre = controlProductoRepetido(db, nombreRestaurante);

        do {
            nomCategoria = solicitarCadena("Categoría a la que pertenece (Bebida, Comida, Postre): ");

            for (Categoria c: listCategorias) {
                if (c.toString().equalsIgnoreCase(nomCategoria)) {
                    categoriaCorrecta = true;
                }
            }
        } while (!categoriaCorrecta);

        precio = Double.parseDouble(solicitarCadena("Precio: "));

        producto = new Producto(nombre, nomCategoria, precio);

        return producto;
    }

    private static String controlProductoRepetido(ObjectContainer db, String nombreRestaurante) {
        String nombre;
        ObjectSet<Restaurante> result = db.queryByExample(new Restaurante(nombreRestaurante));
        Restaurante restaurante = result.get(0);
        boolean productoRepetido = false;

        do {
            nombre = solicitarCadena("Nombre del producto: ");

            for (Producto p: restaurante.getListProductos()) {
                if (p.getNombre().equals(nombre)) {
                    productoRepetido = true;
                }
                else {
                    productoRepetido = false;
                }
            }

        } while (productoRepetido);

        return nombre;
    }

    private static void altaRestaurante(ObjectContainer db) {
        String opcInsertarEmpleado;
        Restaurante restaurante = controlRestauranteRepetido(db);

        //Realizamos un bucle para que se puedan introducir empleados de manera indefinida hasta que se especifique lo contrario.
        do {
            opcInsertarEmpleado = solicitarCadena("¿Desea introducir un empleado? (S/N)").toUpperCase();

            if (opcInsertarEmpleado.equals("S")) {
                restaurante.addEmpleado(crearEmpleado(db));
            }
        } while (opcInsertarEmpleado.equals("S"));

        db.store(restaurante);
    }

    //Método para controlar que el nombre introducido del restaurante no exista ya.
    private static Restaurante controlRestauranteRepetido(ObjectContainer db) {
        Restaurante restauranteAux;
        ObjectSet<Restaurante> result;
        String nombre;

        do {
            nombre = solicitarCadena("Introduce el nombre del restaurante:");
            restauranteAux = new Restaurante(nombre);

            result = db.queryByExample(restauranteAux);

            if (result.size() != 0){
                System.out.println("Ya existe un restaurante con ese nombre.");
            }
        }while (result.size() != 0);

        return new Restaurante(nombre);
    }

    private static Empleado crearEmpleado(ObjectContainer db) {
        String nombreEmpleado, apellido1, apellido2, dni;
        Nomina nomina;
        Empleado empleado;

        //Pedimos los datos del empleado.
        dni = controlDni(db);
        nombreEmpleado = solicitarCadena("Introduce el nombre del empleado: ");
        apellido1 = solicitarCadena("Introduce el primer apellido del empleado: ");
        apellido2 = solicitarCadena("Introduce el segundo apellido del empleado: ");
        nomina = new Nomina(solicitarCadena("Introduce el IBAN de la cuenta: "), Double.parseDouble(solicitarCadena("Introduce el sueldo: ")));

        //Creamos al empleado.
        empleado = new Empleado(nombreEmpleado, apellido1, apellido2, dni, nomina);

        return empleado;
    }

    //Método para controlar que el dni del empleado no exista anteriormente.
    private static String controlDni(ObjectContainer db) {
        String dni;
        ObjectSet<Empleado> result;
        Empleado empleadoAux;

        do {
            dni = solicitarCadena("Introduce el dni del empleado:");
            empleadoAux = new Empleado(null, null, null, dni, null);

            result = db.queryByExample(empleadoAux);

            if (result.size() != 0){
                System.out.println("¡No se admite el pluriempleo!");
            }
        }while (result.size() != 0);

        return dni;
    }

    //Método para realizar apartado A.
    private static void introducirRestAccdat(ObjectContainer db) {
        ObjectSet<Restaurante> result = db.queryByExample(new Restaurante("RestAccdat"));

        if (result.size() == 0) {
            Restaurante restaurante = new Restaurante("RestAccdat");
            restaurante.addEmpleado(new Empleado("Pablo", "Parra", "Rodríguez", "12345678L", new Nomina("12345", 2000)));
            restaurante.addProducto(new Producto("Agua", "Bebida", 1));
            restaurante.addProducto(new Producto("Ensalada", "Comida", 4.5));
            restaurante.addProducto(new Producto("Pastel vegano", "Postre", 2.75));

            db.store(restaurante);
        }
    }

    private static void mostrarBD(ObjectContainer db) {
        Restaurante restauranteAux = new Restaurante(null);
        ObjectSet<Restaurante> result = db.queryByExample(restauranteAux);

        if (result.size() == 0) {
            System.out.println("BD vacía.");
        } else {
            for (Restaurante restaurante: result) {
                System.out.println(restaurante);
            }
        }
    }

    private static String solicitarCadena(String str) {
        System.out.println(str);
        return teclado.nextLine();
    }

    private static int solicitarOpcion() {
        int opc;
        System.out.println("""
                ---------------------------------------------------
                0.Mostrar datos BD
                1.Alta de restaurante
                2.Registrar productos en restaurante
                3.Asociar empleado a restaurante
                4.Búsqueda de productos
                5.Búsqueda de restaurante
                6.Búsqueda de empleado por nombre o algún apellido
                7.Búsqueda de empleados por rango de salario
                8.Salir
                ---------------------------------------------------""");

        do {
            System.out.println("Introduce una opción:");
            opc = Integer.parseInt(teclado.nextLine());
        } while (opc < 0 || opc > 8);
        return opc;
    }
}
