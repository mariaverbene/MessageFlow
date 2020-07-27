# MessageFlow

kafka-producer topic1:
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic topic1 --property "parse.key=true" --property "key.separator=:"

kafka-cinsumer topic2:
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic topic2 --property "print.key=true" --property "key.separator=:" 

POST-request for sending message:
http://localhost:8080/person

GET-request to see all messages:
http://localhost:8080/messages
