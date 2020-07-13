package kafkaspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExService {
    private ScheduledExecutorService es = Executors.newScheduledThreadPool(2);

    @Autowired
    ThreadJdbc threadJdbc;

    @Autowired
    ThreadNew threadNew;

    public void threadStart() {
        es.scheduleAtFixedRate(threadNew, 0, 5, TimeUnit.SECONDS);
        es.scheduleAtFixedRate(threadJdbc, 1, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void esShutdown() {
        es.shutdown();
    }
}
