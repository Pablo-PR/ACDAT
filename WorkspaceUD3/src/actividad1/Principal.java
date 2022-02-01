package actividad1;

import java.util.Scanner;

import com.db4o.*;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

public class Principal {
	private static final String BD_PERSONAS = "bd_txt/personas.oo";
	private static Scanner teclado = new Scanner(System.in);

	public static void main(String[] args) {
		int opc;
		ObjectContainer db;
		
		db=abrirBd();
		do {
			opc = solicitarOpcion();
			tratarOpcion(opc,db);
		} while (opc != 10);
	
		db.close();		
	}

	/**
	 * Crea una referencia embebida a la BD OO.
	 * @return referencia a la BD
	 */
	private static ObjectContainer abrirBd(){
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BD_PERSONAS);
		return db;
	}
	
	/**
	 * Gestión del menú
	 * @param opc opción seleccionada.
	 * @param db referencia a la BD
	 */
	private static void tratarOpcion(int opc, ObjectContainer db) {
		int edad;
		String dni;
		Cuenta cuenta;

		switch (opc) {
		case 1:
			insertarPersonaEnBd(db);
			break;
		case 2:
			consultarBd(db);
			break;
		case 3:
			edad = solicitarEdad();
			consultarPersonasConEdad(db, edad);
			break;
		case 4:
			dni = solicitarCadena("DNI: ");
			consultarPersonasPorDni(db, dni);
			break;
		case 5:
			cuenta = crearCuenta(db);
			dni = solicitarCadena("DNI: ");
			asociarCuentaPersona(db, cuenta, dni);
			break;
		case 6:
			dni = solicitarCadena("DNI: ");
			consultarDatosPorDni(db, dni);
			break;
		case 7:
			dni = solicitarCadena("DNI: ");
			borrarPersona(db, dni);
			break;
		case 8:
			consultarCuentasExistentes(db);
			break;
		case 9:
			consultarPersonasSaldo(db);
			break;
		}
	}

	private static void consultarPersonasSaldo(ObjectContainer db) {
		double saldo;

		System.out.println("Introduce el saldo para filtrar:");
		saldo = Integer.parseInt(teclado.nextLine());

		ObjectSet<Persona> result = db.query(
			new Predicate<Persona>() {
				@Override
				public boolean match(Persona p) {
					return (p.getMiCuenta().getSaldo() > saldo);
				}
			},
			(QueryComparator<Persona>) (persona1, persona2) -> persona1.compareTo(persona2)
		);

		for (Persona p : result){
			System.out.println(p);
		}
	}

	private static void consultarCuentasExistentes(ObjectContainer db) {
		Cuenta cuenta = new Cuenta();
		ObjectSet<Cuenta> result = db.queryByExample(cuenta);

		if (result.size()>0){
			System.out.println("Número de cuentas " + result.size());
			for (Cuenta cuentaAux: result) {
				System.out.println(cuentaAux);
			}
		}
	}

	private static void borrarPersona(ObjectContainer db, String dni) {
		Persona persona = new Persona(null, 0, dni);
		ObjectSet<Persona> result = db.queryByExample(persona);

		if (result.size()!=0){
			if (result.get(0).getMiCuenta()!=null){
				db.delete(result.get(0));
				db.delete(result.get(0).getMiCuenta());
			}
			else {
				db.delete(result.get(0));
			}

			System.out.println("Datos eliminados correctamente");
		}
		else{
			System.out.println("No existe ninguna persona con DNI: " + dni);
		}
	}

	private static void consultarDatosPorDni(ObjectContainer db, String dni) {
		Persona persona = new Persona(null, 0, dni);
		ObjectSet<Persona> result = db.queryByExample(persona);

		if (result.size()!=0){
			System.out.println(result.get(0));
		}
		else{
			System.out.println("No existe ninguna persona con DNI: " + dni);
		}
	}

	private static Cuenta crearCuenta(ObjectContainer db) {
		Cuenta cuenta;
		Cuenta cuentaAux;
		ObjectSet<Cuenta> result;
		int numCuenta;
		int saldo;

		do {
			System.out.println("Introduce el número de cuenta: ");
			numCuenta = Integer.parseInt(teclado.nextLine());

			cuentaAux = new Cuenta(numCuenta, 0);

			result = db.queryByExample(cuentaAux);
		} while(result.size()!=0);

		System.out.println("Introduce el saldo: ");
		saldo = Integer.parseInt(teclado.nextLine());

		cuenta = new Cuenta(numCuenta, saldo);

		return cuenta;
	}

	private static void asociarCuentaPersona(ObjectContainer db, Cuenta cuenta, String dni) {
		Persona persona = new Persona(null, 0, dni);
		Persona per;

		ObjectSet<Persona> result = db.queryByExample(persona);

		if (result.size() == 0)
			System.out.println("BD Vacia o DNI no encontrado.");
		else {
			per = result.get(0);
			per.setMiCuenta(cuenta);

			db.store(per);

			System.out.println("Cuenta añadida satisfactoriamente\n" +
					per.toString());
		}
	}

	private static void consultarPersonasPorDni(ObjectContainer db, String dni) {
		Persona persona = new Persona(null, 0, dni);
		Persona per;
		int respuesta;
		String nuevoNombre;
		int nuevaEdad;

		ObjectSet<Persona> result = db.queryByExample(persona);

		if (result.size() == 0)
			System.out.println("BD Vacia o DNI no encontrado.");
		else {
			per = result.get(0);
			System.out.println("""
					¿Qué modificación desea hacer?
					1.Nombre
					2.Edad""");

			do {
				respuesta = Integer.parseInt(teclado.nextLine());
			} while (respuesta < 1 || respuesta > 2);

			if (respuesta == 1){
				System.out.println("Introduzca el nuevo nombre: ");
				nuevoNombre = teclado.nextLine();
				per.setNombre(nuevoNombre);
			}
			else {
				System.out.println("Introduzca la edad: ");
				nuevaEdad = Integer.parseInt(teclado.nextLine());
				per.setEdad(nuevaEdad);
			}

			db.store(per);

			System.out.println("Modificación realizda con éxito\n" +
					per.toString());
		}
	}

	/**
	 * Presentación y grabación de la opción seleccionada.
	 * @return
	 */
	private static int solicitarOpcion() {
		int opc;
		System.out.println("\n1.Insertar persona en BD");
		System.out.println("2.Consutar BD completa");
		System.out.println("3.Consultar personas según edad");
		System.out.println("4.Modificar datos de una persona");
		System.out.println("5.Asociar cuenta a una persona");
		System.out.println("6.Consultar datos de una persona por DNI");
		System.out.println("7.Borrar a una persona y su cuenta");
		System.out.println("8.Consultar las cuentas existentes");
		System.out.println("9.Consultar personas según saldo");
		System.out.println("10.Salir");
		
		do {
			System.out.println("Introduce opcion");
			opc = Integer.parseInt(teclado.nextLine());
		} while (opc < 1 || opc > 10);
		return opc;
	}

	/**
	 * Consulta sin filtro, para ello se crea una persona sin modificar sus parámetros.
	 * @param db Referencia a la BD
	 */
	private static void consultarBd(ObjectContainer db) {

		Persona patron = new Persona(); // consultar todas las personas, sin filtro
		ObjectSet<Persona> result = db.queryByExample(patron);

		if (result.size() == 0)
			System.out.println("BD Vacia");
		else {
			System.out.println("Numero de personas " + result.size());
			for (Persona persona: result) {
				
				System.out.println(persona);
			}			
		}	
	}
	
	/**
	 * Consulta conjunto de personas por edad.
	 * @param db Referencia a la BD.
	 * @param edad Edad a consultar. Para ello se crea actividad1.Persona con la edad pasada como parámetro.
	 */
	private static void consultarPersonasConEdad( ObjectContainer db, int edad) {
		Persona patron = new Persona(null, edad, null); // consultar todas las personas que tienen esa edad
		Persona per;
		
		ObjectSet<Persona> result = db.queryByExample(patron);

		if (result.size() == 0)
			System.out.println("BD Vacia");
		else {
			System.out.println("Numero de personas con edad " + edad  + " son: "  + result.size());
			while (result.hasNext()) {
				per = result.next();
				System.out.println( per);
			}
		}		
	}

	/**
	 * Inserta objeto persona en BD con los datos solicitados al usuario.
	 * @param db Referencia a la base de datos.
	 */
	private static void insertarPersonaEnBd(ObjectContainer db) {
		boolean dniRepetido = false;
		int i = 0;
		Persona persona = crearPersona();
		Persona patron = new Persona(null, 0, persona.getDni());

		ObjectSet<Persona> result = db.queryByExample(patron);

		if (result.size()!=0){
			System.out.println("DNI ya introducido.");
		}
		else {
			db.store(persona);
		}
	}

	/**
	 * Método que crea una persona solicitando la información por teclado.
	 * @return Devuelve un instancia del objeto persona con los datos ingresados por el usuario.
	 */
	private static Persona crearPersona() {
		Persona persona = new Persona(solicitarCadena("Nombre: "),solicitarEdad(), solicitarCadena("DNI: "));
		return persona;
	}

	/** 
	 * Método para interactuar con el usuario solicitando la edad.
	 * @return int con la edad. 
	 */
	private static int solicitarEdad() {
		int edad;
		do {
			System.out.println("Introduce edad:");
			edad = Integer.parseInt(teclado.nextLine());
		} while (edad < 0);
		return edad;
	}

	/** 
	 * Método para interactuar con el usuario solicitando el nombre.
	 * @return String con la edad. 
	 */
	private static String solicitarCadena( String msg) {
		String nombre;
		System.out.println(msg);
		nombre = teclado.nextLine();
		return nombre;
	}
}
