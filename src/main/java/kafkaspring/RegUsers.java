package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

@Component
public class RegUsers implements MessageHandler {

    @Autowired
    ExService exService;

    Person person;

    public Map<Integer, Person> getMapPerson() {
        return mapPerson;
    }

    @Autowired
    ConfigProperties configProperties;

    private String registered;
    private boolean messageFlowStarted = false;
    private boolean full = false;
    private int personNum = 1;
    private int regPersonAllCount = 0;
    private Map<Integer, Person> mapPerson = new TreeMap<>();       // map to collect all messages produced
    private Map<Integer, Person> mapPersonBuff = new TreeMap<>();       // some middle map to transfer messages from mapPerson to database
    private Map.Entry<Integer, Person> pair;
    private ArrayList<Integer> numbers = new ArrayList();

    public synchronized void putMapPersonBuff() throws InterruptedException {       // to transfer elements (messages) from mapPerson to mapPersonBuff (by ThreadNew)
        while (full)
            wait();     // if not all previously collected messages already transferred to database then ThreadNew is waiting
        if (mapPerson.size() >= configProperties.getNumberRecords()) {   // start only if have enough messages in mapPerson (the number of messages see in properties)
            Iterator<Map.Entry<Integer, Person>> itMap = mapPerson.entrySet().iterator();
            int i = 1;
            while (i <= configProperties.getNumberRecords() && itMap.hasNext()) {   // transfer of messages from mapPerson to mapPersonBuff (the number of messages see in properties)
                pair = itMap.next();
                mapPersonBuff.put(i++, pair.getValue());
                numbers.add(pair.getKey());     // putting keys of transferred messages to arraylist
            }
            for (int num : numbers) {
                mapPerson.remove(num, mapPerson.get(num));      // delete messages transferred to mapPersonBuff from mapPerson based on numbers from arraylist
            }
            numbers.clear();        // clear array list

            full = true;        // mapPersonBuff is ready for transferring messages to database
            notify();       // release ThreadJdbc
        }
    }

    public synchronized Person getMapPersonBuff(Integer key) throws InterruptedException {      // // to transfer elements (messages) from mapPersonBuff to database (by ThreadJdbc)
        Person personNew;
        while (!full)
            wait();     // if not enough messages for transferring to database are collected in mapPersonBuff then ThreadJdbc is waiting
        personNew = mapPersonBuff.get(key);     // element from mapPersonBuff is sending to database (see ThreadJdbc)
        mapPersonBuff.remove(key, mapPersonBuff.get(key));      // this element then deleted from mapPersonBuff

        if (mapPersonBuff.size() == 0) {        // if all messages collected in mapPersonBuff are transferred to database then it is empty now and ready for taking new ones from mapPerson
            full = false;
            notify();       // release ThreadNew
        }
        return personNew;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        person = (Person) message.getPayload();
        registered = person.getRegistered();

        if (registered.equals("yes")) {
            mapPerson.put(personNum++, person);     // if get message from registered user then fill mapPerson
            regPersonAllCount++;        // count all messages from reg users
        }

        if (messageFlowStarted == false) {
            exService.threadStart();        // start executor with ThreadNew and ThreadJdbc
            messageFlowStarted = true;      // then messageFlowStarted is put to 'true' to prevent further starting of executor
        }

        System.out.println("mapPerson size: " + mapPerson.size());
        System.out.println("mapPersonBuff size: " + mapPersonBuff.size());
        System.out.println("all persons: " + regPersonAllCount);
    }
}
