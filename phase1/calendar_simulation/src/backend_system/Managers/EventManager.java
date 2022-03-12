package backend_system.Managers;

import backend_system.Entities.*;
import clock.Clock;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * a class contains all the events and manipulates the events
 */
public class EventManager implements Serializable {

    private String calenderName;
    private TreeSet<Event> pastEvents;
    private TreeSet<Event> ongoingEvents;
    private TreeSet<Event> futureEvents;
    private List<EventSeries> series;
    private NoteManager tagManager;
    private NoteManager memoManager;

    /**
     * construct an empty EventManager
     */
    public EventManager(String calenderName) {
        this.calenderName = calenderName;
        pastEvents = new TreeSet<>();
        /*
         * The following lambda expression CANNOT be replaced with
         * Comparator<Event>.comparing(Event::getEndDateTime) as IntelliJ suggested,
         * or this TreeSet cannot be serialized
         */
        ongoingEvents = new TreeSet<>((Comparator<Event> & Serializable) (e, f) -> {
            return e.getEndDateTime().compareTo(f.getEndDateTime());
        });
        futureEvents = new TreeSet<>();
        series = new ArrayList<>();
        tagManager = new TagManager();
        memoManager = new AlertManager.MemoManager();
    }

    /**
     * show the name of the calender
     * @return a string representing the name of the calendar
     */
    public String getCalenderName(){
        return calenderName;
    }

    /**
     * change the name of this calendar
     * @param newName the new name of the calendar
     */
    public void changeCalendarName(String newName){
        calenderName = newName;
    }

