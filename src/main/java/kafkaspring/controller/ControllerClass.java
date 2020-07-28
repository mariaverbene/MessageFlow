package kafkaspring.controller;

import kafkaspring.beans.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerClass {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/messages")
    public Object count() {
        return userService.getMessages();
    }

//    @Autowired
//    KafkaTemplate kafkaTemplate;
//
//    @PostMapping(value = "/person")
//    public ResponseEntity createPerson(@RequestBody Person person) {
//        ProducerRecord recordNew = new ProducerRecord("topic1", person);
//        kafkaTemplate.send(recordNew);
//        return new ResponseEntity<>("Person is created successfully", HttpStatus.CREATED);
//    }

}



