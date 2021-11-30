import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Ejercicio10 {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/PruebaConexionBD", prop);
            Statement s = con.createStatement();

            do{
                tratarMenu(s);
            }while (tratarMenu(s)!=7);


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

    private static int tratarMenu(Statement s) {
        int opcion;

        System.out.println("""
                1. Insertar alumno.
                2. Insertar módulo.
                3. Matricular alumno en módulo.
                4. Listar los módulos de un alumno.
                5. Borrar matrícula.
                6. Listado de alumnos y matrículas.
                7. Salir.
                """);

        do{
            System.out.println("Introduce la opción que quiera ejecutar:");
            opcion = Integer.parseInt(sc.nextLine());
        }while (opcion < 1 || opcion > 7);

        switch (opcion){
            case 1:
                insertarAlumno(s); //Hecho
                break;
            case 2:
                insertarModulo(s); //Hecho
                break;
            case 3:
                matricularAlumnoModulo();
                break;
            case 4:
                listarModulos();
                break;
            case 5:
                borrarMatricula();
                break;
            case 6:
                listarAlumnosMatriculas();
                break;
            default:
                System.out.println("Adios");
        }
    return opcion;
    }

    private static void listarAlumnosMatriculas() {

    }

    private static void borrarMatricula() {

    }

    private static void listarModulos() {

    }

    private static void matricularAlumnoModulo() {

    }

    private static void insertarModulo(Statement s) {
        String codigo, descripcion;

        System.out.println("Introduce el código del módulo:");
        codigo = sc.nextLine();

        System.out.println("Introduce la descripción del módulo:");
        descripcion = sc.nextLine();

        try {
            s.executeUpdate("INSERT INTO PruebaConexionBD.MODULO_PROFESIONAL (CODIGO, DESCRIPCION) VALUES('" + codigo + "', '" + descripcion + "');");
        } catch (SQLException e) {
            System.out.println("Imposible insertar módulo con código -> " + codigo +", registro duplicado.\n");
        }
    }

    private static void insertarAlumno(Statement s) {
        int id, edad, numesc;
        String nombre, apellido1, apellido2, email;

        System.out.println("Introduce el ID:");
        id = Integer.parseInt(sc.nextLine());

        System.out.println("Introduce el nombre:");
        nombre = sc.nextLine();

        System.out.println("Introduce el primer apellido:");
        apellido1 = sc.nextLine();

        System.out.println("Introduce el segundo apellido:");
        apellido2 = sc.nextLine();

        System.out.println("Introduce el email:");
        email = sc.nextLine();

        System.out.println("Introduce la edad: ");
        edad = Integer.parseInt(sc.nextLine());

        numesc = id*2;

        try {
            s.executeUpdate("INSERT INTO ALUMNOS (id, nombre, apellido1, apellido2, email, edad, numesc) VALUES (" + id + ", '" + nombre + "', '" + apellido1 + "', '" + apellido2 + "', '" + email + "', " + edad + ", " + numesc + ");");
        } catch (SQLException e) {
            System.out.println("Imposible insertar alumno con ID -> " + id + ", registro repetido.\n");
        }
    }
}
