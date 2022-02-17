package ejercicio1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contacto {
	@Id
	private int idContacto;
	
	@Column
	private String nombre, apellido1, apellido2;
	private int numero;
	
	
	public Contacto(int idContacto, String nombre, String apellido1, String apellido2, int numero) {
		super();
		this.idContacto = idContacto;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.numero = numero;
	}


	public int getIdContacto() {
		return idContacto;
	}


	public void setIdContacto(int idContacto) {
		this.idContacto = idContacto;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
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


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}
}
