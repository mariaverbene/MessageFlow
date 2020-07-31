package kafkaspring.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 createTables - method to create tables 'user' and 'messages' in current database if they do not exist
 fillTables - method to fill tables 'user' and 'messages'
 getMessages - method to show messages from inner join request for tables 'user' and 'messages'
 **/

@Component
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTables() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (username text, age integer, id serial primary key)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS messages (userid integer, username text, message text, timestamp bigint, key text, id serial primary key)");
    }

    public void fillTables(String username, Integer age, String message, long timestamp, String key) {
        if (jdbcTemplate.queryForObject("select count(*) from users where username = ?", new Object[]{username}, Integer.class) == 0) {   //checking whether this user already exists in "user" table
            jdbcTemplate.update("insert into users values(?,?)", username, age);    //if no such user then put
        }
        int userId;

        userId = jdbcTemplate.queryForObject("select id from users where username = ?", new Object[]{username}, Integer.class);     //taking user id from "user" table
        jdbcTemplate.update("insert into messages values(?,?,?,?,?)", userId, username, message, timestamp, key);   //adding message in "message" table with userid
    }

    public Object getMessages() {
        String sqlRequest = "Select users.id, users.username, users.age, messages.message, messages.timestamp, messages.key from users inner join messages on users.id = messages.userid";
        return jdbcTemplate.queryForList(sqlRequest);
    }

    }

