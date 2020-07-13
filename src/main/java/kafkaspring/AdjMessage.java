package kafkaspring;

import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class AdjMessage extends MessageProducerSupport implements MessageHandler {

    Person person;

    @Override
    public void handleMessage(Message message) throws MessagingException {

        person = (Person) message.getPayload();

        String keyOfMessage = (String) message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY);
        person.setKeyOfMessage(keyOfMessage);
        person.setHandledTimestamp(System.currentTimeMillis());

        message = MessageBuilder
                .withPayload(person)
                .build();
        sendMessage(message);
    }
}
