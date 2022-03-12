package backend_system;

import backend_system.entities.*;
import backend_system.managers.NoteManager;
import backend_system.managers.TagManager;
import clock.Clock;
import clock.Time;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * a class contains all the events and manipulates the events
 */
public class Calendar implements Serializable {

    private TreeSet<Event> pastEvents;
    private TreeSet<Event> ongoingEvents;
    private TreeSet<Event> futureEvents;
    private Map<String, Event> events;
    private List<EventSeries> series;
    private NoteManager tagManager;
    private NoteManager memoManager;
    private String calendarName;

    /**
     * construct an empty Calendar
     * @param name the name of the Calendar
     */
    public Calendar(String name) {
        calendarName = name;
        pastEvents = new TreeSet<>();
        /*
         * The following lambda expression CANNOT be replaced with Comparator chain as IntelliJ suggested,
         * or this TreeSet cannot be serialized
         */
        ongoingEvents = new TreeSet<>((Comparator<Event> & Serializable) (e, f) -> {
            int cmp = e.getEndDateTime().compareTo(f.getEndDateTime());
            return cmp == 0 ? e.getName().compareTo(f.getName()) : cmp;
        });
        futureEvents = new TreeSet<>();
        events = new HashMap<>();
        series = new ArrayList<>();
        tagManager = new TagManager();
        memoManager = new NoteManager();
    }

    /**
     * This is the constructor of a calendar with given details instead of an empty calendar.
     * @param name the name of the calendar in String
     * @param pastEvents the TreeSet of Events that has past
     * @param ongoingEvents the TreeSet of Events that is ongoing
     * @param futureEvents the TreeSet of Events that would happen in future
     * @param events a Map between the Events and their names
     * @param series a List containing all the series
     * @param tagManager the TagManager of this calendar
     * @param memoManager the MemoManager of this calendar
     */
    public Calendar(String name, TreeSet<Event> pastEvents, TreeSet<Event> ongoingEvents, TreeSet<Event> futureEvents,
                    Map<String, Event> events, List<EventSeries> series,
                    NoteManager tagManager, NoteManager memoManager){
        this.calendarName=name;
        this.pastEvents=pastEvents;
        this.ongoingEvents=ongoingEvents;
        this.futureEvents=futureEvents;
        this.events=events;
        this.series=series;
        this.tagManager=tagManager;
        this.memoManager=memoManager;
    }

    /**
     * This is the getter of the name of this calendar
     *
     * @return the name of this calendar in String
     */
    public String getCalendarName(){
        return calendarName;
    }

    /**
     * get the event through its name
     *
     * @param name the name the the event
     * @return the event that matches its given name
     */
    public Event getEvent(String name) {
        return events.get(name);
    }

    /**
     * get events based on the status
     *
     * @param time the indicator of the events
     * @return a list of events that match
     */
    public List<Event> getEvents(Time time) {
        List<Event> es;
        switch (time) {
            case ALL:
                es = getAllEvents();
                break;
            case PAST:
                es = getPastEvents();
                break;
            case ONGOING:
                es = getOngoingEvents();
                break;
            case FUTURE:
                es = getFutureEvents();
                break;
            default:
                es = new ArrayList<>();
        }
        return es;
    }

    /**
     * return all the events created
     *
     * @return a list of Event in ascending start time
     */
    private List<Event> getAllEvents() {
        List<Event> events = getPastEvents();
        events.addAll(getOngoingEvents());
        events.addAll(getFutureEvents());
        return events;
    }

