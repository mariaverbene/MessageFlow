package kafkaspring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ThreadJdbc implements Runnable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RegUsers regUsers;

    @Autowired
    private ConfigProperties configProperties;

    int userId;
    int age;
    String username;
    long timestamp;
    String message;
    String key;
    int someValue;

    Person user;

    @Override
    public void run() {
        while (true) {
            for (int i = 1; i <= configProperties.getNumberRecords(); i++) {
                try {
                    int count = 0;
                    user = regUsers.getMapPersonBuff(i); // take the values from mapPersonBuff one by one
                    username = user.getLastName() + " " + user.getFirstName();
                    age = user.getAge();
                    timestamp = user.getHandledTimestamp();
                    message = user.getText();
                    key = user.getKeyOfMessage();
                    someValue = user.getSomeValue();

                    // 1) - 4) - with sql tables:
                    // 1) checking in 'users' table whether this user already exists:
                    count = jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class);

                    if (count == 0) // 2) if no put the user into 'users' table:
                    jdbcTemplate.update("insert into users values(?,?)", username, age);

                    // 3) take user id from 'users' table:
                    userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);

                    // 4) put message into 'messages' table with user id from 'users' table:
                    jdbcTemplate.update("insert into messages values(?,?,?,?,?,?)", userId, username, message, timestamp, key, someValue);

                    } catch (InterruptedException e) {
                    e.printStackTrace();
                    }
              }
        }
    }
}
