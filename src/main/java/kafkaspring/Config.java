package kafkaspring;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.concurrent.ScheduledExecutorService;

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
    public IntegrationFlow topicFlow() {
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(new ConcurrentMessageListenerContainer<String, Person>(new DefaultKafkaConsumerFactory<String, Person>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new JsonDeserializer<>(Person.class)), new ContainerProperties(configProperties.getTopicFrom()))))
                .handle(adjMessage)
                .channel(channelPubSub())
                .get();
    }

    @Bean
    public IntegrationFlow topicFlow1() {
        return IntegrationFlows
                .from(channelPubSub())
                .handle(unRegUsers)
                .handle(Kafka.outboundChannelAdapter(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties())).topic(configProperties.getTopicTo()))
                .get();
    }

    @Bean
    public IntegrationFlow topicFlow2() {
        return IntegrationFlows
                .from(channelPubSub())
                .handle(regUsers)
                .get();
    }

}