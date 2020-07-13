package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Component
public class ThreadNew implements Runnable {

    @Autowired
    RegUsers regUsers;

    @Override
    public void run() {
        for (Map.Entry<Integer, Person> user : regUsers.getMapPerson().entrySet())
            user.getValue().setSomeValue((int) (Math.random() * 100));
        System.out.println("ThreadNew" + Thread.currentThread().getName() + " " + LocalDateTime.now() + " " + Thread.activeCount());
    }
}
