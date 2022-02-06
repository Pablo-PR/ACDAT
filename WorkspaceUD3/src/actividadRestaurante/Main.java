package actividadRestaurante;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String BD_RESTAURANTE = "WorkspaceUD3/bd_txt/restaurantes.oo";
    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        int opc;
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BD_RESTAURANTE);

        //TO DO introducirRestAccdat(db);

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
        }
    }

    private static void asociarEmpleado(ObjectContainer db) {
        String nombreRestaurante;
        Restaurante restauranteAux;
        Restaurante restaurante;
        ObjectSet<Restaurante> result;

        nombreRestaurante = controlRestauranteExistente(db);
        restauranteAux = new Restaurante(nombreRestaurante);
        result = db.queryByExample(nombreRestaurante);
        restaurante = result.get(0);

        restaurante.addEmpleado(crearEmpleado(db));

        db.store(restaurante);
    }

    private static String controlRestauranteExistente(ObjectContainer db) {
        String nombreRestaurante;
        Restaurante restaurante;
        ObjectSet<Restaurante> result;

        do {
            nombreRestaurante = solicitarCadena("Nombre del restaurante al que se va a asociar el empleado: ");

            restaurante = new Restaurante(nombreRestaurante);
            result = db.queryByExample(restaurante);
        } while (result.size()==0);

        return nombreRestaurante;
    }

    private static void registrarProducto(ObjectContainer db) {
        String nombreRestaurante;
        Restaurante restaurante;
        Restaurante restauranteAux;
        ObjectSet<Restaurante> result;
        String opc;

        nombreRestaurante = solicitarCadena("Nombre del restaurante: ");
        restauranteAux = new Restaurante(nombreRestaurante);
        result = db.queryByExample(restauranteAux);

        if (result.size() == 0) {
            System.out.println("No existe ningún restaurante con ese nombre.");
        } else {
            restaurante = result.get(0);

            do {
                opc = solicitarCadena("¿Desea introducir un producto? (S/N)").toUpperCase();

                if (opc.equals("S")) {
                    restaurante.addProducto(crearProducto());
                }
            } while (opc.equals("S"));

            db.store(restaurante);
        }
    }

    private static Producto crearProducto() {
        String nombre;
        String nomCategoria;
        double precio;
        Categoria categoria = null;
        Producto producto;

        nombre = solicitarCadena("Nombre del producto: ");

        do {
            nomCategoria = solicitarCadena("Categoría a la que pertenece (Bebida, Comida, Postre): ").toUpperCase();
        } while (!nomCategoria.equals("BEBIDA") && !nomCategoria.equals("COMIDA") && !nomCategoria.equals("POSTRE"));

        precio = Double.parseDouble(solicitarCadena("Precio: "));

        switch (nomCategoria) {
            case "BEBIDA":
                categoria = Categoria.Bebida;
                break;
            case "COMIDA":
                categoria = Categoria.Comida;
                break;
            case "POSTRE":
                categoria = Categoria.Postre;
                break;
        }

        producto = new Producto(nombre, categoria, precio);

        return producto;
    }

    private static void mostrarBD(ObjectContainer db) {
        Restaurante restauranteAux = new Restaurante(null, null);
        ObjectSet<Restaurante> result = db.queryByExample(restauranteAux);

        if (result.size() == 0) {
            System.out.println("BD vacía.");
        } else {
            for (Restaurante restaurante: result) {
                System.out.println(restaurante);
            }
        }
    }

    private static void altaRestaurante(ObjectContainer db) {
        String opcInsertarEmpleado;
        String nombre;
        ArrayList<Empleado> listEmpleados = new ArrayList<>();
        Restaurante restaurante = null;

        nombre = controlRestauranteRepetido(db);

        //Realizamos un bucle para que se puedan introducir empleados de manera indefinida hasta que se especifique lo contrario.
        do {
            opcInsertarEmpleado = solicitarCadena("¿Desea introducir un empleado? (S/N)").toUpperCase();

            //Controlamos los casos en los que se crea un restaurante con empleados y sin ellos.
            if (opcInsertarEmpleado.equals("S")){
                listEmpleados.add(crearEmpleado(db));
            } else if (opcInsertarEmpleado.equals("N") && listEmpleados.size() == 0){
                restaurante = new Restaurante(nombre);
            } else if (opcInsertarEmpleado.equals("N") && listEmpleados.size() != 0) {
                restaurante = new Restaurante(nombre, listEmpleados);
            }
        } while (opcInsertarEmpleado.equals("S"));
        
        db.store(restaurante);
    }

    //Método para controlar que el nombre introducido del restaurante no exista ya.
    private static String controlRestauranteRepetido(ObjectContainer db) {
        Restaurante restauranteAux;
        ObjectSet<Restaurante> result;
        String nombre;

        do {
            nombre = solicitarCadena("Introduce el nombre del restaurante:");
            restauranteAux = new Restaurante(nombre, null);

            result = db.queryByExample(restauranteAux);

            if (result.size() != 0){
                System.out.println("Ya existe un restaurante con ese nombre.");
            }
        }while (result.size() != 0);

        return nombre;
    }

    private static Empleado crearEmpleado(ObjectContainer db) {
        String nombreEmpleado;
        String apellido1;
        String apellido2;
        String dni;
        Nomina nomina;
        Empleado empleado;

        //Pedimos los datos del empleado.
        dni = controlDni(db);
        nombreEmpleado = solicitarCadena("Introduce el nombre del empleado:");
        apellido1 = solicitarCadena("Introduce el primer apellido del empleado:");
        apellido2 = solicitarCadena("Introduce el segundo apellido del empleado:");
        nomina = new Nomina(solicitarCadena("Introduce el IBAN de la cuenta:"), Double.parseDouble(solicitarCadena("Introduce el sueldo:")));

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

    private static String solicitarCadena(String str) {
        System.out.println(str);
        return teclado.nextLine();
    }

    private static int solicitarOpcion() {
        int opc;
        System.out.println("""
                
                0.Mostrar datos BD
                1.Alta de restaurante
                2.Registrar productos en restaurante
                3.Asociar empleado a restaurante
                4.Búsqueda de productos
                5.Búsqueda de restaurante
                6.Búsqueda de empleado por nombre o algún apellido
                7.Búsqueda de empleados por rango de salario
                8.Salir""");

        do {
            System.out.println("Introduce una opción:");
            opc = Integer.parseInt(teclado.nextLine());
        } while (opc < 0 || opc > 8);
        return opc;
    }
}
