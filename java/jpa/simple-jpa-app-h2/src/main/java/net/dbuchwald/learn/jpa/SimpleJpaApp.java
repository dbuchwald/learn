package net.dbuchwald.learn.jpa;

import net.dbuchwald.learn.jpa.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Hello world!
 *
 */
public class SimpleJpaApp 
{
    private static final String PERSISTENCE_UNIT_NAME = "simplejpaapp-unit";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        TypedQuery<Customer> q = em.createQuery("select t from Customer t", Customer.class);
        List<Customer> customerList = q.getResultList();
        for (Customer customer : customerList) {
                System.out.println(customer.toString());
        }
        System.out.println("Size: " + customerList.size());

        // create new todo
        /*
        em.getTransaction().begin();
        Customer customer = new Customer();
        customer.setId(new Long(4));
        customer.setName("Jane Doe");
        customer.setActive(false);
        em.persist(customer);
        em.getTransaction().commit();
        */

        em.close();
        factory.close();
    }
}
