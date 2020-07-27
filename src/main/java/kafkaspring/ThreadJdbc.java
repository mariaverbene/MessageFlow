package kafkaspring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.logging.Logger;

@Component
public class ThreadJdbc implements Runnable {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Thread threadJdbc;
    private RegUsers regUsers;

    private int userId;
    private int age;
    private long timestamp;
    private String message;
    private String key;
    private String username;

    private ArrayList<Person>listPerson = new ArrayList<>();

    private static Logger log = Logger.getLogger(ThreadJdbc.class.getName());

    public ThreadJdbc(RegUsers regusers) {
        threadJdbc = new Thread(this);
        this.regUsers = regusers;
        threadJdbc.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                regUsers.copyMapPerson(listPerson);     //method from RegUsers to copy values (messages) collected in mapPerson
                for (Person person : listPerson) {      //iteration of messages
                    username = person.getLastName() + " " + person.getFirstName();
                    age = person.getAge();
                    timestamp = person.getHandledTimestamp();
                    message = person.getText();
                    key = person.getKeyOfMessage();

                    if (jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class) == 0) {   //checking whether this user already exists in "user" table
                        jdbcTemplate.update("insert into users values(?,?)", username, age);    //if no such user then put
                    }

                    userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);     //taking user id from "user" table
                    jdbcTemplate.update("insert into messages values(?,?,?,?,?)", userId, username, message, timestamp, key);   //adding message in "message" table with userid
                    }

                listPerson.clear();
                log.info("posted");
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
