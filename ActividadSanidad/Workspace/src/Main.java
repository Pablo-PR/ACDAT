import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import utilidades.Utilidades;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static String insertPacientes = "INSERT INTO asegurado (nombre, edad) VALUES (?, ?)";
    private static String insertVisitas = "INSERT INTO visitaCentro (codigoCentro, idAsegurado) VALUES (?, ?)";
    private static String mostrarPacientes = "SELECT DISTINCT a.nombre FROM asegurado a INNER JOIN visitaCentro vc ON a.idAsegurado = vc.idAsegurado ORDER BY a.nombre";
    private static String crearTablaCentro = "CREATE TABLE centro (" +
            "idCentro int(25) NOT NULL," +
            "nombre varchar(25)," +
            "PRIMARY KEY (idCentro))";
    private static String insertDatosCentro = "INSERT INTO centro (idCentro, nombre) VALUES (?, ?)";

    public static void main(String[] args) {
        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanidad", prop);
            DatabaseMetaData dbmd = con.getMetaData();

            cargaDatos(con, dbmd);
            listarPacientes(con);
            //creaTabla(con);
            realizaMigracion(con, prop);
        } catch (Exception e) {
            System.err.println("Error de conexión.");
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

    private static void realizaMigracion(Connection con, Properties prop) throws SQLException {
        int id;
        String nombre;
        Connection conSanidadExterna = null;
        conSanidadExterna = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanidad_externa", prop);

        PreparedStatement cargaDatosSanidadExterna = conSanidadExterna.prepareStatement("SELECT * FROM centro_externo");
        PreparedStatement insertarDatosSanidad = con.prepareStatement(insertDatosCentro);

        ResultSet datosSanidadExterna = cargaDatosSanidadExterna.executeQuery();

        con.setAutoCommit(false);

        while (datosSanidadExterna.next()){
            id = datosSanidadExterna.getInt(1);
            nombre = datosSanidadExterna.getString(2);

            insertarDatosSanidad.setInt(1, id);
            insertarDatosSanidad.setString(2, nombre);

            insertarDatosSanidad.addBatch();
        }

        insertarDatosSanidad.executeBatch();
        con.commit();
    }

    private static void creaTabla(Connection con) throws SQLException {
        PreparedStatement crearTabla = con.prepareStatement(crearTablaCentro);

        crearTabla.executeUpdate();
    }

    private static void listarPacientes(Connection con) throws SQLException {
        PreparedStatement listarPacientes = con.prepareStatement(mostrarPacientes);
        ResultSet rs = listarPacientes.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        System.out.println("Lista de pacientes que han visitado un centro");
        while (rs.next()){
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.println(rs.getString(i));
            }
        }
    }

    private static void cargaDatos(Connection con, DatabaseMetaData dbmd) {
        int idGenerado = 0;
        String nombrePaciente;
        String edad;
        String codigoCentro;
        String visita;
        String otroPaciente;

        try {
            PreparedStatement psPacientes = con.prepareStatement(insertPacientes, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psVisitas = con.prepareStatement(insertVisitas);

            con.setAutoCommit(false);

            do{
                //Preguntamos datos para insertar paciente.
                System.out.println("Inserte el nombre del paciente:");
                nombrePaciente = sc.nextLine();

                System.out.println("Inserte la edad del paciente:");
                edad = sc.nextLine();

                psPacientes.setString(1, nombrePaciente);
                psPacientes.setString(2, edad);
                psPacientes.executeUpdate();

                System.out.println("¿Desea registrar visitas para este paciente? (S/N)");
                visita = sc.nextLine();

                if (visita.equals("S")){
                    //Obtenemos su ID para poder almacenar los datos en la tabla visitas.
                    ResultSet rs = psPacientes.getGeneratedKeys();
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }

                    do{
                        //Almacenamos datos en tabla visita usando el ID obtenido.
                        System.out.println("Introduce el código del centro:");
                        codigoCentro = sc.nextLine();

                        psVisitas.setString(1, codigoCentro);
                        psVisitas.setString(2, "" + idGenerado);
                        psVisitas.executeUpdate();

                        System.out.println("¿Desea registrar otra visita para este paciente? (S/N)");
                        visita = sc.nextLine();
                    }while (visita.equals("S"));

                    con.commit();
                    System.out.println("Inserción realizada y transacción consolidada con éxito.");
                }
                else{
                    con.commit();
                    System.out.println("Inserción realizada y transacción consolidada con éxito.");
                }

                System.out.println("¿Desea registrar otro paciente? (S/N)");
                otroPaciente = sc.nextLine();
            }while (otroPaciente.equals("S"));

        }catch (SQLException e) {
            Utilidades.muestraErrorSQL(e);
            try {
                con.rollback();
                System.out.println("Rollback realizado");
            }catch(SQLException re) {
                Utilidades.muestraErrorSQL(re);
                System.out.println("Error realizando Rollback");
            }
        }
    }
}