package calendar_simulation;

import backend_system.Alarm;
import backend_system.Calendar;
import backend_system.entities.*;
import backend_system.User;
import backend_system.UserSystem;
import clock.Time;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class which contains all the data that are shown to the user. It also contains all the operations the user can do
 * in this calender simulation.
 */
public class CalendarSystem {

    private User currentUser = null;
    private UserSystem userSystem = new UserSystem();

    private List<Event> currentList = new ArrayList<>();
    private Event currentEvent = null;
    private List<Memo> currentMemoList = new ArrayList<>();
    private Memo currentMemo = null;
    private List<Tag> currentTagList = new ArrayList<>();
    private Tag currentTag = null;
    private Alert currentAlert = null;
    private List<Alert> currentAlertList = new ArrayList<>();
    private EventSeries currentEventSeries = null;
    private Calendar currentCalendar;

    private static volatile CalendarSystem instance = null;

    private CalendarSystem() throws IOException, ClassNotFoundException {
    }

    /**
     * This method returns the CalendarSystem of the whole program
     * @return a CalendarSystem
     */
    public static CalendarSystem getInstance(){
        if(instance == null){
            synchronized (CalendarSystem.class){
                getInstanceHelper();
            }
        }
        return instance;
    }

    private static void getInstanceHelper(){
        if(instance == null) {
            try {
                instance = new CalendarSystem();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method lets the user log in with the user name and the password if they match
     * @param username the given user name
     * @param password the given password
     * @return 1 if logged in
     *          -1 if failed to log in
     * @throws IOException an exception in file I/O
     * @throws ClassNotFoundException an exception in serializable
     */
    public int login(String username, String password) throws IOException, ClassNotFoundException{
        int result = userSystem.login(username, password);
        if(result == 1){
            currentUser = userSystem.getUser();
        }
        return result;
    }

    /**
     * check if the user has logged in
     *
     * @return a boolean
     */
    public boolean isLoggedIn() {
        return userSystem.isLoggedIn();
    }

    /**
     * Log the user off from the system. It works whether someone has logged in or not and returns an int indicating
     * if this attempt was successful.
     *
     * @return  1 if successful
     *          -1 if unsuccessful
     */
    public int logoff() {
        try {
            userSystem.logoff();
        } catch (IOException e) {
            return -1;
        }
        currentUser = null;
        currentCalendar = null;
        return 1;
    }

    /**
     * check if the given user exists in the system
     *
     * @param username the name of the user to be checked
     * @return a boolean
     */
    public boolean accountExists(String username) {
        return userSystem.userExists(username);
    }

    /**
     * create an account with given information
     *
     * @param username the name of the new account
     * @param password the password of the new account
     * @throws IOException an exception in file I/O
     */
    public void createAccount(String username, String password) throws IOException {
        userSystem.createAccount(username, password);
    }

    /**
     * get all the memos the user has required to view
     *
     * @return  a list of strings that contain each memo message of
     * currently selected memo list.
     */
    public List<String> getCurrentMemoListInfo() {
        List<String> info = new ArrayList<>();
        for(Memo memo: currentMemoList){
            info.add(memo.toString());
        }
        return info;
    }

    /**
     * get all the alerts the user has required to view
     *
     * @return  a list of strings that contain each alert message of
     * currently selected alert list.
     */
    public List<String> getCurrentAlertListInfo() {
        List<String> info = new ArrayList<>();
        for (Alert alert: currentAlertList) {
            info.add(alert.toString());
        }
        return info;
    }

    /**
     * get all the tags the user has required to view
     *
     * @return  a list of strings that contain each tag message of
     * currently selected tag list.
     */
    public List<String> getCurrentTagListInfo() {
        List<String> info = new ArrayList<>();
        for (Tag tag: currentTagList) {
            info.add(tag.toString());
        }
        return info;
    }

    /**
     * This method set which events are going to be displayed
     * @param past the boolean whether the past event is going to be displayed
     * @param ongoing the boolean whether the ongoing event is going to be displayed
     * @param future the boolean whether the future event is going to be displayed
     */
    public void chooseWhatEventsToDisplay(boolean past, boolean ongoing, boolean future){
        List<Event> result = new ArrayList<>();
        if(past) result.addAll(currentCalendar.getEvents(Time.PAST));
        if(ongoing) result.addAll(currentCalendar.getEvents(Time.ONGOING));
        if(future) result.addAll(currentCalendar.getEvents(Time.FUTURE));
        currentList = result;
    }

    /**
     * get the name of the currently selected memo
     *
     * @return the memo message of currently selected memo
     */
    public String getCurrentMemoInfo() {
        if (currentMemo == null) {
            return null;
        }
        return currentMemo.toString();
    }

    /**
     * get all the memos that the user has created before
     *
     */
    public void viewAllMemos() {
        currentMemoList = currentCalendar.getAllMemos()
                .stream()
                .map(e -> (Memo) e)
                .collect(Collectors.toList());
    }

    /**
     * create an event with given information
     *
     * @param name      the name of the new event
     * @param startTime the start time of the new event
     * @param endTime   the end time of the new event
     */
    public void createEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        currentCalendar.createEvent(name, startTime, endTime);
    }

    /**
     * create a series of event with given information
     *
     * @param name       the name of the new event
     * @param startTime  the start time of the new event
     * @param endTime    the end time of the new event
     * @param seriesName the series name of the new event
     * @param duration  the frequency of the events in the series
     * @param number     the total number of events in the series
     */
    public void createEvent(String name, LocalDateTime startTime, LocalDateTime endTime, String seriesName, Duration duration, int number) {
        currentCalendar.createEvent(name, startTime, endTime, seriesName, duration, number);
    }


    /**
     * This method creates a new calendar with a given name
     * @param name the name of the new calendar in String
     */
    public void createCalendar(String name){
        currentUser.addCalender(name);
    }

    /**
     * select the event from the currently being viewed event list with its number
     *
     * @param num the number of event it appears in the list
     */
    public void selectEvent(int num) {
        currentEvent = currentList.get(num);
        currentTagList = currentCalendar.viewTag(currentEvent);
        currentMemoList = currentCalendar.viewMemo(currentEvent);
        currentAlertList = currentCalendar.viewAlert(currentEvent);
    }

    /**
     * select the memo from the currently being viewed memo list with its index
     *
     * @param num the number of memo it appears in the list
     */
    public void selectMemo(int num) {
        currentMemo = currentMemoList.get(num);
    }

    /**
     * select the alert from the currently being viewed alert list with its index
     *
     * @param num the number of alert it appears in the list
     */
    public void selectAlert(int num) {
        currentAlert = currentAlertList.get(num);
    }

    /**
     * select the tag from the currently being viewed tag list with its number
     *
     * @param num the number of tag it appears in the list
     */
    public void selectTag(int num) {
        currentTag = currentTagList.get(num);
    }

    private int notFound() {
        if (currentList == null) {
            return -1;
        } else if (currentList.size() == 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * set the current event list to all the events with the given name
     *
     * @param name the target name
     * @return 1 if successful
     * -1 if unsuccessful
     */
    public int searchByName(String name) {
        currentList = currentCalendar.searchByName(name);
        return notFound();
    }

    /**
     * set the current event list to all the events with the given series name
     *
     * @param seriesName the target series name
     */
    public void searchBySeriesName(String seriesName) {
        currentList = currentCalendar.searchBySeriesName(seriesName);
    }

    /**
     * set the current event list to all the events with the given tag name
     *
     * @param tagName the target tag name
     * @return 1 if successful
     * -1 if unsuccessful
     */
    public int searchByTag(String tagName) {
        currentList = currentCalendar.searchByTag(tagName);
        return notFound();
    }

    /**
     * set the current event list to all the events with the given date
     *
     * @param date the target date
     * @return 1 if successful
     * -1 if unsuccessful
     */
    public int searchByDate(LocalDate date) {
        currentList = currentCalendar.searchByDate(date);
        return notFound();
    }

    /**
     * add a tag with given name to the currently selected event
     *
     * @param tagName the name of the added tag
     */
    public void addTagToEvent(String tagName) {
        currentCalendar.addTagToEvent(tagName, currentEvent);
        currentTagList = currentCalendar.viewTag(currentEvent);
    }

    /**
     * add a memo with given content to the currently selected event
     *
     * @param memo the content of the add memo
     */
    public void addNewMemo(String memo) {
        currentCalendar.addMemoToEvent(memo, currentEvent);
        currentMemoList = currentCalendar.viewMemo(currentEvent);
    }

    /**
     * add the currently selected event into the series with the given name
     *
     * @param seriesName the name of the series which the event is being added into
     */
    public void addIntoSeries(String seriesName) {
        currentCalendar.addIntoSeries(currentEvent, seriesName);
    }

    /**
     * set an alert for the currently selected event
     *
     * @param name the name of the alert
     * @param time the triggered time of the alert
     * @return 1 if successful
     * -1 if unsuccessful
     */
    public int setAlert(String name, LocalDateTime time) {
        if (currentEvent == null) {
            return -1;
        }
        currentCalendar.setAlert(currentEvent, name, time);
        userSystem.getUser().getAlarm().add(name, time);
        currentAlertList = currentCalendar.viewAlert(currentEvent);
        return 1;
    }

    /**
     * set several alerts for the currently selected event
     *
     * @param name     the name of the alert
     * @param time     the triggered time of the alert
     * @param duration the number of hours between two consecutive alerts
     * @return 1 if successful
     * -1 if unsuccessful
     */
    public int setAlert(String name, LocalDateTime time, Duration duration) {
        if (currentEvent == null) {
            return -1;
        }
        currentCalendar.setAlert(currentEvent, name, time, duration);
        userSystem.getUser().getAlarm().add(name, time, currentEvent.getStartDateTime(), duration);
        currentAlertList = currentCalendar.viewAlert(currentEvent);
        return 1;
    }

    /**
     * This method returns whether there is already an event with the given name.
     * @param name the target name in String
     * @return true if such event exists
     *          false if such event does not exist
     */
    public boolean hasEventWithSameName(String name) {
        return currentCalendar.hasEventWithSameName(name);
    }

    /**
     * This is the getter of the name of all the calendars of the current user
     * @return a List of String
     */
    public List<String> getCalendarNamesOfCurrentUser() {
        List<String> cs = new ArrayList<>();
        cs.add("Common");
        cs.addAll(currentUser.getCalendarsNames());
        return cs;
    }

    /**
     * This method would show the user with the merged calendar with some given calendar
     * @param selectedCalendarList an ArrayList of calendars
     */
    public void updateCurrentCalendar(ArrayList<String> selectedCalendarList) {
        if (selectedCalendarList.isEmpty()) {
            currentCalendar = null;
        } else if (selectedCalendarList.size() == 1) {
            String name = selectedCalendarList.get(0);
            if (name.equals("Common"))
                currentCalendar = userSystem.getCommonCalendar();
            else
                currentCalendar = currentUser.getCalender(selectedCalendarList.get(0));
        } else {
            List<Calendar> calendars = new ArrayList<>();
            for(String name: selectedCalendarList) {
                if (name.equals("Common"))
                    calendars.add(userSystem.getCommonCalendar());
                else
                    calendars.add(currentUser.getCalender(name));
            }
            currentCalendar = Calendar.mergeCalendars(calendars);
        }
    }

    /**
     * This is the getter of the names of the events that are going to be displayed to the user
     * @return a List of String
     */
    public List<String> getCurrentEventListName() {
        List<String> result = new ArrayList<>();
        for(Event event: currentList){
            result.add(event.getName());
        }
        return result;
    }

    /**
     * This is the getter of the names of the event series that are going to be displayed to the user
     * @return a List of String
     */
    public List<String> getAllEventSeriesNames() {
        List<EventSeries> es = currentCalendar.getAllEventSeries();
        List<String> result = new ArrayList<>();
        for(EventSeries eventSeries: es){
            result.add(eventSeries.getSeriesName());
        }
        return result;
    }

    /**
     * select Tag with name name
     * @param name The target tag name.
     */
    public void selectTag(String name){
        currentTag = new Tag(name);
    }

    /**
     * This is the getter of the name of the currently selected event
     * @return the name of the event if an event is selected
     *          null if no event is selected
     */
    public String getCurrentEventName() {
        if(currentEvent == null) return null;
        return currentEvent.getName();
    }

    /**
     * This is the getter of the start time of the currently selected event
     * @return a LocalDateTime of the start time of the event if an event is selected
     *          null if no event is selected
     */
    public LocalDateTime getCurrentEventStartDT() {
        if(currentEvent == null) return null;
        return this.currentEvent.getStartDateTime();
    }

    /**
     * This is the getter of the end time of the currently selected event
     * @return a LocalDateTime of the end time of the event if an event is selected
     *          null if no event is selected
     */
    public LocalDateTime getCurrentEventEndDT() {
        if(currentEvent == null) return null;
        return this.currentEvent.getEndDateTime();
    }

    /**
     * This method would replace end time and start time of the selected event with the given ones
     * @param endTime the new end time
     * @param startTime the new start time
     * @return 1 if successful
     *          -1 if new information is invalid
     */
    public int editEventTime(LocalDateTime endTime, LocalDateTime startTime) {
        if(endTime.isBefore(startTime)) {return -1;}
        currentCalendar.editEventTime(currentEvent, endTime, startTime);
        return 1;
    }

    /**
     * This method would replace name of the selected event with the given ones
     * @param name the new name
     * @return 1 if successful
     *          -1 if new information is invalid
     */
    public int editEventName(String name){
        if(hasEventWithSameName(name)) return -1;
        currentCalendar.editEventName(currentEvent, name);
        return 1;
    }

    /**
     * This method would remove the currently selected event from the calendar
     */
    public void deleteCurrentEvent() {
        currentCalendar.deleteEvent(currentEvent);
        currentList.remove(currentEvent);
    }

    /**
     * remove the current Memo from the currently view memo list
     *
     * @param mode 1 when deleting from event; 2 when deleting the
     *             entire memo
     */
    public void deleteCurrentMemo(int mode){
        if(mode==1){
            currentCalendar.deleteMemoFromEvent(currentEvent, currentMemo);
        } else if(mode==2){
            currentCalendar.deleteEntireMemo(currentMemo);
        }
        currentMemoList.remove(currentMemo);
        currentMemo = null;
    }

    /**
     * remove the current tag from the currently view tag list
     *
     * @param mode 1 when deleting from event; 2 when deleting the
     *             entire tag
     */
    public void deleteCurrentTag(int mode){
        if(mode==1){
            currentCalendar.deleteTagFromEvent(currentEvent, currentTag);
        } else if(mode==2){
            currentCalendar.deleteEntireTag(currentTag);
        }
        currentTagList.remove(currentTag);
        currentTag = null;
    }

    /**
     * This method would edit the name of the currently selected memo.
     * @param mode 1 if the memo is selected from event
     *             2 if the memo is selected by viewing all the memos in the calendar
     * @param newMemoName the new name of the memo
     */
    public void editCurrentMemo(int mode, String newMemoName){
        if(mode==1){
            currentCalendar.editMemoForOne(currentMemo, newMemoName, currentEvent);
        } else if(mode==2){
            currentCalendar.editEntireMemo(currentMemo, newMemoName);
        }
        currentMemo = new Memo(newMemoName);
        getEventsOfCurrentMemo();
        currentMemoList = currentCalendar.viewMemo(currentEvent);
    }

    /**
     * This method would edit the name of the currently selected tag.
     * @param mode 1 if the tag is selected from event
     *             2 if the tag is selected by viewing all the memos in the calendar
     * @param newTagName the new name of the memo
     */
    public void editCurrentTag(int mode, String newTagName){
        if(mode==1){
            currentCalendar.editTagForOne(currentTag, newTagName, currentEvent);
        } else if(mode==2){
            currentCalendar.editEntireTag(currentTag, newTagName);
        }
        currentTag = new Tag(newTagName);
        getEventsOfCurrentTag();
    }

    /**
     * This is the getter of the name of the currently selected alert
     * @return the name of the alert in String if an alert is selected
     *          null if no alert is selected
     */
    public String getCurrentAlertName() {
        if(currentAlert == null) return null;
        return currentAlert.getAlertName();
    }

    /**
     * This is the getter of the time of the currently selected alert
     * @return the LocalDateTime of the alert if an alert is selected
     *          null if no alert is selected
     */
    public LocalDateTime getCurrentAlertTime() {
        if(currentAlert == null) return null;
        return currentAlert.getAlertDateTime();
    }

    /**
     * This method edits the name and the triggered time of the currently selected alert
     * @param text the new name
     * @param newDT the new triggered time
     */
    public void editCurrentAlert(String text, LocalDateTime newDT) {
        // Don't forget to change userSystem.getUser().getAlert()
        currentCalendar.setAlertInfo(currentAlert, text, newDT);
    }

    /**
     * remove the alert from the currently viewed alert list
     *
     */
    public void deleteCurrentAlert() {
        currentCalendar.deleteAlert(currentEvent, currentAlert);
        currentAlertList.remove(currentAlert);
        userSystem.getUser().getAlarm().remove(currentAlert);
        currentAlert = null;
    }

    /**
     * get all the tags that the user has created before
     *
     * @return a list of strings that contain all the tag messages
     */
    public List<String> getContentOfAllTags() {
        return currentCalendar.getContentOfAllTags();
    }

    /**
     * THi is the getter of all the contents of the memos
     * @return a List of String
     */
    public List<String> getContentOfAllMemos() {
        return currentCalendar.getContentOfAllMemos();
    }

    /**
     * This method would let the user select a series with the given name
     * @param name the target series name
     */
    public void selectSeries(String name){
        currentEventSeries = currentCalendar.searchSeriesByName(name);
    }

    /**
     * This method edits the name of the currently selected series
     * @param text the new series name
     */
    public void editCurrentSeries(String text) {
        currentCalendar.changeNameTo(currentEventSeries, text);
    }

    /**
     * This method removes the currently selected series from the calendar.
     */
    public void deleteCurrentSeries() {
        currentCalendar.deleteEventSeries(currentEventSeries);
        currentEventSeries = null;
    }

    /**
     * This method checks if a calendar has been selected and displayed
     * @return true if a calendar is selected and displayed
     *          false if no calendar is selected and displayed
     */
    public boolean currentCalendarIsNotNull(){
        return currentCalendar != null;
    }
    
    /**
     * Send a message with an optional event attached.
     * @param receiver  the receiver of the message
     * @param message   the message
     * @param event     the name of the event; can be null
     * @return  1       if the delivery succeeds;
     *          -1      if the receiver does not exist;
     *          -2      if there is not such event in the common calendar.
     */
    public int sendMessage(String receiver, String message, String event) {
        return userSystem.sendMessage(receiver, message, event);
    }

    /**
     * Receive the messages sent to the user. The inbox will be cleared.
     * @return [sender, message, event] for every message
     */
    public List<String[]> receiveMessages() {
        List<Message> ms = userSystem.receiveMessages();
        List<String[]> ss = new ArrayList<>();
        for (Message m: ms)
            ss.add(new String[] {m.getSender(), m.getMessage(), m.getEvent()});
        return ss;
    }

    /**
     * Get the time of the event.
     * @param calendar  the name of the calendar
     * @param event     the name of the event
     * @return  [start time, end time]  if the event exists;
     *          null                    otherwise.
     */
    public LocalDateTime[] getTime(String calendar, String event) {
        Event e = null;
        if ("Common".equals(calendar)) {
            e = userSystem.getCommonCalendar().getEvent(event);
        } else {
            Calendar c = userSystem.getUser().getCalender(calendar);
            if (c != null)
                e = c.getEvent(event);
        }
        return e == null ? null : new LocalDateTime[]{e.getStartDateTime(), e.getEndDateTime()};
    }

    /**
     * Add the user to the event in the common calendar.
     * @param user  the name of the user
     * @param event the name of the event
     * @return  1   if the operation succeeds;
     *          -1  if the event does not exist in the common calendar.
     */
    public int addUserToEvent(String user, String event) {
        return userSystem.getCommonCalendar().addUser(event, user);
    }

    /**
     * Return the name of the current user.
     *
     * Precondition: isLoggedIn()
     *
     * @return the name of the current user
     */
    public String getUsername() {
        return userSystem.getUser().getName();
    }

    /**
     * Return the string representation of the next alert that has not been rang.
     *
     * Precondition: isLoggedIn()
     *
     * @return  the string representation of the next alert that has not been rang if such alert exists;
     *          null otherwise
     */
    public String getNextAlert() {
        Alarm alarm = userSystem.getUser().getAlarm();
        if (alarm.hasNext())
            return alarm.next().toString();
        return null;
    }

    /**
     * This is the getter of the information of the selected tags
     * @return a String
     */
    public String getCurrentTagInfo() {
        if(currentTag == null) return null;
        return currentTag.toString();
    }

    /**
     * This method shows the user with all the event corresponding with the currently selected memo.
     */
    public void getEventsOfCurrentMemo() {
        currentList = currentCalendar.getEventsOfMemo(currentMemo);
    }

    /**
     * This method returns the name of currentSeries.
     * @return name of currentSeries
     */
    public String getCurrentSeriesName() {
        return currentCalendar.getSeriesName(currentEventSeries);
    }

    /**
     * This method change current list to events associated to currentTag.
     */
    public void getEventsOfCurrentTag() {
        currentList = currentCalendar.getEventsOfTag(currentTag);
    }


}
