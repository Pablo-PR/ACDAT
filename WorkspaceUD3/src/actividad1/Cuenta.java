package actividad1;

import java.util.Objects;

public class Cuenta {
    private int numCuenta;
    private double saldo;

    public Cuenta(int numCuenta, double saldo) {
        this.numCuenta = numCuenta;
        this.saldo = saldo;
    }

    public Cuenta(){
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return numCuenta == cuenta.numCuenta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCuenta);
    }

    @Override
    public String toString() {
        return "{numCuenta=" + numCuenta +
                ", saldo=" + saldo +
                '}';
    }
}
