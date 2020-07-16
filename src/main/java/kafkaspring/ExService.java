package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class ExService {
    private ScheduledExecutorService es = Executors.newScheduledThreadPool(2);

    @Autowired
    ThreadJdbc threadJdbc;

    @Autowired
    ThreadNew threadNew;

    public void threadStart() {
        es.execute(threadNew);
        es.execute(threadJdbc);
    }

    @PreDestroy
    public void esShutdown() {
        es.shutdown();
    }
}

