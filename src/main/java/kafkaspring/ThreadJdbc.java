package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ThreadJdbc implements Runnable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RegUsers regUsers;
    int userId;
    int age;
    String username;
    long timestamp;
    String message;
    String key;
    int someValue;

    private Thread threadJdbc;

    public ThreadJdbc() {
        Thread threadJdbc = new Thread();
    }

    @Override
    public void run() {
        int count = 0;

        for (Map.Entry<Integer, Person> user : regUsers.getMapPerson().entrySet()) {
            username = user.getValue().getLastName() + " " + user.getValue().getFirstName();
            age = user.getValue().getAge();
            timestamp = user.getValue().getHandledTimestamp();
            message = user.getValue().getText();
            key = user.getValue().getKeyOfMessage();
            someValue = user.getValue().getSomeValue();

            count = jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class);
            if (count == 0)
                jdbcTemplate.update("insert into users values(?,?)", username, age);

            userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);

            count = 0;
            count = jdbcTemplate.queryForObject("select count(*) from messages where timestamp = ?", new Object[]{timestamp}, Integer.class);
            if (count == 0)
                if (someValue >= 0)
                jdbcTemplate.update("insert into messages values(?,?,?,?,?,?)", userId, username, message, timestamp, key, someValue);
        }
        System.out.println("ThreadJDBC" + Thread.currentThread().getName() + " " + LocalDateTime.now() + " " + Thread.activeCount());
    }
}
