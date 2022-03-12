package backend_system.Entities;

import backend_system.Managers.AlertManager;
import clock.Clock;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Event represents a event with name and start/end datetime in this calendar system, and it also contains the
 * alerts that are related to this event.
 */
public class Event implements Comparable, Serializable {
    private ArrayList<Tag> tags;
    private ArrayList<Memo> memos;
    private AlertManager alertManager;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    /**
     * Create a event with its event name and the start/end datetime.
     *
     * @param name          the name of this event.
     * @param startDateTime the start date and time of this event.
     * @param endDateTime   the end date and time of this event.
     */
    public Event(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isAfter(startDateTime)) {
            this.name = name;

            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            tags = new ArrayList<>();
            memos = new ArrayList<>();
            alertManager = new AlertManager();
        }
    }

    /**
     * Return name of this event.
     *
     * @return the event name that this object contains.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the start time and date of this event.
     *
     * @return the a LocalDateTime object that represents start date and time of this alert.
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Returns the end time and date of this alert.
     *
     * @return the a LocalDateTime object that represents end date and time of this alert.
     */
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * Creates a single alert for this event.
     *
     * @param name the name of the alert.
     * @param dt   the time and date of this alert.
     */
    public void createIndividualAlert(String name, LocalDateTime dt) {
        this.alertManager.createNormalAlert(name, dt);
    }

    /**
     * Creates a series of alerts according frequency that is in seconds.
     *
     * @param name           the name of the alert.
     * @param dt             the first time and date of this alert.
     * @param untilWhen      the last time and date of this alert
     * @param temporalAmount the time between two alerts in seconds.
     */
    public void createFrequencyAlert(String name, LocalDateTime dt, LocalDateTime untilWhen, int temporalAmount) {
        this.alertManager.createFrequencyAlert(name, dt, untilWhen, temporalAmount);
    }

    /**
     * Return all the tags that are related to this event.
     *
     * @return a list of tags associated this event.
     */
    public List<Tag> viewAllTag() {
        return tags;
    }

    /**
     * Return all the memos that are related to this event.
     *
     * @return a list of memos associated this event.
     */
    public List<Memo> viewAllMemo() {
        return memos;
    }

    /**
     * Add a tag to this event.
     *
     * @param t the tag to be added to this event
     */
    public void addTag(Tag t) {
        if (!tags.contains(t))
            tags.add(t);
    }

    /**
     * Add a memo to this event.
     *
     * @param m the memo to be added to this event
     */
    public void addMemo(Memo m) {
        if (!memos.contains(m))
            memos.add(m);
    }

    /**
     * Delete a tag from this event's tag list.
     *
     * @param t the tag to be deleted of this event
     */
    public void deleteTag(Tag t) {
        tags.remove(t);
    }

    /**
     * Delete a memo from this event's memo list.
     *
     * @param m the memo to be added to this event
     */
    public void deleteMemo(Memo m) {
        memos.remove(m);
    }

    /**
     * Delete a alert using alertManager.
     *
     * @param a the alert to be deleted by
     */
    public void deleteAlert(Alert a) {
        alertManager.deleteAlert(a);
    }

    /**
     * Compare two Event objects for ordering.
     *
     * @param o the Event to be compared.
     * @return 0 if the argument Event is equal to this Alert; -1 if this Event is before the argument Event;
     * 1 if this Event is after the argument Event.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Event)
            return startDateTime.compareTo(((Event) o).startDateTime);
        else
            return startDateTime.compareTo((LocalDateTime) o);
    }

    /**
     * Check if this Event is future, ongoing, or past.
     *
     * @return 0 if this Event is in progress; -1 if this Event is in the past;
     * 1 if this Event is in the future.
     */
    public int ifInProgress() {
        return ifInProgress(Clock.getTime());
    }

    /**
     * Check if this Event is future, ongoing, or past.
     *
     * @return 0 if this Event is in progress; -1 if this Event is in the past;
     * 1 if this Event is in the future.
     */
    public int ifInProgress(LocalDateTime t) {
        if (t.isBefore(startDateTime)) {
            return 1;
        } else if (t.isBefore(endDateTime)) {
            return 0;
        } else
            return -1;
    }

    /**
     * Return all the alerts that are related to this Event.
     *
     * @return a list of alerts of this Event.
     */
    public ArrayList<Alert> viewAllAlertsOfThisEvent() {
        return this.alertManager.getAlertList();
    }

    /**
     * Check all the alerts of this Event whether or not they should be displayed.
     */
    public void ring() {
        this.alertManager.ring();
    }

    /**
     * Return all the alerts that have passed
     *
     * @return a list of alerts that was in the past
     */
    public List<Alert> getPastAlertList() {
        return this.alertManager.getPastAlertList();
    }

    /**
     * Return a string of information about this event: name and start/end datetime
     *
     * @return a combined string of information about this event.
     */
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
