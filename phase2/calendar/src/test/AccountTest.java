package test;

import backend_system.UserSystem;
import clock.Clock;

import java.io.IOException;

/**
 * A test for account
 */
public class AccountTest {

    /**
     * testing codes
     * @param args the argument
     * @throws IOException an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Clock.init(null);
        UserSystem userSystem = new UserSystem();

        if (userSystem.userExists("test")) {
            System.out.println("User exists.");
        } else {
            userSystem.createAccount("test", "66666");
            System.out.println("Create new account.");
        }
        userSystem.login("test", "66666");
        assert userSystem.isLoggedIn();
        userSystem.logoff();
        assert !userSystem.isLoggedIn();

        userSystem.login("test", "55555");
        assert !userSystem.isLoggedIn();

        System.out.println("pass");
    }
}
