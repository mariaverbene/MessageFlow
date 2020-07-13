package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class RegUsers implements MessageHandler {

    @Autowired
    ExService exService;

    private String registered;
    private boolean messageFlowStarted = false;
    private Map<Integer, Person> mapPerson = new ConcurrentHashMap<>();
    private int i = 1;

    Person person;

    public Map<Integer, Person> getMapPerson() {
        return mapPerson;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        person = (Person) message.getPayload();
        registered = person.getRegistered();

        if (registered.equals("yes"))
            mapPerson.put(i++,person);

        if (messageFlowStarted == false) {
            exService.threadStart();
        }

        messageFlowStarted = true;

        System.out.println("mapPerson: " + mapPerson);
    }
}
