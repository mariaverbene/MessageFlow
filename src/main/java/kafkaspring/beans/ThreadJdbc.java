package kafkaspring.beans;

import kafkaspring.model.Person;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.ArrayList;

/**
 run() - to create tables in current database and fill them with collected messages
 **/

@Component
public class ThreadJdbc implements Runnable {

    private Thread threadJdbc;
    private RegUsers regUsers;
    private UserService userService;
    private volatile boolean threadRun = true;
    private ArrayList<Person> listPerson = new ArrayList<>();

    public ThreadJdbc(RegUsers regusers, UserService userService) {
        threadJdbc = new Thread(this);
        this.regUsers = regusers;
        this.userService = userService;
        threadJdbc.start();
    }

    @Override
    public void run() {
        userService.createTables();
        while (threadRun) {
            try {
                regUsers.copyMapPerson(listPerson);
                for (Person person : listPerson) {
                    userService.fillTables(person);
                }
                listPerson.clear();
                }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        threadRun = false;
    }
}
