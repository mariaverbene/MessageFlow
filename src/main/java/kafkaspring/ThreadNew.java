package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadNew implements Runnable {

    @Autowired
    RegUsers regUsers;

    @Override
    public void run() {
         while (true) {
            try {
                regUsers.putMapPersonBuff();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }
}
