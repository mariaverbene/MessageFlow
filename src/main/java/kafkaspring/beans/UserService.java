package kafkaspring.beans;

import kafkaspring.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 createTables - method to create tables 'user' and 'messages' in current database if they do not exist
 fillTables - method to fill tables 'user' and 'messages'
 getMessages - method to show messages from inner join request for tables 'user' and 'messages'
 **/

@Component
public class UserService {

    private JdbcTemplate jdbcTemplate;

    UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private ArrayList<Person> messagesLost= new ArrayList<>();
    private Logger log = Logger.getLogger(UserService.class.getName());

    public void createTables() {
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (username varchar(30), age integer, id serial primary key)");
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS messages (userid integer references users (id), username varchar(30), message varchar(255), timestamp bigint, key varchar(10), id serial primary key)");
        }
        catch (DataAccessException e) {
            log.info(">>>>>>>>>>Problems with posting data to database tables");
        }
    }

    public void fillTables(Person person) {
        try {
            String username = person.getLastName() + " " + person.getFirstName();
            int age = person.getAge();
            long timestamp = person.getHandledTimestamp();
            String message = person.getText();
            String key = person.getKeyOfMessage();

            if (jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class) == 0) {
                jdbcTemplate.update("insert into users values(?,?)", username, age);    //if no such user then put
            }
            int userId;
            userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);
            jdbcTemplate.update("insert into messages values(?,?,?,?,?)", userId, username, message, timestamp, key);

            log.info(">>>>>>>>>>Message " + person.toString() + " posted to database");
        }
        catch (DataAccessException e) {
            messagesLost.add(person);
            log.info(">>>>>>>>>>Problems with posting data to database tables, message " + person.toString() + " saved, number of saved messages = " + messagesLost.size());
        }
        }

    public Object getMessages() {
        try {
            String sqlRequest = "Select users.id, users.username, users.age, messages.message, messages.timestamp, messages.key from users inner join messages on users.id = messages.userid";
            return jdbcTemplate.queryForList(sqlRequest);
        }
        catch (DataAccessException e) {
            log.info(">>>>>>>>>>Not possible to show requested data - problems with accessing database tables");
            return null;
        }
    }


}

