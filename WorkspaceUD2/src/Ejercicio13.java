import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;
import java.util.Scanner;

public class Ejercicio13 {
    // llamada a la función de base de datos.
    public static String llamada_funcion = "{ ? = call pasar_a_mayusculas(?)}";
    private static Scanner teclado = new Scanner(System.in);

	/*
	CREATE CustomerLevel(credit DECIMAL(10,2)) RETURNS varchar(20)
	BEGIN
	    DECLARE customerLevel VARCHAR(20);

	    IF credit > 50000 THEN
			SET customerLevel = 'PLATINUM';
	    ELSEIF (credit >= 50000 AND
				credit <= 10000) THEN
	        SET customerLevel = 'GOLD';
	    ELSEIF credit < 10000 THEN
	        SET customerLevel = 'SILVER';
	    END IF;
		-- return the customer level
		RETURN (customerLevel);
	END
	 */

    public static void main(String[] args) {

        Connection con = null;
        Properties prop = new Properties();
        prop.put("useSSL", "false");
        prop.put("user", "u579684516_ACCDAT");
        prop.put("password", "AccDat_2DAM");

        try {
            // Se conecta con la base de datos. En este caso es externa y el usuario nos viene dado. Es un usuario únicamente con función de ejecución de funciones
            con = DriverManager.getConnection("jdbc:mysql://185.224.138.49:3306/u579684516_ACCDAT", prop);

            // Obtenermos una instancia de un objeto que implementa la interface CallableStatement.
            CallableStatement cst = con.prepareCall(llamada_funcion);

            cst.registerOutParameter(1, Types.VARCHAR);

            // Se pasa el parámetro como segundo '?' de la llamada
            cst.setString(2, "hola");

            // Se realiza la ejecución
            cst.executeUpdate();

            // Se toma el valor de salida del objeto CallableStatement y se pinta por pantalla.
            System.out.println(cst.getString(1));

            cst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}