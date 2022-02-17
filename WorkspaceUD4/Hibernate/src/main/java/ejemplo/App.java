package ejemplo;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App {
	public static void main(String[] args) {
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(2000, 00, 20);
		
		Empleado e1 = new Empleado("Empleado 1", calendar.getTime());
		session.save(e1);
		
		calendar.set(2000, 11, 01);
		
		Empleado e2 = new Empleado("Empleado 2", calendar.getTime());
		session.save(e2);
		
		session.getTransaction().commit();
		
		//Cierre de la sesión
		session.close();
		sf.close();
	}

}
