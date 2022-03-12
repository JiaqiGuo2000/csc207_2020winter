package backend_system.entities;

import java.io.Serializable;

/**
 * A class that represents a message sent to a user by another user. It is responsible for storage of all the information
 * of a message, including sender's name, receiver's name, the content of the message and the name of the attached event.
 */
public class Message implements Serializable {
    String sender, receiver, message, event;

    /**
     * This is the constructor of the class. It receives the given values and initialize an instance.
     * @param sender the name of the sender of the message in String
     * @param receiver the name of the receiver of the message in String
     * @param message the content of the message in String
     * @param event the name of the attached event in String; can be null
     */
    public Message(String sender, String receiver, String message, String event) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.event = event;
    }

    /**
     * This is the getter of the name of the sender of a message.
     * @return the name of the sender of a message in String.
     */
    public String getSender() {
        return sender;
    }

    /**
     * This is the getter of the name of the receiver of a message.
     * @return the name of the receiver of a message in String.
     */
    public String getReceiver() {
        return receiver;
    }
    /**
     * This is the getter of the content of a message.
     * @return the content of a message in String.
     */
    public String getMessage() {
        return message;
    }

    /**
     * This is the getter of the name of the attached event of a message.
     * @return the name of the attached event of a message in String.
     */
    public String getEvent() {
        return event;
    }

    /**
     * The toString method is override and returns all the detailed information of the message.
     * @return a String contains all the detailed information of the message
     */
    @Override
    public String toString() {
        return "From: " + sender + " To: " + receiver + "\n" + message + (event != null ? "\nEvent: " + event : "");
    }
}
