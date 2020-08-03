package kafkaspring.beans;

import kafkaspring.model.Person;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

/**
 handleMessage - message handler method for deserialization of incoming messages with Person class,
                adding message key and sending them further (in kafka topic2) in case "no" value set for "getRegistered" field
 **/

@Component
public class UnregUsers extends MessageProducerSupport implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Logger log = Logger.getLogger(UnregUsers.class.getName());

        Person person = (Person) message.getPayload();
        String registered = person.getRegistered();

        if (registered.equals("no")) {      //checking whether user is 'registered'
            message = MessageBuilder
                    .withPayload(person)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, person.getKeyOfMessage())
                    .build();
            sendMessage(message);
            log.info(">>>>>>>>>>Message from unregistered user " + person.toString() + " forwarded to topic2");
        }
    }
}