package net.dbuchwald.learn.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import net.dbuchwald.learn.jpa.model.Customer;

/**
 * Hello world!
 *
 */
public class SimpleJpaApp 
{
    private static final String PERSISTENCE_UNIT_NAME = "simplejpaapp-unit";

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        TypedQuery<Customer> q = em.createQuery("select t from Customer t", Customer.class);
        List<Customer> customerList = q.getResultList();
        for (Customer customer : customerList) {
                System.out.println(customer.toString());
        }
        System.out.println("Size: " + customerList.size());

        // create new todo

        em.close();
        factory.close();
    }
}
