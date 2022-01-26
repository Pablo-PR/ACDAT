package actividad1;

public class Persona {
	private String nombre;
	private int edad;
	private String dni;
	private Cuenta miCuenta;

	public Persona(String nombre, int edad, String dni) {
		this.nombre = nombre;
		this.edad = edad;
		this.dni = dni;
	}
//	
	public Persona() {
	}
	@Override
	public String toString() {
		if (miCuenta == null){
			return "[actividad1.Persona --> Nombre: " + nombre + ", Edad: " + edad + ", DNI: " + dni + "]";
		}
		else {
			return "[actividad1.Persona --> Nombre: " + nombre + ", Edad: " + edad + ", DNI: " + dni + ", Cuenta: " + miCuenta.getNumCuenta() + "]";
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
//	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	public Cuenta getMiCuenta() {
		return miCuenta;
	}

	public void setMiCuenta(Cuenta miCuenta) {
		this.miCuenta = miCuenta;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		else if (!dni.equals(other.dni)){
			return false;
		}
		return true;
	}	
}