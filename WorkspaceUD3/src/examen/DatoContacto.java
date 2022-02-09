package examen;

public class DatoContacto {
	String tipoDato;
	Integer valor;

	public DatoContacto(String tipoDato, Integer valor) {
		this.tipoDato = tipoDato;
		this.valor = valor;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "DatoContacto{" +
				"tipoDato='" + tipoDato + '\'' +
				", valor=" + valor +
				'}';
	}
}
