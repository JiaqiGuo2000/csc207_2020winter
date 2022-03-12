package test;

import backend_system.UserSystem;
import backend_system.entities.Message;
import clock.Clock;

import java.io.IOException;
import java.util.List;

/**
 * a test for messaging
 */
public class MessageTest {
    /**
     * testing code
     * @param args the argument
     * @throws IOException an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Clock.init(null);
        UserSystem userSystem = new UserSystem();

        userSystem.createAccount("Alice", "123");
        userSystem.createAccount("Bob", "456");

        userSystem.login("Alice", "123");
        userSystem.sendMessage("Bob", "I love you.", null);
        userSystem.sendMessage("Bob", "I hate you.", null);
        userSystem.logoff();

        userSystem.login("Bob", "456");
        List<Message> ms = userSystem.receiveMessages();
        for (Message m: ms)
            System.out.println(m);
        userSystem.logoff();
    }
}
