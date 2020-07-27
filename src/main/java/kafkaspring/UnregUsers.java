package kafkaspring;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 handleMessage - message handler method for deserializing messages with Person class,
                adding message key and sending them further (in kafka topic2) in case "no" value was set for "getRegistered" field
 **/

@Component
public class UnregUsers extends MessageProducerSupport implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        Person person = (Person) message.getPayload();

        if (person.getRegistered().equals("no")) {      //checking whether user is 'registered'
            message = MessageBuilder
                    .withPayload(person)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, person.getKeyOfMessage())
                    .build();
            sendMessage(message);
        }
    }
}