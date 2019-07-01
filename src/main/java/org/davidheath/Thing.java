package org.davidheath;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Thing {
    public static void main2(String args[]) {
        System.out.println("Hello world");

        Injector injector = Guice.createInjector(new BillingModule(), new NotOnWeekendsModule());
        final BillingService b = injector.getInstance(BillingService.class);
        b.chargeOrder();
    }

    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static EntityManagerFactory factory;

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        Query q = em.createQuery("select t from Todo t");
        List<Todo> todoList = q.getResultList();
        for (Todo todo : todoList) {
            System.out.println(todo);
        }
        System.out.println("Size: " + todoList.size());

        // create new todo
        em.getTransaction().begin();
        Todo todo = new Todo();
        todo.setSummary("This is a test");
        todo.setDescription("This is a test");
        em.persist(todo);
        em.getTransaction().commit();

        em.close();
    }

}
