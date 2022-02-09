package examen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contacto implements Comparable<Contacto>{
	String apellido1, apellido2, nombre;
	List<DatoContacto> datos;
	int edad;

	public Contacto(String apellido1, String apellido2, String nombre) {
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.nombre = nombre;
		this.datos = new ArrayList<>();
	}

	public Contacto(String apellido1, String apellido2, String nombre, List<DatoContacto> datos) {
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.nombre = nombre;
		this.datos = datos;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<DatoContacto> getDatos() {
		return datos;
	}

	public void setDatos(List<DatoContacto> datos) {
		this.datos = datos;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public void addDatoContacto(DatoContacto dc) {
		datos.add(dc);
	}

	@Override
	public int compareTo(Contacto o) {
		if (this.getNombre().compareTo(o.getNombre()) == 0)
			return 0;
		if (this.getNombre().compareTo(o.getNombre()) < 0)
			return -1;
		else
			return 1;
	}

	@Override
	public String toString() {
		return "Contacto{" +
				"apellido1='" + apellido1 + '\'' +
				", apellido2='" + apellido2 + '\'' +
				", nombre='" + nombre + '\'' +
				", datos=" + datos +
				", edad=" + edad +
				'}';
	}
}
