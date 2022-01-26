package actividad1;

import java.util.Scanner;

import com.db4o.*;

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
		} while (opc != 6);
	
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
			cuenta = crearCuenta();
			dni = solicitarCadena("DNI: ");
			asociarCuentaPersona(db, cuenta, dni);
			break;
		}
	}

	private static Cuenta crearCuenta() {
		Cuenta cuenta;
		int numCuenta;
		int saldo;

		System.out.println("Introduce el número de cuenta: ");
		numCuenta = Integer.parseInt(teclado.nextLine());

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
		System.out.println("1.Insertar persona en BD");
		System.out.println("2.Consutar BD completa");
		System.out.println("3.Consultar personas con una edad");
		System.out.println("4.Modificar datos persona por DNI");
		System.out.println("5.Asociar cuenta a persona por DNI");
		System.out.println("6.Salir");
		
		do {
			System.out.println("Introduce opcion");
			opc = Integer.parseInt(teclado.nextLine());
		} while (opc < 1 || opc > 6);
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
		Persona patron = new Persona();
		ObjectSet<Persona> result = db.queryByExample(patron);

		while (result.hasNext() && !dniRepetido) {
			patron = result.get(i);

			if (persona.equals(patron)){
				dniRepetido = true;
			}

			i++;
		}

		if (dniRepetido) {
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
