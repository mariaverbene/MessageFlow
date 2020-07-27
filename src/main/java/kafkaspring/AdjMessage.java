package kafkaspring;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

/**
 handlerMessage - message handler method for deserializing messages with Person class,
 getting key of kafka-message and setting timestamp
 **/

@Component
public class AdjMessage extends MessageProducerSupport implements MessageHandler {

    private static Logger log = Logger.getLogger(AdjMessage.class.getName());

    @Override
    public void handleMessage(Message message) throws MessagingException {
        log.info(">>>>got new message");

        Person person = (Person) message.getPayload();
        person.setHandledTimestamp(System.currentTimeMillis());

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
