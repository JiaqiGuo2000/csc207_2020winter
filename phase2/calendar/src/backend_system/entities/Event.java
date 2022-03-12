package backend_system.entities;

import backend_system.managers.AlertManager;
import clock.Clock;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void createFrequencyAlert(String name, LocalDateTime dt, LocalDateTime untilWhen, Duration temporalAmount) {
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
     * Change one of this event's tag to a new tag.
     *
     * @param  tag the old tag to be deleted.
     * @param newTag the new tag to be added.
     */
    public void editTagForOne(Tag tag, Tag newTag){
        tags.remove(tag);
        addTag(newTag);
    }

    /**
     * Change one of this event's memo to a new memo.
     *
     * @param memo the old memo to be deleted.
     * @param newMemo the new memo to be added.
     */
    public void editMemoForOne(Memo memo, Memo newMemo){
        memos.remove(memo);
        addMemo(newMemo);
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
     * @param   o   the Event to be compared.
     * @return  0   if o is equal to this Event;
     *          -1  if the start time of this Event is before that of o, or the time is the same but the name of this
     *          Event is lexicographically lesser;
     *          1   if the start time of this Event is after that of o, or the time is the same but the name of this
     *          Event is lexicographically greater.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Event) {
            int cmp = startDateTime.compareTo(((Event) o).startDateTime);
            return cmp == 0 ? getName().compareTo(((Event) o).getName()) : cmp;
        } else {
            return startDateTime.compareTo((LocalDateTime) o);
        }
    }

    /**
     * Check if this Event is future, ongoing, or past.
     *
     * @param t the time of the event
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
     * This method edits the name, end time and start time of an event with given information.
     * @param endTime new end time of the event
     * @param startTime new start time of the event
     */
    public void setTime(LocalDateTime endTime, LocalDateTime startTime){
        this.endDateTime = endTime;
        this.startDateTime = startTime;
    }

    /**
     * This method edits the name of an event with given information.
     * @param name new name of the event
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Return all the alerts that are related to this Event.
     *
     * @return a list of alerts of this Event.
     */
    public ArrayList<Alert> getAllAlerts() {
        return this.alertManager.getAlertList();
    }

    /**
     * The new overriding version of equals. It compares tags, memos, name, start time and end time.
     * @param o the Event to be compared
     * @return true if the same
     *          false if not the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(tags, event.tags) &&
                Objects.equals(memos, event.memos) &&
                Objects.equals(name, event.name) &&
                Objects.equals(startDateTime, event.startDateTime) &&
                Objects.equals(endDateTime, event.endDateTime);
    }

    /**
     * The new overriding version of hashCode.
     * @return the hash outcome
     */
    @Override
    public int hashCode() {
        return Objects.hash(tags, memos, name, startDateTime, endDateTime);
    }

    /**
     * The new overriding version of toString. It returns some of the information of the event in another format.
     * @return a String contains the information of the event
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return name + " (" + startDateTime.format(formatter) + " - " + endDateTime.format(formatter) + ")";
    }
}
