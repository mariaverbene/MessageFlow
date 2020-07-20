package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerClass {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @RequestMapping(value="/messages")
    public Object count() {
        String sqlRequest = "Select users.id, users.username, users.age, messages.message, messages.timestamp, messages.key from users inner join messages on users.id = messages.userid";
        return jdbcTemplate.queryForList(sqlRequest);
    }

//    @Autowired
//    KafkaTemplate kafkaTemplate;
//
//    @RequestMapping(value = "/person", method = RequestMethod.POST)
//    public ResponseEntity<Object> createPerson(@RequestBody Person person) {
//        ProducerRecord recordNew = new ProducerRecord("topic1", person);
//       // kafkaTemplate.send(recordNew);
//        return new ResponseEntity<>("Person is created successfully", HttpStatus.CREATED);
//    }

}



