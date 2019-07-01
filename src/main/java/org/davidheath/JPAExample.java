package org.davidheath;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEventManager;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class JPAExample {

    public static void main(String[] args) throws InterruptedException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistenceUnit");

        EntityManager entityManager = emf.createEntityManager();
        final Session session = entityManager.unwrap(Session.class);
        session.getEventManager().addListener(new PostCommitHook());
        System.out.println("----\nFirst thread started");
        entityManager.getTransaction().begin();
        System.out.println("First thread: tx begun");

        entityManager.persist(new MyObject("one"));
        entityManager.persist(new MyObject("two"));

        System.out.println("First thread: persisted first object");


        System.out.println("First thread: committing");
        entityManager.getTransaction().commit();
        System.out.println("First thread: committed");
    }

    public static void main_syncq(String[] args) throws InterruptedException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPersistenceUnit");



        SynchronousQueue<String> s = new SynchronousQueue<String>();

        Thread first = new Thread(() -> {
            EntityManager entityManager = emf.createEntityManager();
            final Session session = entityManager.unwrap(Session.class);
            session.getEventManager().addListener(new PostCommitHook());
            System.out.println("----\nFirst thread started");
            entityManager.getTransaction().begin();
            System.out.println("First thread: tx begun");

            MyObject myObject = new MyObject();
            myObject.setStr("one");
            entityManager.persist(myObject);

            System.out.println("First thread: persisted first object");


            System.out.println("First thread: inserting second object");
            myObject = new MyObject();
            myObject.setStr("two");
            entityManager.persist(myObject);
            System.out.println("First thread: second object inserted");

            System.out.println("First thread: committing");
            entityManager.getTransaction().commit();
            System.out.println("First thread: committed");

            try {
                System.out.println("First thread: sending signal");
                s.put("Go!");
                Thread.sleep(500);
                System.out.println("First thread: signal sent");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });


        Thread second = new Thread(() -> {
            EntityManager entityManager = emf.createEntityManager();

            System.out.println("----\nSecond thread started");
            final String poll;
            try {
                poll = s.take();
                System.out.println("Second thread: got signal " + poll);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            entityManager.getTransaction().begin();
            System.out.println("Second thread: begun tx");

            Query query = entityManager.createQuery("select t from MyObject t");
            List resultList1 = query.getResultList();

            System.out.println("Results: " + resultList1);

            System.out.println("Second thread: committing");

            entityManager.getTransaction().commit();
            System.out.println("Second thread: committed");
        });

        first.start();
        second.start();

        first.join();
        second.join();

//        findObjectById(entityManager);
//        queryWithJPQL(entityManager);
//        typedQueryWithJPQL(entityManager);
//        criteriaQuery(entityManager);
//        queryNative(entityManager);
    }

    private static void findObjectById(EntityManager entityManager) {
        System.out.println("----\nfinding object by id");
        MyObject o = entityManager.find(MyObject.class, 2L);
        System.out.println(o);
    }

    private static void queryWithJPQL(EntityManager entityManager) {
        System.out.println("----\nQuerying using JPQL");
        Query query = entityManager.createQuery("select t from MyObject t");
        List resultList1 = query.getResultList();
        System.out.println(resultList1);
    }

    private static void typedQueryWithJPQL(EntityManager entityManager) {
        System.out.println("----\nTyped Querying using JPQL");
        TypedQuery<MyObject> q =
                entityManager.createQuery("select t from MyObject t"
                        , MyObject.class);
        System.out.println(q.getResultList());
    }

    private static void queryNative(EntityManager entityManager) {
        System.out.println("----\nnative query");
        Query nativeQuery = entityManager.createNativeQuery("select * from MyObject");
        List resultList = nativeQuery.getResultList();
        for (Object o : resultList) {
            if (o.getClass().isArray()) {
                Object oa[] = (Object[]) o;
                System.out.println(Arrays.asList(oa));
            } else {
                System.out.println(o);
            }
        }
    }

    private static void criteriaQuery(EntityManager entityManager) {
        System.out.println("----\ncriteria query");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MyObject> query = cb.createQuery(MyObject.class);
        CriteriaQuery<MyObject> select = query.select(query.from(MyObject.class));

        TypedQuery<MyObject> typedQuery = entityManager.createQuery(select);
        System.out.println(typedQuery.getResultList());
    }
}
