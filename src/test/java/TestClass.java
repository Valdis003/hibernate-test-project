import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestClass {
    static EntityManagerFactory entityManagerFactory;
    static EntityManager entityManager;
    static EntityTransaction transaction;

    @BeforeClass
    public static void beforeClassMethod() {
        entityManagerFactory = Persistence.
                createEntityManagerFactory("HelloWorldPU");

        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void afterClassMethod() {
        entityManager.close();
    }

    @Before
    public void beforeTestMethod() {
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void afterTestMethod() {
        transaction.commit();
    }

    @Test
    public void testHibernateWorkingFine() throws IOException {
        Message message = new Message();
        message.setMessage("Hello world");

        entityManager.persist(message);

        System.out.println(message.message);

        assert message.message.equals("Hello world");
    }

    @Test
    public void testHibernateWritesMessage() throws IOException {
        Message message = new Message();
        message.setMessage("goodbye world");

        entityManager.persist(message);

        System.out.println(message.message);

        assert message.message.equals("goodbye world");
    }

    @Test
    public void testObjectChangesInsideTransaction() {
        Message message = new Message("Message");
        entityManager.persist(message);
        message.message = "newMessage";

        Message newMessage = entityManager.find(Message.class, message.id);
        System.out.println(newMessage.message);
        assert newMessage.message.equals("newMessage");
    }
}