    /**
     * a unit test for viewing events
     */
    static public void main(String[] args) {
        EventManager em = new EventManager("");

        Clock.init(null);
        Clock.setSpeed(0);
        LocalDateTime c = LocalDateTime.parse("2020-03-07T00:00");
        Clock.jumpTo(c);

        final int n = 300;

        LocalDateTime[] t = new LocalDateTime[n];
        t[0] = LocalDateTime.parse("2020-03-07T12:00");
        for (int i = 1; i < n; ++i)
            t[i] = t[i - 1].plusDays(1);

        List<Event> es = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (Math.random() < 0.5) {
                    Event e = new Event(i + ", " + "j", t[i], t[j]);
                    es.add(e);
                }
            }
        }
        Collections.shuffle(es);
        for (Event e : es) em.addEvent(e);

        for (int i = 0; i < n; ++i, c = c.plusDays(Math.random() < 0.5 ? 1 : 2)) {
            List<Event> past = new ArrayList<>(), ongoing = new ArrayList<>(), future = new ArrayList<>();
            for (Event e : es) {
                int cmp = e.ifInProgress(c);
                (cmp == 1 ? future : (cmp == 0 ? ongoing : past)).add(e);
            }
            past.sort(Comparator.comparing(Event::getStartDateTime));
            ongoing.sort(Comparator.comparing(Event::getStartDateTime));
            future.sort(Comparator.comparing(Event::getStartDateTime));
            assert past.equals(em.viewPastEvent());
            assert ongoing.equals(em.viewOngoingEvent());
            assert future.equals(em.viewFutureEvent());
        }
        System.out.println("Pass");
    }

    /**
     * return all the alerts that would be triggered in the future associated with the events they are attached to
     *
     * @return an ArrayList of Array containing the combination of Event and Alert
     */
    public List<Object[]> getAllAlertsOfThisEM() {
        ArrayList<Object[]> alertArrayList = new ArrayList<>();
        for (Event i : futureEvents) {
            Object[] a = new Object[2];
            a[0] = i;
            a[1] = i.viewAllAlertsOfThisEvent();
            alertArrayList.add(a);
        }
        return alertArrayList;
    }

    /**
     * check if there are any alert that could be triggered at the time when this method is called
     */
    public void checkAndRingAll() {
        for (Event i : futureEvents) {
            i.ring();
        }
    }

    /**
     * return all the alerts that have been created associated with the events they are attached to
     *
     * @return an ArrayList of Array containing the combination of Event and Alert
     */
    public ArrayList<Object[]> getAllPastAlerts() {
        ArrayList<Object[]> alertArrayList = new ArrayList<>();
        for (Event i : futureEvents) {
            Object[] a = new Object[2];
            a[0] = i;
            a[1] = i.getPastAlertList();
            alertArrayList.add(a);
        }
        for (Event i : ongoingEvents) {
            Object[] a = new Object[2];
            a[0] = i;
            a[1] = i.getPastAlertList();
            alertArrayList.add(a);
        }
        for (Event i : pastEvents) {
            Object[] a = new Object[2];
            a[0] = i;
            a[1] = i.getPastAlertList();
            alertArrayList.add(a);
        }
        return alertArrayList;
    }

    /**
     * return all the events created
     *
     * @return a list of Event in ascending start time
     */
    public List<Event> viewAllEvent() {
        List<Event> events = viewPastEvent();
        events.addAll(viewOngoingEvent());
        events.addAll(viewFutureEvent());
        return events;
    }

    /**
     * update futureEvents, ongoingEvents and pastEvents according to the time when this method is called
     */
    public void updateEvents() {
        LocalDateTime t = Clock.getTime();
        int cmp;
        while (!futureEvents.isEmpty() && (cmp = futureEvents.first().ifInProgress(t)) != 1)
            (cmp == 0 ? ongoingEvents : pastEvents).add(futureEvents.pollFirst());
        while (!ongoingEvents.isEmpty() && ongoingEvents.first().ifInProgress(t) != 0)
            pastEvents.add(ongoingEvents.pollFirst());
    }

    /**
     * set an alert with given information for a given event
     *
     * @param event the event which the new alert to be attached to
     * @param name  the name of the new alert
     * @param time  the triggered time of the new alert
     */
    public void setAlert(Event event, String name, LocalDateTime time) {
        event.createIndividualAlert(name, time);
    }

    /**
     * set frequency alerts with given information for a given event
     *
     * @param event    the event which the new alert to be attached to
     * @param name     the name of the new alert
     * @param time     the triggered time of the new alert
     * @param duration how many hours is there between one alert and the next alert.
     */
    public void setAlert(Event event, String name, LocalDateTime time, int duration) {
        event.createFrequencyAlert(name, time, event.getStartDateTime(), duration);
    }

    /**
     * create and add a new event with given information
     *
     * @param name      the name of the new event
     * @param startTime the start time of the new event
     * @param endTime   the end time of the new event
     */
    public void createEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        addEvent(new Event(name, startTime, endTime));
    }

    /**
     * add an event
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        LocalDateTime t = Clock.getTime();
        if (event.ifInProgress(t) == 1) {
            futureEvents.add(event);
        } else if (event.ifInProgress(t) == 0) {
            ongoingEvents.add(event);
        } else {
            pastEvents.add(event);
        }
    }

    /**
     * create and add a series of new events with given information
     *
     * @param name       the name of the new events
     * @param startTime  the start time of the new event
     * @param endTime    the end time of the new event
     * @param seriesName the name of the series
     * @param frequency  the frequency of the events in the series
     * @param number     total number of events in the series
     */
    public void createEvent(String name, LocalDateTime startTime, LocalDateTime endTime, String seriesName, int frequency, int number) {
        EventSeries eventSeries = searchSeriesByName(seriesName);
        if (eventSeries == null) {
            EventSeries es = new EventSeries(name, startTime, endTime, seriesName, frequency, number);
            series.add(es);
            for (Event e : es.getEventList()) {
                addEvent(e);
            }
        } else {
            int i = 0;
            while (i < number) {
                Event e = new Event(name, startTime, endTime);
                eventSeries.addEvent(e);
                startTime = startTime.plusDays(frequency);
                endTime = endTime.plusDays(frequency);
                i++;
            }
        }
    }

    /**
     * add an event to a series
     *
     * @param event      the event to be added into series
     * @param seriesName the name of the series which the event is to be added into
     */
    public void addIntoSeries(Event event, String seriesName) {
        // the method to add an event into a given series, or create a new series if there's no such series name
        EventSeries eventSeries = searchSeriesByName(seriesName);
        if (eventSeries == null) {
            EventSeries newEs = new EventSeries(seriesName);
            newEs.addEvent(event);
            this.series.add(newEs);
        } else {
            eventSeries.addEvent(event);
        }
    }

    /**
     * search for all the events with the given name
     *
     * @param name the target event name
     * @return a list of Event
     */
    public List<Event> searchByName(String name) {
        // the method to search all the events in the given name
        List<Event> events = new ArrayList<>();
        for (Event event : pastEvents) {
            if (event.getName().equals(name)) {
                events.add(event);
            }
        }
        for (Event event : ongoingEvents) {
            if (event.getName().equals(name)) {
                events.add(event);
            }
        }
        for (Event event : futureEvents) {
            if (event.getName().equals(name)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * search for all the events that are in any series with the given series name
     *
     * @param name the target series name
     * @return a list of Event
     */
    public List<Event> searchBySeriesName(String name) {
        // the method to search all the events in the given series name
        List<Event> events = null;
        for (EventSeries ev : series) {
            if (ev.getSeriesName().equals(name)) {
                events = new ArrayList<>(ev.getSeries());
            }
        }
        return events;
    }

    /**
     * search for all events with tag of the given name
     *
     * @param tagName the target tag name
     * @return a list of Event
     */
    public List<Event> searchByTag(String tagName) {
        // the method to search all the events in the given tag
        return ((TagManager) tagManager).searchEventByTag(tagName);
    }

    /**
     * search for all the events that start in the given date
     *
     * @param date the target date
     * @return a list of Event
     */
    public List<Event> searchByDate(LocalDate date) {
        // the method to search all the events in the given date
        List<Event> events = new ArrayList<>();
        for (Event event : pastEvents) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                events.add(event);
            }
        }
        for (Event event : ongoingEvents) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                events.add(event);
            }
        }
        for (Event event : futureEvents) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * search the series with given series name
     *
     * @param name the target series name
     * @return an EventSeries
     */
    public EventSeries searchSeriesByName(String name) {
        for (EventSeries i : this.series) {
            if (i.getSeriesName().equals(name)) return i;
        }
        return null;
    }

    /**
     * add a tag with given name to a given event
     *
     * @param tagName the name of the tag to be added
     * @param event   the event which the tag is added on
     */
    public void addTagToEvent(String tagName, Event event) {
        // the method to add tag onto an event
        Tag tag = new Tag(tagName);
        event.addTag(tag);
        tagManager.addNotedEvent(tag, event);
    }

    /**
     * add tags with given name to given events
     *
     * @param tagName the name of the tag to be added
     * @param events  the events which the tags are added on
     */
    public void addTagToEvent(String tagName, List<Event> events) {
        // the method to add tags onto a list of events
        Tag tag = new Tag(tagName);
        for (Event event : events) {
            event.addTag(tag);
        }
        tagManager.addNotedListOfEvents(tag, events);
    }

    /**
     * add a memo with given name to a given event
     *
     * @param memoName the name of the memo to be added
     * @param event    the event which the memo is added on
     */
    public void addMemoToEvent(String memoName, Event event) {
        // the method to add memo onto an event
        Memo memo = new Memo(memoName);
        event.addMemo(memo);
        memoManager.addNotedEvent(memo, event);
    }

    /**
     * return all the memos created before
     *
     * @return a list of Memo
     */
    public List<Note> getAllMemos() {
        return memoManager.getAllNotes();
    }

    /**
     * add the memo with the given index from all the memos to a given event
     *
     * @param event the event which the memo is added on
     * @param index the index of the memo to be added among all the memos
     */
    public void addMemoToEventFromCurrent(Event event, int index) {
        Memo memo = (Memo) getAllMemos().get(index);
        event.addMemo(memo);
        memoManager.addNotedEvent(memo, event);
    }

    /**
     * add a memo with given name to some given events
     *
     * @param memoName the name of the memo to be added
     * @param events   the events which the memo is added on
     */
    public void addMemoToEvent(String memoName, List<Event> events) {
        // the method to add memo onto a list of events
        Memo memo = new Memo(memoName);
        for (Event event : events) {
            event.addMemo(memo);
        }
        memoManager.addNotedListOfEvents(memo, events);
    }

    /**
     * Returns a list of tags that is associated with this event
     *
     * @param event the event that the user currently select
     * @return a list of Tags
     */
    public List<Tag> viewTag(Event event) {
        // the method to return the list of tags of an event
        return event.viewAllTag();
    }

    /**
     * return all the alerts attached to a given event
     *
     * @param event the event whose alerts are required to be viewed
     * @return a list of Alert
     */
    public List<Alert> viewAlert(Event event) {
        return event.viewAllAlertsOfThisEvent();
    }

    /**
     * return all the memos attached to a given event
     *
     * @param event the event whose memos are required to be viewed
     * @return a list of Memo
     */
    public List<Memo> viewMemo(Event event) {
        // the method to return the memo of an event, the header should be modified in this method
        return event.viewAllMemo();
    }

    /**
     * remove a given tag from a given event
     *
     * @param event the event with the tag to be removed from
     * @param tag   the tag to be removed
     */
    public void deleteTag(Event event, Tag tag) {
        event.deleteTag(tag);
        tagManager.deleteNote(event, tag);
    }

    /**
     * remove a given memo from a given event
     *
     * @param event the event with the memo to be removed from
     * @param memo  the memo to be removed
     */
    public void deleteMemo(Event event, Memo memo) {
        event.deleteMemo(memo);
        memoManager.deleteNote(event, memo);
    }

    /**
     * remove a given alert from a given event
     *
     * @param event the event with the alert to be removed from
     * @param alert the alert to be removed
     */
    public void deleteAlert(Event event, Alert alert) {
        event.deleteAlert(alert);
    }

    /**
     * remove a given event if it is present
     *
     * @param e the event to be removed
     * @return whether the event is present or not
     */
    public int deleteEvent(Event e) {
        if (!(pastEvents.remove(e) || ongoingEvents.remove(e) || futureEvents.remove(e)))
            return -1;
        List<Tag> tag = e.viewAllTag();
        List<Memo> memo = e.viewAllMemo();
        for (Tag t : tag)
            tagManager.deleteNote(e, t);
        for (Memo m : memo)
            memoManager.deleteNote(e, m);
        return 1;
    }

    /**
     * show all the events that start in the future
     *
     * @return a list of Event in ascending start time
     */
    public List<Event> viewFutureEvent() {
        updateEvents();
        return new ArrayList<>(this.futureEvents);
    }

    /**
     * show all the events that have started but not ended yet
     *
     * @return a list of Event in ascending start time
     */
    public List<Event> viewOngoingEvent() {
        updateEvents();
        List<Event> es = new ArrayList<>(this.ongoingEvents);
        es.sort(Comparator.comparing(Event::getStartDateTime));
        return es;
    }

    /**
     * show all the events that have already ended
     *
     * @return a list of Event in ascending start time
     */
    public List<Event> viewPastEvent() {
        updateEvents();
        return new ArrayList<>(this.pastEvents);
    }

    /**
     * view all the events which the memo with the given index is attached to
     *
     * @param index the index of the target memo in the list of all memos
     * @return a list of Event
     */
    public List<Event> viewEventFromMemo(int index) {
        Note memo = getAllMemos().get(index);
        return memoManager.getNotes().get(memo);
    }

    /**
     * a method header which is now useless
     */
    public void delete() {
        // the method to delete some object from an event, the header should be modified in this method
    }
}
