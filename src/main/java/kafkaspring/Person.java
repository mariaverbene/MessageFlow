package kafkaspring;

public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private long handledTimestamp;
    private String text;
    private String keyOfMessage;
    private String registered;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setHandledTimestamp(long handledTimestamp) { this.handledTimestamp = handledTimestamp; }
    public long getHandledTimestamp() { return handledTimestamp; }

    public void setKeyOfMessage(String keyOfMessage) { this.keyOfMessage = keyOfMessage; }
    public String getKeyOfMessage() { return keyOfMessage; }

    public void setText(String text) { this.text = text; }
    public String getText() { return text; }

    public void setRegistered(String registered) { this.registered = registered; }
    public String getRegistered() { return registered; }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", registered=" + registered +
                ", text=" + text +
                ", handledTimestamp=" + handledTimestamp +
                ", keyOfMessage=" + keyOfMessage +
                '}';
    }
}
