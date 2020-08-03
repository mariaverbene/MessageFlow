package kafkaspring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 topicFrom - topic1
 topicTo - topic2
 numberRecords - number of messages collected in mapPerson before sending to database
 **/

@Component
@ConfigurationProperties("config")
public class ConfigProperties {
    private String topicFrom;
    private String topicTo;
    private int numberRecords;

    public String getTopicFrom() { return topicFrom; }
    public void setTopicFrom(String topicFrom) { this.topicFrom = topicFrom; }

    public String getTopicTo() { return topicTo; }
    public void setTopicTo(String topicTo) { this.topicTo = topicTo; }

    public void setNumberRecords(int numberRecords) { this.numberRecords = numberRecords; }
    public int getNumberRecords() { return numberRecords; }
}