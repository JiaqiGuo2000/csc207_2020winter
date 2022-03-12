package backend_system;

import backend_system.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * a class for the interactions between users
 */
public class UserSystem {
    private User user;
    private CommonCalendar common;
    private Inboxes inboxes;
    private SerHelper<User> userSer;
    private SerHelper<CommonCalendar> calSer;
    private SerHelper<Inboxes> boxSer;

    /**
     * This method initialize the user system by setting up serializable files and common calendars and inboxes.
     * @throws IOException An exception in file I/O
     * @throws ClassNotFoundException An exception in serializable
     */
    public UserSystem() throws IOException, ClassNotFoundException {
        calSer = new SerHelper<>(new File("usr/common.ser"), new CommonCalendar("Common"));
        common = calSer.read();
        boxSer = new SerHelper<>(new File("usr/inbox.ser"), new Inboxes());
        inboxes = boxSer.read();
    }

    /**
     * This method returns the status whether the user has logged in.
     * @return true if logged in
     *         false if not logged in
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * This method check if the data of user with the given name exists.
     * @param username the target user name in String
     * @return true if exists
     *         false if not exists
     */
    public boolean userExists(String username) {
        File file = new File("usr/" + username + ".ser");
        return file.exists();
    }

    /**
     * This method takes an user name and a password to let the user log in if the user name and the password matches.
     * @param username the user name in String
     * @param password the password in String
     * @return 1 if logged in successfully
     *         -1 if failed to log in
     * @throws IOException An exception in file I/O
     * @throws ClassNotFoundException An exception in serializable
     */
    public int login(String username, String password) throws IOException, ClassNotFoundException {
        if (!userExists(username))
            return -1;
        userSer = new SerHelper<>(new File("usr/" + username + ".ser"), null);
        user = userSer.read();
        if (user.match(password)) {
            common.setUsername(username);
            return 1;
        } else {
            logoff();
            return -1;
        }
    }

    /**
     * This is the getter of the currently logged in user
     * @return the user currently logged in
     */
    public User getUser() {
        return user;
    }

    /**
     * This is the getter of the common calendar
     * @return the common calendar
     */
    public CommonCalendar getCommonCalendar() {
        return common;
    }

    /**
     * This method lets the currently logged in user log off.
     * @throws IOException an exception in file I/O
     */
    public void logoff() throws IOException {
        if (isLoggedIn()) {
            userSer.write(user);
            user = null;
            userSer = null;
            calSer.write(common);
            common.setUsername(null);
            boxSer.write(inboxes);
        }
    }

    /**
     * This method takes an user name and a password to set up a new account for an user.
     * @param username the user name in String
     * @param password the password in String
     * @throws IOException an exception in file I/O
     */
    public void createAccount(String username, String password) throws IOException {
        new SerHelper<>(new File("usr/" + username + ".ser"), new User(username, password));
    }

    /**
     * Send a message with an optional event attached.
     * @param receiver  the receiver of the message
     * @param message   the message
     * @param event     the name of the event
     * @return  1       if the delivery succeeds;
     *          -1      if the receiver does not exist;
     *          -2      if there is not such event in the common calendar.
     */
    public int sendMessage(String receiver, String message, String event) {
        if (!userExists(receiver))
            return -1;
        if (event != null && !common.contains(event, user.getName()))
            return -2;
        Message msg = new Message(user.getName(), receiver, message, event);
        inboxes.send(msg);
        return 1;
    }

    /**
     * This method let the user currently logged in receive message from the inboxes.
     * @return A List contains all the messages that receiver is the user currently logged in
     */
    public List<Message> receiveMessages() {
        return inboxes.receive(user.getName());
    }
}
