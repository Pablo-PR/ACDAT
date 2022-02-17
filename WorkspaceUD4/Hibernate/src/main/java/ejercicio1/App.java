package ejercicio1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class App 
{
    public static void main( String[] args )
    {
    	//Inicialización del SessionFactory
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();
		
		//Apertura de una sesión (e inicio de una transacción)
		Session session = sf.openSession();
		session.beginTransaction();					
		

		Contacto contacto  = new Contacto(1, "Pablo", "Parra", "Rodriguez", 555);
		Contacto contacto1  = new Contacto(2, "María", "Isotrol", "trol", 444);
		Contacto contacto2  = new Contacto(3, "Isco", "Brr", "Prr", 333);
		session.save(contacto);
		session.save(contacto1);
		session.save(contacto2);
		
		//Commit de la transacción
		session.getTransaction().commit();
		
		//Cierre de la sesión
		session.close();
		sf.close();
    }
}
