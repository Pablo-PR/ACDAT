package actividadRestaurante;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String BD_RESTAURANTE = "bd_txt/restaurantes.oo";
    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        int opc;
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BD_RESTAURANTE);

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
        }
    }

    private static void registrarProducto(ObjectContainer db) {
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
        Restaurante restauranteAux;
        ObjectSet<Restaurante> result;

        //Controlamos que el nombre introducido del restaurante no exista ya.
        do {
            nombre = solicitarCadena("Introduce el nombre del restaurante:");
            restauranteAux = new Restaurante(nombre, null);

            result = db.queryByExample(restauranteAux);

            if (result.size() != 0){
                System.out.println("Ya existe un restaurante con ese nombre.");
            }
        }while (result.size() != 0);
        
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

    private static Empleado crearEmpleado(ObjectContainer db) {
        String nombreEmpleado;
        String apellido1;
        String apellido2;
        String dni;
        Nomina nomina;
        Empleado empleado;
        Empleado empleadoAux;
        ObjectSet<Empleado> result;

        //Controlamos que el dni del empleado no exista anteriormente.
        do {
            dni = solicitarCadena("Introduce el dni del empleado:");
            empleadoAux = new Empleado(null, null, null, dni, null);

            result = db.queryByExample(empleadoAux);

            if (result.size() != 0){
                System.out.println("¡No se admite el pluriempleo!");
            }
        }while (result.size() != 0);

        //Pedimos los datos del empleado.
        nombreEmpleado = solicitarCadena("Introduce el nombre del empleado:");
        apellido1 = solicitarCadena("Introduce el primer apellido del empleado:");
        apellido2 = solicitarCadena("Introduce el segundo apellido del empleado:");
        nomina = new Nomina(solicitarCadena("Introduce el IBAN de la cuenta:"), Double.parseDouble(solicitarCadena("Introduce el sueldo:")));
        empleado = new Empleado(nombreEmpleado, apellido1, apellido2, dni, nomina);

        return empleado;
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
