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
 handlerMessage - message handler method for deserialization of incoming messages with Person class
 and setting handled timestamp
 **/

@Component
public class AdjMessage extends MessageProducerSupport implements MessageHandler {

    @Override
    public void handleMessage(Message message) throws MessagingException {
        Logger log = Logger.getLogger(AdjMessage.class.getName());

        Person person = (Person) message.getPayload();
        log.info(">>>>>>>>>>New message: " + person.toString());

        person.setHandledTimestamp(System.currentTimeMillis());
        log.info(">>>>>>>>>>Timestamp added: " + person.getHandledTimestamp());

        String keyOfMessage = (String) message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY);

        if (!(keyOfMessage == null)) {
            person.setKeyOfMessage(keyOfMessage);
        }
        else {
            person.setKeyOfMessage("-");
        }

        message = MessageBuilder
            .withPayload(person)
            .build();
        sendMessage(message);
    }
}
