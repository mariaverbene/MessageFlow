package kafkaspring.beans;

import kafkaspring.config.ConfigProperties;
import kafkaspring.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 handleMessage - message handler method for deserialization of incoming messages with Person class
                and collecting them in mapPerson collection (with fillMapPerson method) in case other than "no" value set for "getRegistered" field
 fillMapPerson - method for collecting incoming messages in mapPerson collection
 copyMapPerson - method called from ThreadJDBC class for copying messages from mapPerson collection to database
 setMapPerson - method for testing purposes only
 **/

@Component
public class RegUsers implements MessageHandler {

    @Autowired
    private ConfigProperties configProperties;

    private int personNum = 1;
    private Map<Integer, Person> mapPerson = new TreeMap<>();
    Logger log = Logger.getLogger(RegUsers.class.getName());

    public void setMapPerson(Map<Integer, Person> mapPerson) {
        this.mapPerson = mapPerson;
    }

    public synchronized void fillMapPerson(Person person) throws InterruptedException {
        if (mapPerson.size() < configProperties.getNumberRecords()) {
            mapPerson.put(personNum++, person);
        }

        if (mapPerson.size() == configProperties.getNumberRecords()) {
            notify();
            wait();
        }
    }

    public synchronized void copyMapPerson(List<Person> listPerson) throws InterruptedException {
        if (mapPerson.size() == 0) {
            notify();
            wait();
        }

        listPerson.addAll(mapPerson.values());
        mapPerson.clear();
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Person person = (Person) message.getPayload();
        String registered = person.getRegistered();

        try {
            if (!registered.equals("no")) {
                log.info(">>>>>>>>>>Message " + personNum + " from registered user: " + person.toString() + " forwarded to database");
                fillMapPerson(person);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
