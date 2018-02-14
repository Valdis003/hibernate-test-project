import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory(("HelloWorldPU"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Message message = new Message();
        message.setMessage("Hello world");
        String s = "Шаблон для документа, в котором потрачено @@count@@ рублей.";

        entityManager.persist(message);

        System.out.println(message.message);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();


        transaction.commit();
        entityManager.close();
    }
}
