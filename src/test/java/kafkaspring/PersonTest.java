package kafkaspring;

import junit.framework.TestCase;
import org.junit.Test;

public class PersonTest extends TestCase {
    Person person = new Person();
    @Test
    public void testGetAge() {
        person.setAge(30);
        assertEquals(30,person.getAge());
    }
}