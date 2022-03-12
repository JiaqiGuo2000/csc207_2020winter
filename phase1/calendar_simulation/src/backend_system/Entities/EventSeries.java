package backend_system.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * The class EventSeries represents a series of events with a specified name.
 */
public class EventSeries implements Serializable {
    private TreeSet<Event> seriesEvents = new TreeSet<>();
    private String seriesName;

    /**
     * Create a series representation with its specified name.
     *
     * @param name the name of this series.
     */
    public EventSeries(String name) {
        seriesName = name;
    }

    /**
     * Create a event with its event name and the start/end datetime.
     *
     * @param name          the name of the events.in this series.
     * @param startDateTime the start date and time of this series.
     * @param endDateTime   the end date and time of this series.
     * @param duration      the time between two in event in this series in days.
     * @param number        the number of events in this series.
     */
    public EventSeries(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, String seriesName, int duration, int number) {
        this.seriesName = seriesName;
        int i = 0;
        while (i < number) {
            this.seriesEvents.add(new Event(name + " " + (i + 1), startDateTime, endDateTime));
            startDateTime = startDateTime.plusDays(duration);
            endDateTime = endDateTime.plusDays(duration);
            i++;
        }
    }

    /**
     * Return all the events in this series.
     *
     * @return a list of events in this series.
     */
    public List<Event> getEventList() {
        return new ArrayList<>(seriesEvents);
    }

    /**
     * Add a Event to this series of events
     *
     * @param event the event to be added in this series.
     */
    public void addEvent(Event event) {
        seriesEvents.add(event);
    }

    /**
     * Return series name of this series.
     *
     * @return the name of this series.
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * Return all the events in this series.
     *
     * @return a TreeSet of all the events in this series.
     */
    public TreeSet<Event> getSeries() {
        return seriesEvents;
    }

//    public void setSeriesName(String name){
//        seriesName = name;
//    }
}
