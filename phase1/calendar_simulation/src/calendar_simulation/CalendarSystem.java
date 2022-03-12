package calendar_simulation;

import backend_system.Entities.*;
import backend_system.Managers.EventManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class which contains all the data that are shown to the user. It also contains all the operations the user can do
 * in this calender simulation.
 */
public class CalendarSystem {

    private boolean loggedIn = false;
    private String currentUser;
    private AccountManager accountManager;
    private DataManager dataManager;

    private List<Event> currentList = null;
    private Event currentEvent = null;
    private List<Memo> currentMemoList = null;
    private Note currentMemo = null;
    private List<Tag> currentTagList = null;
    private Note currentTag = null;
    private Alert currentAlert = null;
    private List<Alert> currentAlertList = null;
    // the variables that are viewed by the user
    private EventManager eventManager = new EventManager("");

    /**
     * create the CalenderSystem with the file containing the data previously stored in an exteral file
     *
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    CalendarSystem() throws IOException, ClassNotFoundException {
        accountManager = new AccountManager("accounts.ser");
    }

    /**
     * check if the user has logged in
     *
     * @return a boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * let the user log in with given information. This would compare this information with the data previously stored
     * in an external file. It returns an int indicating whether the logging attempt was successful or not.
     *
     * @param username the user name of the user
     * @param password the password of the user
     * @return 1 if successful
     * -1 if unsuccessful
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    int login(String username, String password) throws IOException, ClassNotFoundException {
        if (accountManager.match(username, password)) {
            // still works if username contains whitespaces
            currentUser = username;
            dataManager = new DataManager("usr/" + username + ".ser", eventManager);
            eventManager = dataManager.read();
            loggedIn = true;
            this.eventManager.updateEvents();
            this.eventManager.checkAndRingAll();
            System.out.println("Notification (past alerts):");
            viewAllPastAlerts();
            return 1;
        } else {
            return -1;
        }
    }

    private void viewAllPastAlerts() {
        List<Object[]> arrayList = this.eventManager.getAllPastAlerts();
        if (arrayList.size() != 0) {
            int i = 1;
            for (Object[] arr : arrayList) {
                Event e = (Event) arr[0];
                ArrayList<Alert> alertArrayList = (ArrayList<Alert>) arr[1];
                String s = "";
                for (Alert j : alertArrayList) {
                    s += i + " Past Alert : " + j.getAlertName() + "   for Event " + e.getName() + "\n";
                    i++;
                }
                if (s != "") System.out.println(s);
            }
        }
    }

    /**
     * let the user log off from the system. It returns an int indicating whether this attempt was successful or not.
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int logoff() {
        try {
            if (eventManager != null && dataManager != null) {
                dataManager.write(eventManager);
            }
        } catch (IOException e) {
            return -1;
        }
        dataManager = null;
        eventManager = null;
        loggedIn = false;
        return 1;
    }

    /**
     * check if the given username exists in the system
     *
     * @param username the username to be checked
     * @return a boolean
     */
    boolean accountExists(String username) {
        return accountManager.contains(username);
    }

    String getAccountUser() {
        return currentUser;
    }

    boolean getIfCurrentEventNotNull() {
        return currentEvent != null;
    }

    /**
     * create an account with given information
     *
     * @param username the name of the new account
     * @param password the password of the new account
     * @return 1   if the account is successfully created;
     * -1  if the account has already existed
     * @throws IOException an exception in file I/O
     */
    int createAccount(String username, String password) throws IOException {
        if (accountManager.contains(username))
            return -1;
        accountManager.set(username, password);
        return 1;
    }

    /**
     * delete the currently logged in account
     *
     * @return 1 if successful
     * @throws IOException an exception in file I/O
     */
    int deleteAccount() throws IOException {
        assert (loggedIn);
        dataManager.delete();
        logoff();
        accountManager.remove(currentUser);
        return 1;
    }

    /**
     * change the password of the currently logged in account
     *
     * @param password the new password to be set
     * @return 1 if successful
     * @throws IOException an exception in file I/O
     */
    int changePassword(String password) throws IOException {
        assert (loggedIn);
        accountManager.set(currentUser, password);
        return 1;
    }

    private int viewCurrentEvent() {
        if (currentEvent == null) {
            return -1;
        }
        System.out.println(currentEvent);
        viewCurrentTagList();
        viewCurrentMemoList();
        viewCurrentAlertList();
        return 1;
    }

    /**
     * print all the memos the user has required to view
     */
    void viewCurrentMemoList() {
        int j = 1;
        String s = "";
        for (Note i : this.currentMemoList) {
            s += j + " Memo: " + i.toString() + "\n";
            j++;
        }
        System.out.println(s);
    }

    /**
     * print all the alerts the user has required to view
     */
    void viewCurrentAlertList() {
        int j = 1;
        String s = "";
        for (Alert i : this.currentAlertList) {
            s += j + " Alert: " + i.toString() + "\n";
            j++;
        }
        System.out.println(s);
    }

