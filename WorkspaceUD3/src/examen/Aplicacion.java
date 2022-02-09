package examen;

import actividadRestaurante.Empleado;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

import java.util.ArrayList;
import java.util.Scanner;

public class Aplicacion {
	private static final String BD_AGENDA = "accdatgenda.oo";
	private static Scanner teclado = new Scanner(System.in);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int opc;
		ObjectContainer db;
		
		db=abrirBd();

		do {
			muestraMenu();
			opc = solicitarOpcion();
			tratarOpcion(opc,db);
		} while (opc != 6);
		
		System.out.println("Cerrando agenda...");	
	    db.close();		
	}

	private static void tratarOpcion(int opc, ObjectContainer db) {
		switch (opc) {
			case 1:
				altaContacto(db);
				break;
			case 2:
				annadirDatoContacto(db);
				break;
			case 3:
				eliminarDatoContacto(db);
				break;
			case 4:
				buscarCualquierDato(db);
				break;
			case 5:
				calcularContactos(db);
				break;
			case 6:
				contactosRangoEdad(db);
				break;
		}
	}

	private static void contactosRangoEdad(ObjectContainer db) {
		int minEdad = Integer.parseInt(solicitarCadena("Introduce la edad mínima a filtrar: "));
		int maxEdad = Integer.parseInt(solicitarCadena("Introduce la edad máxima a filtrar: "));

		ObjectSet<Contacto> result = db.query(
				new Predicate<Contacto>() {
					@Override
					public boolean match(Contacto c) {
						return (c.getEdad() >= minEdad && c.getEdad() <= maxEdad);
					}
				}
				, (QueryComparator<Contacto>) (c1, c2) -> c1.compareTo(c2)
		);
	}

	private static void calcularContactos(ObjectContainer db) {
		String cadena = controlTipoDato();

		ObjectSet<DatoContacto> result = db.query(
				new Predicate<DatoContacto>() {
					@Override
					public boolean match(DatoContacto dc) {
						return (dc.getTipoDato().contains(cadena));
					}
				}
		);

		listarCalcularContactos(result);
	}

	private static void listarCalcularContactos(ObjectSet<DatoContacto> result) {
		if (result.size() == 0) {
			System.out.println("Tipo de dato no encontrado.");
		}
		else {
			System.out.println("Encontrados " + result.size());
		}
	}

	private static void buscarCualquierDato(ObjectContainer db) {
		String cadena = solicitarCadena("Introduce el nombre, algún apellido del contacto o algún número de teléfono: ");

		ObjectSet<Contacto> result = db.query(
				new Predicate<Contacto>() {
					@Override
					public boolean match(Contacto c) {
						return (c.getNombre().contains(cadena) || c.getApellido1().contains(cadena) || c.getApellido2().contains(cadena));
					}
				}
				, (QueryComparator<Contacto>) (c1, c2) -> Integer.compare(c2.getEdad(), c1.getEdad())
		);

		listarBusquedaContactos(result);
	}

	private static void listarBusquedaContactos(ObjectSet<Contacto> result) {
		Contacto contacto;

		if (result.size() == 0) {
			System.out.println("No hay contactos coincidentes.");
		}
		else {
			while (result.hasNext()) {
				contacto = result.next();
				System.out.println(contacto);
			}
		}
	}

	private static void eliminarDatoContacto(ObjectContainer db) {
		Contacto contacto;
		ObjectSet<Contacto> result = controlContactoNoExistente(db);
		int opcBorrar;

		if (result.size() == 0) {
			System.out.println("No existe ningún contacto con los datos introducidos.");
		}
		else {
			contacto = result.get(0);

			System.out.println(contacto.getDatos());

			do {
				opcBorrar = Integer.parseInt(solicitarCadena("Indique el dato que desea borrar: "));
			}while (opcBorrar > contacto.getDatos().size() || opcBorrar < contacto.getDatos().size());

			contacto.getDatos().remove(opcBorrar-1);
			db.store(contacto);

			System.out.println("Eliminado con éxito.");
		}
	}

	private static void annadirDatoContacto(ObjectContainer db) {
		Contacto contacto;
		ObjectSet<Contacto> result = controlContactoNoExistente(db);

		if (result.size() == 0) {
			System.out.println("No existe ningún contacto con los datos introducidos.");
		}
		else {
			contacto = result.get(0);
			contacto.addDatoContacto(crearDatoContacto(db));
			db.store(contacto);

			System.out.println("Añadido con éxito.");
		}
	}

	//Método que comprueba si ya existe el contacto.
	private static ObjectSet<Contacto> controlContactoNoExistente(ObjectContainer db) {
		String apellido1, apellido2, nombre;
		Contacto contactoAux;
		ObjectSet<Contacto> result;

		nombre = solicitarCadena("Introduce el nombre del contacto: ");
		apellido1 = solicitarCadena("Introduce el primer apellido del contacto: ");
		apellido2 = solicitarCadena("Introduce el segundo apellido del contacto: ");

		contactoAux = new Contacto(apellido1, apellido2, nombre);
		result = db.queryByExample(contactoAux);

		return result;
	}

	private static void altaContacto(ObjectContainer db) {
		Contacto contacto = controlContactoExistente(db);
		String opc;
		ArrayList<DatoContacto> listDatos = new ArrayList<>();
		int edad = Integer.parseInt(solicitarCadena("Introduce la edad del contacto: "));

		contacto.setEdad(edad);

		//Realizamos un bucle para que se puedan introducir datos del contacto de manera indefinida hasta que se especifique lo contrario.
		do {
			opc = solicitarCadena("¿Desea añadir otro número? (S/N)").toUpperCase();

			if (opc.equals("S")) {
				contacto.addDatoContacto(crearDatoContacto(db));
			}
		} while (opc.equals("S"));

		db.store(contacto);
	}

	private static DatoContacto crearDatoContacto(ObjectContainer db) {
		DatoContacto dc;
		String tipoDato;
		int valor;

		tipoDato = controlTipoDato();
		valor = Integer.parseInt(solicitarCadena("Introduce el número: "));

		return dc = new DatoContacto(tipoDato, valor);
	}

	private static String controlTipoDato() {
		String tipoDato;

		do {
			tipoDato = solicitarCadena("Introduce el tipo de número a guardar (F/M/T): ").toUpperCase();
		} while (!tipoDato.equalsIgnoreCase("F") && !tipoDato.equalsIgnoreCase("M") && !tipoDato.equalsIgnoreCase("T"));

		return tipoDato;
	}

	//Método para controlar que el contacto no exista anteriormente.
	private static Contacto controlContactoExistente(ObjectContainer db) {
		String nombre, apellido1, apellido2;
		Contacto contactoAux;
		ObjectSet<Contacto> result;
		Contacto contacto = null;

		do {
			nombre = solicitarCadena("Introduce el nombre del contacto: ");
			apellido1 = solicitarCadena("Introduce el primer apellido del contacto: ");
			apellido2 = solicitarCadena("Introduce el segundo apellido del contacto: ");

			contactoAux = new Contacto(apellido1, apellido2, nombre);
			result = db.queryByExample(contactoAux);

			if (result.size() != 0){
				System.out.println("Ya existe un contacto con ese nombre y apellidos.");
			}
		}while (result.size() != 0);

		contacto = new Contacto(apellido1, apellido2, nombre);

		return contacto;
	}

	public static void muestraMenu() {
		System.out.println("1. Alta de contacto.");
		System.out.println("2. Añadir dato de contacto.");
		System.out.println("3. Eliminar dato de contacto.");
		System.out.println("4. Buscar contacto.");
		System.out.println("5. Calcular contactos.");
		System.out.println("6. Obtener contactos en un rango de edad.");
		System.out.println("7. Salir.");
	}
	
	/**
	 * Presentaci�n y grabaci�n de la opci�n seleccionada.
	 * @return
	 */
	private static int solicitarOpcion() {
		int opc;
		System.out.println("Introduzca la opci�n que desea realizar:");
		opc = Integer.parseInt(teclado.nextLine());
		return opc;
	}

	private static ObjectContainer abrirBd() {

		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();

		config.common().objectClass(Contacto.class).cascadeOnUpdate(true);
		config.common().objectClass(Contacto.class).cascadeOnDelete(false);

		return Db4oEmbedded.openFile(config, BD_AGENDA);
	}

	private static String solicitarCadena(String str) {
		System.out.println(str);
		return teclado.nextLine();
	}

}
