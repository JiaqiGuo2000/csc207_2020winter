package backend_system.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class contains all the message of in the system and is responsible for the manipulation of the messages.
 */
public class Inboxes implements Serializable {
    private Map<String, List<Message> > boxes = new HashMap<>();

    /**
     * This method send a message into the system so that other user may receive it.
     * @param msg the message to be sent
     */
    public void send(Message msg) {
        String receiver = msg.getReceiver();
        if (boxes.containsKey(receiver)) {
            List<Message> inbox = boxes.get(receiver);
            inbox.add(msg);
        } else {
            List<Message> inbox = new ArrayList<>();
            inbox.add(msg);
            boxes.put(receiver, inbox);
        }
    }

    /**
     * This method checks and returns all the messages sent to a given user.
     * @param user the name of the user who attempts to receive messages
     * @return an ArrayList containing all the messages sent to the target user. The ArrayList would be empty if no
     * messages have been sent to the target user.
     */
    public List<Message> receive(String user) {
        if (boxes.containsKey(user)) {
            List<Message> inbox = boxes.get(user), ms = new ArrayList<>(inbox);
            inbox.clear();
            return ms;
        } else {
            return new ArrayList<>();
        }
    }
}