    /**
     * print all the tags the user has required to view
     */
    void viewCurrentTagList() {
        int j = 1;
        String s = "";
        for (Note i : this.currentTagList) {
            s += j + " Tag: " + i.toString() + "\n";
            j++;
        }
        System.out.println(s);
    }

    /**
     * print all the events the user has required to view
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int getCurrentEventList() {
        if (currentList == null) {
            return -1;
        }
        for (int i = 0; i < currentList.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". ");
            System.out.println(currentList.get(i).toString());
        }
        return 1;
    }

    /**
     * print the name of the currently selected memo
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int getCurrentMemoName() {
        if (currentMemo == null) {
            return -1;
        }
        System.out.print(currentMemo.toString());
        return 1;
    }

    /**
     * print all the memos the user has required to view
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int getCurrentMemoList() {
        if (currentList == null) {
            return -1;
        }
        for (int i = 0; i < currentMemoList.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". ");
            System.out.print(currentMemoList.get(i).toString());
        }
        return 1;
    }

    /**
     * print all the memos that the user has created before
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewAllMemos() {
        List<Note> allMemos = eventManager.getAllMemos();
        if (allMemos.size() == 0) {
            return -1;
        }
        int i = 1;
        StringBuilder s = new StringBuilder();
        for (Note note : allMemos) {
            s.append("Memo ").append(i).append(": ").append(note.toString()).append("\n");
            i++;
        }
        System.out.println(s);
        return 1;
    }

    /**
     * Check all the alerts in every event in this eventManager, and ring if there is some on time.
     */
    void checkAndRing() {
        this.eventManager.checkAndRingAll();
    }

