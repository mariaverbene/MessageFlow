package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ControllerClass {

    @Autowired
    AdjMessage adjMessage;

//    @RequestMapping(value="/count")
//    public String count() {
//        return "Number of all messages = " + adjMessage.getSize() + ", messages from registered users = " + messageCount.getSize() + " :" + messageCount.getValuesMessageCount();
//    }
//
//    @RequestMapping(value = "/messages")
//    public ResponseEntity<Object> showMessages() {
//        return new ResponseEntity<>(messageCash.getMessageCash(), HttpStatus.OK);
//    }

//    @RequestMapping(value = "/person", method = RequestMethod.POST)
//    public ResponseEntity<Object> createPerson(@RequestBody Person person) {
//        adjMessage.setPerson(person.getLastName(), person);
//        return new ResponseEntity<>("Person is created successfully", HttpStatus.CREATED);
//    }

}