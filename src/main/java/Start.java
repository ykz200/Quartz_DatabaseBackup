
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:applicationContext-DatabaseBackupJob.xml");
    }
}
