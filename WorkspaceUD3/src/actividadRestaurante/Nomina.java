package actividadRestaurante;

public class Nomina {
    private String iban;
    private double sueldo;


    public Nomina(String iban, double sueldo) {
        this.iban = iban;
        this.sueldo = sueldo;
    }


    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public String toString() {
        return "Nomina{" +
                "sueldo=" + sueldo +
                '}';
    }
}