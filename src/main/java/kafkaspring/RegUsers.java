package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class RegUsers implements MessageHandler {

    @Autowired
    ConfigProperties configProperties;

    private int personNum = 1;
    private Map<Integer, Person> mapPerson = new TreeMap<>();       // map to collect all messages produced

    Person person;

    public synchronized void putMapPerson() throws InterruptedException {
        if (mapPerson.size() < configProperties.getNumberRecords())
            mapPerson.put(personNum++, person);

        if (mapPerson.size() == configProperties.getNumberRecords()) {
            notify();
            wait();
        }
    }

    public synchronized void getMapPerson(List<Person> listPerson) throws InterruptedException {
        if (mapPerson.size() == 0) {
            notify();
            wait();
        }

        listPerson.addAll(mapPerson.values());
        mapPerson.clear();
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        person = (Person) message.getPayload();

        try {
            putMapPerson();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
