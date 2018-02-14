import org.junit.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class TestClass {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static EntityTransaction transaction;

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
    public void testUserAddress() {
//        Collection<User> userCollection = new ArrayList<User>();
        for (int i = 0; i < 1000000; i++) {
            Address address = new Address();
            address.setNumber(String.valueOf(i));
            address.setStreet(String.valueOf(i) + " street.");

            User user = new User();
            user.setAddress(address);
            entityManager.persist(user);
        }

        Date date = new Date();
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> entry = query.from(User.class);

        TypedQuery<User> typedQuery = entityManager.createQuery(query.select(entry));
        List<User> resultList = typedQuery.getResultList();

        Date date1 = new Date();
        System.out.println("entity");
        System.out.println(date1.getTime() - date.getTime());
        for (User user : resultList) {
            user.getAddress();
        }
    }


    @Test
    public void testUserWithEmbeddedAddress() {
//        Collection<User> userCollection = new ArrayList<User>();
        for (int i = 0; i < 1000000; i++) {
            EmbeddedAddress address = new EmbeddedAddress();
            address.setNumber(String.valueOf(i));
            address.setStreet(String.valueOf(i) + " street.");

            UserWithEmbedded user = new UserWithEmbedded();
            user.setAddress(address);
            entityManager.persist(user);
        }

        Date date = new Date();
        entityManager.getTransaction().commit();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> entry = query.from(User.class);

        TypedQuery<User> typedQuery = entityManager.createQuery(query.select(entry));
        List<User> resultList = typedQuery.getResultList();

        Date date1 = new Date();
        System.out.println("embedded");
        System.out.println(date1.getTime() - date.getTime());
        for (User user : resultList) {
            user.getAddress();
        }
    }

    @Ignore
    @Test
    public void testHibernateWorkingFine() throws IOException {
        Message message = new Message();
        message.setMessage("Hello world");

        entityManager.persist(message);

        System.out.println(message.message);

        assert message.message.equals("Hello world");
    }

    @Test
    @Ignore
    public void testHibernateWritesMessage() throws IOException {
        Message message = new Message();
        message.setMessage("goodbye world");

        entityManager.persist(message);

        System.out.println(message.message);

        assert message.message.equals("goodbye world");
    }

    @Test
    @Ignore
    public void testObjectChangesInsideTransaction() {
        Message message = new Message("Message");
        entityManager.persist(message);
        message.message = "newMessage";

        Message newMessage = entityManager.find(Message.class, message.id);
        System.out.println(newMessage.message);
        assert newMessage.message.equals("newMessage");
    }
}
