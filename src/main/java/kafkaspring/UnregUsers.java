package kafkaspring;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class UnregUsers extends MessageProducerSupport implements MessageHandler {

    Person person;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        person = (Person) message.getPayload();

        if (person.getRegistered().equals("no")) {      //checking whether user is 'registered'

            message = MessageBuilder
                    .withPayload(person)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, person.getKeyOfMessage())
                    .build();
            sendMessage(message);
        }
    }
}