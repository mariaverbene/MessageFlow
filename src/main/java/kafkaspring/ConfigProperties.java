package kafkaspring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("config")
public class ConfigProperties {
    private String topicFrom;
    private String topicTo;
    private int numberRecords;       //number of records for each transferring from collection to database

    public String getTopicFrom() { return topicFrom; }
    public void setTopicFrom(String topicFrom) { this.topicFrom = topicFrom; }

    public String getTopicTo() { return topicTo; }
    public void setTopicTo(String topicTo) { this.topicTo = topicTo; }

    public void setNumberRecords(int numberRecords) { this.numberRecords = numberRecords; }
    public int getNumberRecords() { return numberRecords; }
}