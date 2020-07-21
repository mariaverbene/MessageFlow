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
    String keyOfMessage;

    @Override
    public void handleMessage(Message message) throws MessagingException {
        System.out.println(">>>>got new message");

        person = (Person) message.getPayload(); //deserialization
        person.setHandledTimestamp(System.currentTimeMillis()); //setting timestamp = current time

        keyOfMessage = (String) message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY);    //getting key of kafka-message

        if (!(keyOfMessage == null))
            person.setKeyOfMessage(keyOfMessage);
        else
            person.setKeyOfMessage("-");    //messages from POST request do not have keys

        message = MessageBuilder
                .withPayload(person)
                .build();
        sendMessage(message);
    }
}
