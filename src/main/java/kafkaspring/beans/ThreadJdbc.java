package kafkaspring.beans;

import kafkaspring.model.Person;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 run() - to create tables in current database and fill them with collected messages
 **/

@Component
public class ThreadJdbc implements Runnable {

    private Thread threadJdbc;
    private RegUsers regUsers;
    private UserService userService;

    private ArrayList<Person>listPerson = new ArrayList<>();

    private static Logger log = Logger.getLogger(ThreadJdbc.class.getName());

    public ThreadJdbc(RegUsers regusers, UserService userService) {
       threadJdbc = new Thread(this);
       this.regUsers = regusers;
       this.userService = userService;
       threadJdbc.start();
    }

    @Override
    public void run() {
        userService.createTables();
        while (true) {
            try {
                regUsers.copyMapPerson(listPerson);
                for (Person person : listPerson) {
                    String username = person.getLastName() + " " + person.getFirstName();
                    int age = person.getAge();
                    long timestamp = person.getHandledTimestamp();
                    String message = person.getText();
                    String key = person.getKeyOfMessage();
                    userService.fillTables(username, age, message, timestamp, key);
                }
                listPerson.clear();
                log.info("posted");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        threadJdbc.join();
    }
}
