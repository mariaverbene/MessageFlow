package kafkaspring;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class RegUsersTest extends TestCase {
    Map<Integer, Person> mapPerson = new TreeMap<>();
    Map<Integer, Person> mapPersonInitial = new TreeMap<>();
    ArrayList<Person> listPerson = new ArrayList<>();
    Person person1 = new Person();
    Person person2 = new Person();
    Person person3 = new Person();

    RegUsers regUsers = new RegUsers();;

    @Test
    public void testCopyMapPerson() throws InterruptedException {

        person1.setLastName("Ivanov");
        person2.setLastName("Petrov");
        person3.setLastName("Sidorov");

        mapPerson.put(1, person1);
        mapPerson.put(2, person2);
        mapPerson.put(3, person3);

        mapPersonInitial.putAll(mapPerson);

        regUsers.setMapPerson(mapPerson);
        regUsers.copyMapPerson(listPerson);

        assertEquals("[]", mapPerson.values().toString());
        assertEquals(mapPersonInitial.values().toString(), listPerson.toString());
    }

}