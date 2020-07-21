package kafkaspring;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;


@Configuration
@EnableIntegration
public class Config {

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private AdjMessage adjMessage;

    @Autowired
    private RegUsers regUsers;

    @Autowired
    private UnregUsers unRegUsers;

    @Bean
    public PublishSubscribeChannel channelPubSub() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public IntegrationFlow topicFlow() {    //messages from kafka producer forwarded to publish-subscribe channel with further splitting between registered and non-registered persons
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(new ConcurrentMessageListenerContainer<String, Person>(new DefaultKafkaConsumerFactory<String, Person>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Person.class))), new ContainerProperties(configProperties.getTopicFrom()))))
                .handle(adjMessage)
                .channel(channelPubSub())
                .get();
    }

    @Bean
    public IntegrationFlow topicFlowUnReg() {   //proceed messages from unregistered users -> in topic2
        return IntegrationFlows
                .from(channelPubSub())
                .handle(unRegUsers)
                .handle(Kafka.outboundChannelAdapter(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties())).topic(configProperties.getTopicTo()))
                .get();
    }

    @Bean
    public IntegrationFlow topicFlowReg() {     //proceed messages from registered users -> collection -> database
        return IntegrationFlows
                .from(channelPubSub())
                .handle(regUsers)
                .get();
    }

    @Bean
    public IntegrationFlow topicFlowPost() {    //messages from POST request
        return IntegrationFlows
            .from(Http.inboundGateway("/person")
                .requestPayloadType(Person.class)
                .requestMapping(m -> m.methods(HttpMethod.POST)))
            .handle(Kafka.outboundChannelAdapter(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties())).topic(configProperties.getTopicFrom()))
            .get();
    }

}