    /**
     * show the user with all the events associated with the selected memo
     *
     * @param index the index of the memo from the list of all the memo that were created previously
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewEventsFromMemos(int index) {
        List<Event> events = eventManager.viewEventFromMemo(index - 1);
        if (events.size() == 0) {
            return -1;
        }
        currentList = events;
        return displayCurrentList();
    }

    /**
     * print the name of the currenly selected tag
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int getCurrentTagName() {
        if (currentTag == null) {
            return -1;
        }
        System.out.println(currentTag);
        return 1;
    }

    /**
     * print all the tags the user has required to view
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int getCurrentTagList() {
        if (currentTagList == null) {
            return -1;
        }
        for (int i = 0; i < currentTagList.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". ");
            System.out.println(currentTagList.get(i));
        }
        return 1;
    }

    private int displayCurrentList() {
        int i = 1;
        String s = "";
        for (Event e : this.currentList) {
            s += "Event " + i + ": " + e.getName() + "\n";
            i++;
        }
        System.out.println(s);
        return 1;
    }

    /**
     * show all the events created to the user
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewAllEvent() {
        currentList = eventManager.viewAllEvent();
        if (currentList.size() == 0) {
            return -1;
        }
        return displayCurrentList();
    }

    /**
     * create an event with given information
     *
     * @param name      the name of the new event
     * @param startTime the start time of the new event
     * @param endTime   the end time of the new event
     * @return 1 if successful
     */
    int createEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        eventManager.createEvent(name, startTime, endTime);
        return 1;
    }

    /**
     * create a series of event with given information
     *
     * @param name       the name of the new event
     * @param startTime  the start time of the new event
     * @param endTime    the end time of the new event
     * @param seriesName the series name of the new event
     * @param frequency  the frequency of the events in the series
     * @param number     the total number of events in the series
     * @return 1 if successful
     */
    int createEvent(String name, LocalDateTime startTime, LocalDateTime endTime, String seriesName, int frequency, int number) {
        eventManager.createEvent(name, startTime, endTime, seriesName, frequency, number);
        return 1;
    }

    /**
     * show all the events that have started but not ended to the user
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewOngoingEvent() {
        currentList = eventManager.viewOngoingEvent();
        if (currentList.size() == 0) {
            return -1;
        }
        return displayCurrentList();
    }

    /**
     * show all the events that have ended to the user
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewPastEvent() {
        currentList = eventManager.viewPastEvent();
        if (currentList.size() == 0) {
            return -1;
        }
        return displayCurrentList();
    }

    /**
     * show all the events that haven't started to the user
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewFutureEvent() {
        currentList = this.eventManager.viewFutureEvent();
        if (currentList.size() == 0) {
            return -1;
        }
        return displayCurrentList();
    }

    /**
     * select the event from the currently being viewed event list with its number
     *
     * @param num the number of event it appears in the list
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int selectEvent(int num) {
        if (currentList == null) {
            return -1;
        }
        if (num < 1 || num > currentList.size()) {
            return -1;
        } else {
            currentEvent = currentList.get(num - 1);
            currentTagList = eventManager.viewTag(currentEvent);
            currentMemoList = eventManager.viewMemo(currentEvent);
            currentAlertList = eventManager.viewAlert(currentEvent);
            viewCurrentEvent();
            return 1;
        }
    }

    /**
     * select the memo from the currently being viewed memo list with its number
     *
     * @param num the number of memo it appears in the list
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int selectMemo(int num) {
        if (num < 1 || num > currentMemoList.size()) {
            return -1;
        } else {
            currentMemo = currentMemoList.get(num - 1);
            System.out.println(currentMemo);
            return 1;
        }
    }

    /**
     * select the alert from the currently being viewed alert list with its number
     *
     * @param num the number of alert it appears in the list
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int selectAlert(int num) {
        if (num < 1 || num > currentMemoList.size()) {
            return -1;
        } else {
            currentAlert = currentAlertList.get(num - 1);
            System.out.println(currentAlert);
            return 1;
        }
    }

    /**
     * select the tag from the currently being viewed tag list with its number
     *
     * @param num the number of tag it appears in the list
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int selectTag(int num) {
        if (num < 1 || num > currentTagList.size()) {
            return -1;
        } else {
            currentTag = currentTagList.get(num - 1);
            System.out.println(currentTag);
            return 1;
        }
    }

    /**
     * deselect current event to enable another select operation
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int deselectEvent() {
        if (currentEvent == null) {
            return -1;
        }
        currentEvent = null;
        currentMemoList = null;
        currentTagList = null;
        currentAlertList = null;
        return 1;
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
     * print all the alerts that are set previously
     *
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int viewAllAlerts() {
        List<Object[]> arrayList = this.eventManager.getAllAlertsOfThisEM();
        if (arrayList.size() == 0) return -1;
        else {
            int i = 1;
            for (Object[] arr : arrayList) {
                Event e = (Event) arr[0];
                ArrayList<Alert> alertArrayList = (ArrayList<Alert>) arr[1];
                String s = "";
                for (Alert j : alertArrayList) {
                    s += i + " Alert : " + j.getAlertName() + "   for Event " + e.getName() + "\n";
                    i++;
                }
                System.out.println(s);
            }
            return 1;
        }
    }

    /**
     * show all the events with the given name
     *
     * @param name the target name
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int searchByName(String name) {
        currentList = eventManager.searchByName(name);
        displayCurrentList();
        return notFound();
    }

    /**
     * show all the events with the given series name
     *
     * @param seriesName the target series name
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int searchBySeriesName(String seriesName) {
        currentList = eventManager.searchBySeriesName(seriesName);
        displayCurrentList();
        return notFound();
    }

    /**
     * show all the events with the given tag name
     *
     * @param tagName the target tag name
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int searchByTag(String tagName) {
        currentList = eventManager.searchByTag(tagName);
        displayCurrentList();
        return notFound();
    }

    /**
     * show all the events with the given date
     *
     * @param date the target date
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int searchByDate(LocalDate date) {
        currentList = eventManager.searchByDate(date);
        displayCurrentList();
        return notFound();
    }

    /**
     * add a tag with given name to the currently selected event
     *
     * @param tagName the name of the added tag
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int addTagToEvent(String tagName) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.addTagToEvent(tagName, currentEvent);
        return 1;
    }

    /**
     * add a memo with given content to the currently selected event
     *
     * @param memo the content of the add memo
     * @return 1 if successful
     */
    int addNewMemo(String memo) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.addMemoToEvent(memo, currentEvent);
        return 1;
    }


    /**
     * add memo with the give number in the list with all memo to the currently selected event
     *
     * @param num the number of the memo in the list with all memo
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int addMemoToEvent(int num) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.addMemoToEventFromCurrent(currentEvent, num - 1);
        return 1;
    }

    /**
     * remove the memo from the currently view memo list by the number of memo
     *
     * @param num the number of the memo to be removed
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int deleteMemo(int num) {
        if (currentMemoList == null || num < 1 || num - 1 > currentMemoList.size()) {
            return -1;
        } else {
            eventManager.deleteMemo(currentEvent, currentMemoList.get(num - 1));
            return 1;
        }
    }

    /**
     * remove the tag from the currently view tag list by the number of tag
     *
     * @param num the number of the tag to be removed
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int deleteTag(int num) {
        if (currentTagList == null || num < 1 || num - 1 > currentTagList.size()) {
            return -1;
        } else {
            eventManager.deleteTag(currentEvent, currentTagList.get(num - 1));
            return 1;
        }
    }

    /**
     * add the currently selected event into the series with the given name
     *
     * @param seriesName the name of the series which the event is being added into
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int addIntoSeries(String seriesName) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.addIntoSeries(currentEvent, seriesName);
        return 1;
    }

    /**
     * set an alert for the currently selected event
     *
     * @param name the name of the alert
     * @param time the triggered time of the alert
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int setAlert(String name, LocalDateTime time) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.setAlert(currentEvent, name, time);
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
    int setAlert(String name, LocalDateTime time, int duration) {
        if (currentEvent == null) {
            return -1;
        }
        eventManager.setAlert(currentEvent, name, time, duration);
        return 1;
    }

    /**
     * remove the alert from the currently viewed alert list with the given number in the list
     *
     * @param num the number that alert in the list
     * @return 1 if successful
     * -1 if unsuccessful
     */
    int deleteAlerts(int num) {
        if (currentAlertList == null || num < 1 || num - 1 > currentAlertList.size()) {
            return -1;
        } else {
            eventManager.deleteAlert(currentEvent, currentAlertList.get(num - 1));
            return 1;
        }
    }
}
