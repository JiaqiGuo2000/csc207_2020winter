package backend_system;

import java.io.Serializable;
import java.util.*;

/**
 * a class for an account of a user
 */
public class User implements Serializable {
    private String username, password;
    private Alarm alarm;
    private List<Calendar> calenders;

    /**
     * This is the constructor of the class. It contains the name, password and all the calendars created by this user.
     * @param username the name of the user in String
     * @param password the password of the user in String
     */
    User(String username, String password) {
        this.username = username;
        this.password = password;
        alarm = new Alarm();
        calenders = new ArrayList<>();
    }

    /**
     * This is the getter of the user's name.
     * @return a String represents the name of the user
     */
    public String getName() {
        return username;
    }

    /**
     * This method compares the given String with the password of the user to check if the password is correct.
     * @param password the input password in String
     * @return true if the given password matches the correct one
     *         false if the given password does not match the correct one
     */
    public boolean match(String password) {
        return this.password.equals(password);
    }

    /**
     * create a new calendar for the user
     * @param calendarName the name of the new calendar
     */
    public void addCalender(String calendarName){
        calenders.add(new Calendar(calendarName));
    }

    /**
     * This method returns the names of all the calendars created by the user.
     * @return a List that contains all the names of the calendars created by the user.
     */
    public ArrayList<String> getCalendarsNames(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(Calendar eventManager: calenders){
            arrayList.add(eventManager.getCalendarName());
        }
        return arrayList;
    }

    /**
     * get the calendar with given name, or null if no such calendar exists
     * @param name the wanted name of the calendar
     * @return an Calendar
     */
    public Calendar getCalender(String name){
        for (Calendar em:calenders){
            if(em.getCalendarName().equals(name)){
                return em;
            }
        }
        return null;
    }

    /**
     * This is the getter of the alarm of this user
     * @return an alarm
     */
    public Alarm getAlarm() {
        return alarm;
    }
}
