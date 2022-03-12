package backend_system;

import backend_system.entities.Event;
import clock.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a common calendar that is shared by all the users. Each events in this calendar are shared by
 * some of the users. It is a subclass of Calendar.
 */
public class CommonCalendar extends Calendar implements Serializable {

    private String username;
    private Map<String, List<String>> eventToUsers = new HashMap<>();

    /**
     * This is the constructor. It takes a String as the name of the Calendar.
     * @param name the name of Calendar in String
     */
    public CommonCalendar(String name) {
        super(name);
    }

    /**
     * This method edits the user name
     * @param username the new user name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method would allow another user to get access to an event.
     * @param event the target event name
     * @param user the target user name
     * @return 1 if successful
     *          -1 if unsuccessful
     */
    public int addUser(String event, String user) {
        if (super.getEvent(event) == null)
            return -1;
        if (eventToUsers.containsKey(event)) {
            List<String> us = eventToUsers.get(event);
            if (!us.contains(user))
                us.add(user);
        } else {
            List<String> us = new ArrayList<>();
            us.add(user);
            eventToUsers.put(event, us);
        }
        return 1;
    }

    /**
     * This method disables the an user's access to an event.
     * @param event the target event name
     * @param user the target user name
     */
    public void removeUser(String event, String user) {
        List<String> us = eventToUsers.get(event);
        us.remove(user);
        if (us.isEmpty())
            deleteEvent(event);
    }

    /**
     * This method returns whether an user has the access to an event
     * @param event the target event name
     * @param user the target user name
     * @return true if the user has the access to the event
     *          false if the user does not have the access to the event
     */
    public boolean contains(String event, String user) {
        if (eventToUsers.containsKey(event)) {
            List<String> us = eventToUsers.get(event);
            return us.contains(user);
        }
        return false;
    }

    /**
     * This method adds an event into the calendar
     * @param event the event to be added
     */
    @Override
    public void addEvent(Event event) {
        super.addEvent(event);
        addUser(event.getName(), username);
    }

    /**
     * This method removes an event from this calendar
     * @param event the event going to be removed
     */
    @Override
    public void deleteEvent(Event event) {
        removeUser(event.getName(), username);
    }

    /**
     * This method shows all the event in the given period of time
     * @param time the target time period
     * @return a List of Events
     */
    @Override
    public List<Event> getEvents(Time time) {
        List<Event> es = new ArrayList<>();
        for (Event e: super.getEvents(time))
            if (contains(e.getName(), username))
                es.add(e);
        return es;
    }

    /**
     * This is the getter of event with given name
     * @param name the target name in String
     * @return an Event if such event exists
     *          null if no such event exists
     */
    @Override
    public Event getEvent(String name) {
        Event e = super.getEvent(name);
        return contains(name, username) ? e : null;
    }
}
