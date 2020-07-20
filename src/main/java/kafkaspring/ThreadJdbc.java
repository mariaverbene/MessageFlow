package kafkaspring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.ArrayList;

@Component
public class ThreadJdbc implements Runnable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Thread threadJdbc;
    RegUsers regUsers;

    int userId;
    int age;
    long timestamp;
    String message;
    String key;
    String username;

    ArrayList<Person>listPerson = new ArrayList<>();

    ThreadJdbc(RegUsers regusers) {
        threadJdbc = new Thread(this);
        this.regUsers = regusers;
        threadJdbc.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                regUsers.getMapPerson(listPerson);
                for (Person person : listPerson) {
                    username = person.getLastName() + " " + person.getFirstName();
                    age = person.getAge();
                    timestamp = person.getHandledTimestamp();
                    message = person.getText();
                    key = person.getKeyOfMessage();

                    if (jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class) == 0)
                        jdbcTemplate.update("insert into users values(?,?)", username, age);

                    userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);
                    jdbcTemplate.update("insert into messages values(?,?,?,?,?)", userId, username, message, timestamp, key);
                    }

                listPerson.clear();
                System.out.println("posted");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        threadJdbc.join();
    }
}