    /**
     * update futureEvents, ongoingEvents and pastEvents according to the time when this method is called
     */
    private void updateEvents() {
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
    public void setAlert(Event event, String name, LocalDateTime time, Duration duration) {
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
        events.put(event.getName(), event);
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
     * @param duration  the frequency of the events in the series
     * @param number     total number of events in the series
     */
    public void createEvent(String name, LocalDateTime startTime, LocalDateTime endTime, String seriesName, Duration duration, int number) {
        EventSeries eventSeries = searchSeriesByName(seriesName);
        if (eventSeries == null) {
            EventSeries es = new EventSeries(name, startTime, endTime, seriesName, duration, number);
            series.add(es);
            for (Event e : es.getEventList()) {
                addEvent(e);
            }
        } else {
            int i = 0;
            while (i < number) {
                Event e = new Event(name, startTime, endTime);
                eventSeries.addEvent(e);
                startTime = startTime.plus(duration);
                endTime = endTime.plus(duration);
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
        List<Event>a = ((TagManager) tagManager).searchEventByTag(tagName);
        if(a != null) return a;
        else{return new ArrayList<>();}
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
        if(tagName.equals("")) return;
        Tag tag = new Tag(tagName);
        event.addTag(tag);
        tagManager.addNotedEvent(tag, event);
    }

    /**
     * add a memo with given name to a given event
     *
     * @param memoName the name of the memo to be added
     * @param event    the event which the memo is added on
     */
    public void addMemoToEvent(String memoName, Event event) {
        if (memoName.equals("")) return;
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
        return event.getAllAlerts();
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
     * Edit a tag for one particular event associated.
     *
     * @param tag  the old tag to be removed
     * @param newTagName the new tag message to be added
     * @param event the event that is associated
     */
    public void editTagForOne(Tag tag, String newTagName, Event event){
        Tag newTag = new Tag(newTagName);
        tagManager.editNoteForOne(tag, newTag, event);
        event.editTagForOne(tag, newTag);
    }

    /**
     * Edit a tag for all events that associated.
     *
     * @param tag  the old tag to be removed
     * @param newTagName the new tag message to be added
     */
    public void editEntireTag(Tag tag, String newTagName){
        Tag newTag = new Tag(newTagName);
        List<Event> events = tagManager.editEntireNote(tag, newTag);
        for (Event event:events){
            event.editTagForOne(tag, newTag);
        }
    }

    /**
     * Edit a memo for one particular event associated.
     *
     * @param memo  the old memo to be removed
     * @param newMemoName the new memo message to be added
     * @param event the event that is associated
     */
    public void editMemoForOne(Memo memo, String newMemoName, Event event){
        Memo newMemo = new Memo(newMemoName);
        memoManager.editNoteForOne(memo, newMemo, event);
        event.editMemoForOne(memo, newMemo);
    }

    /**
     * Edit a memo for all events that associated.
     *
     * @param memo  the old memo to be removed
     * @param newMemoName the new memo message to be added
     */
    public void editEntireMemo(Memo memo, String newMemoName){
        Memo newMemo = new Memo(newMemoName);
        List<Event> events = memoManager.editEntireNote(memo, newMemo);
        for (Event event:events){
            event.editMemoForOne(memo, newMemo);
        }
    }

    /**
     * Remove a tag entirely from the calendar
     *
     * @param tag the tag to be removed
     */
    public void deleteEntireTag(Tag tag){
        List<Event> events = tagManager.deleteEntireNote(tag);
        for (Event event:events){
            event.deleteTag(tag);
        }
    }

    /**
     * Remove a memo entirely from the calendar
     *
     * @param memo the memo to be removed
     */
    public void deleteEntireMemo(Memo memo){
        List<Event> events = memoManager.deleteEntireNote(memo);
        for (Event event:events){
            event.deleteMemo(memo);
        }
    }

    /**
     * remove a given tag from a given event
     *
     * @param event the event with the tag to be removed from
     * @param tag   the tag to be removed
     */
    public void deleteTagFromEvent(Event event, Tag tag) {
        event.deleteTag(tag);
        tagManager.deleteNoteFromEvent(event, tag);
    }

    /**
     * remove a given memo from a given event
     *
     * @param event the event with the memo to be removed from
     * @param memo  the memo to be removed
     */
    public void deleteMemoFromEvent(Event event, Memo memo) {
        event.deleteMemo(memo);
        memoManager.deleteNoteFromEvent(event, memo);
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
     * @param event the name of the event to be removed
     */
    public void deleteEvent(String event) {
        deleteEvent(events.get(event));
    }

    /**
     * remove a given event; the event should be present
     *
     * @param e the event to be removed
     */
    public void deleteEvent(Event e) {
        List<Tag> tag = e.viewAllTag();
        List<Memo> memo = e.viewAllMemo();
        for (Tag t : tag)
            tagManager.deleteNoteFromEvent(e, t);
        for (Memo m : memo)
            memoManager.deleteNoteFromEvent(e, m);
        for(EventSeries es : series)
            es.removeEventFromSeries(e);
        events.remove(e.getName());
        pastEvents.remove(e);
        ongoingEvents.remove(e);
        futureEvents.remove(e);
    }

    /**
     * show all the events that start in the future
     *
     * @return a list of Event in ascending start time
     */
    private List<Event> getFutureEvents() {
        updateEvents();
        return new ArrayList<>(this.futureEvents);
    }

    /**
     * show all the events that have started but not ended yet
     *
     * @return a list of Event in ascending start time
     */
    private List<Event> getOngoingEvents() {
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
    private List<Event> getPastEvents() {
        updateEvents();
        return new ArrayList<>(this.pastEvents);
    }

    /**
     * This method returns whether there is already an event with the given name.
     * @param name the target name in String
     * @return true if such event exists
     *          false if such event does not exist
     */
    public boolean hasEventWithSameName(String name){
        for(Event event: pastEvents){
            if(event.getName().equals(name))
                return true;
        }
        for(Event event: ongoingEvents){
            if(event.getName().equals(name))
                return true;
        }
        for(Event event: futureEvents){
            if(event.getName().equals(name))
                return true;
        }
        return false;
    }

    /**
     * This is the getter of the all the series of this calendar
     * @return a List contains all the series of this calendar
     */
    public List<EventSeries> getAllEventSeries(){
        return series;
    }

    /**
     * This method would replace the name, end time and start time of a given event with the given ones
     * @param event the target event
     * @param endTime the new end time
     * @param startTime the new start time
     */
    public void editEventTime(Event event, LocalDateTime endTime, LocalDateTime startTime){
        event.setTime(endTime, startTime);
    }

    /**
     * This method would replace the name, end time and start time of a given event with the given ones
     * @param event the target event
     * @param name the new name
     */
    public void editEventName(Event event, String name){
        event.setName(name);
    }

    /**
     * this is the getter of the getter of the map between the Events and their names
     * @return a Map between the Events and their names
     */
    public Map<String, Event> getEventsMapping(){
        return events;
    }

    /**
     * This the getter of the Managers
     * @param type the wanted type of the manager
     * @return a NoteManager
     */
    public NoteManager getManager(String type){
        if (type.equals("tag")) return tagManager;
        if (type.equals("memo")) return memoManager;
        return null;
    }

    /**
     * This method merges several note managers' information together to create a new manager tha contains all the
     * information of the merged not managers
     * @param managers the List of NoteManagers
     * @return a NoteManger
     */
    public static NoteManager mergeNoteManager(List<NoteManager> managers){
        HashMap<Note, List<Event>> notes = new HashMap<>();
        List<Note> noteList = new ArrayList<>();
        for(NoteManager noteManager: managers){
            notes.putAll(noteManager.getNotes());
            noteList.addAll(noteManager.getAllNotes());
        }
        return new NoteManager(notes, noteList);
    }

    /**
     * This is the getter of all the Tags of the TagManager
     * @return a List of String
     */
    public List<String> getContentOfAllTags(){
        return tagManager.getContentOfAllNotes();
    }

    /**
     * This is the getter of the all the memos' info
     * @return a List of String
     */
    public List<String> getContentOfAllMemos(){
        return memoManager.getContentOfAllNotes();
    }

    /**
     * This method changes the name of an event series
     * @param currentEventSeries the target event series
     * @param text the new name in String
     */
    public void changeNameTo(EventSeries currentEventSeries, String text) {
        currentEventSeries.changeNameTo(text);
    }

    /**
     * This method removes an event series from the calendar
     * @param currentEventSeries the target event series
     */
    public void deleteEventSeries(EventSeries currentEventSeries) {
        this.series.removeIf(i -> currentEventSeries.getSeriesName().equals(i.getSeriesName()));
    }

    /**
     * This method merges several calendar into a common one to let the user see the entire schedule.
     * @param calendars the List of calendars going to be merged
     * @return a Calendar that contains all the events of the merged calendars
     */
    static public Calendar mergeCalendars(List<Calendar> calendars) {
        TreeSet<Event> pastEvents = new TreeSet<>();
        TreeSet<Event> ongoingEvents = new TreeSet<>();
        TreeSet<Event> futureEvents = new TreeSet<>();
        Map<String, Event> events = new HashMap<>();
        List<EventSeries> series = new ArrayList<>();
        List<NoteManager> tagManagers = new ArrayList<>();
        List<NoteManager> memoManagers = new ArrayList<>();
        for (Calendar calendar : calendars) {
            pastEvents.addAll(calendar.getEvents(Time.PAST));
            ongoingEvents.addAll(calendar.getEvents(Time.ONGOING));
            futureEvents.addAll(calendar.getEvents(Time.FUTURE));
            events.putAll(calendar.getEventsMapping());
            series.addAll(calendar.getAllEventSeries());
            tagManagers.add(calendar.getManager("tag"));
            memoManagers.add(calendar.getManager("memo"));
        }
        NoteManager tagManager = Calendar.mergeNoteManager(tagManagers);
        NoteManager memoManager = Calendar.mergeNoteManager(memoManagers);
        return new Calendar(" ", pastEvents, ongoingEvents,
                futureEvents, events, series, tagManager, memoManager);
    }

    /**
     * This method would returns all the events that is corresponded with a memo
     * @param memo the target memo
     * @return a List of Events
     */
    public List<Event> getEventsOfMemo(Memo memo) {
        if(memoManager.getNotes().get(memo) != null) return memoManager.getNotes().get(memo);
        else{return new ArrayList<>();}
    }


    /**
     * This method return the name of currentEventSeries.
     * @param currentEventSeries a series
     * @return the name of currentEventSeries
     */
    public String getSeriesName(EventSeries currentEventSeries) {
        return currentEventSeries.getSeriesName();
    }

    /**
     * get related events for a given tag
     * @param currentTag the tag that is given to do search
     * @return a list of events that related to the given tag
     */
    public List<Event> getEventsOfTag(Tag currentTag) {
        List<Event> result = tagManager.getNotes().get(currentTag);
        if(result != null) return result;
        else{return new ArrayList<>();}
    }

    /**
     * set the information of the given alert
     *
     * @param a the alert to be changed
     * @param name the new alert message
     * @param dt the new alert time
     */
    public void setAlertInfo(Alert a, String name, LocalDateTime dt){
        a.setAlertDateTime(dt);
        a.setAlertName(name);
    }
}
