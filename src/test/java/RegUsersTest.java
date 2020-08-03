import junit.framework.TestCase;
import kafkaspring.beans.RegUsers;
import kafkaspring.config.ConfigProperties;
import kafkaspring.model.Person;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegUsersTest extends TestCase {

    @Test
    public void testCopyFillMapMethods() throws InterruptedException {

        RegUsers regUsers = new RegUsers();
        ConfigProperties configProperties = new ConfigProperties();

        ArrayList testListPerson = new ArrayList<Person>();
        ArrayList listCopy = new ArrayList<Person>();

        ReflectionTestUtils.setField(configProperties, "numberRecords", 10);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    regUsers.copyMapPerson(testListPerson);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }}, 0, 1, TimeUnit.SECONDS);

        for (int i=1; i<=configProperties.getNumberRecords(); i++) {
            Person person = new Person();
            person.setAge(30+i);

            listCopy.add(person);
            regUsers.fillMapPerson(person, configProperties.getNumberRecords());
        }

        assertEquals("[]", regUsers.getMapPerson().values().toString());
        assertEquals(configProperties.getNumberRecords(), testListPerson.size());
        assertEquals(listCopy, testListPerson);
    }

}
