package backend_system;

import backend_system.entities.Alert;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class represents the alarm set by a user.
 */
public class Alarm implements Serializable, Iterator<Alert> {
    private PriorityQueue<Alert> as = new PriorityQueue<>();

    /**
     * This is the new overriding version of hasNext for iteration.
     * @return true if has another alarm after this one
     *          false if no other alarm after this one
     */
    @Override
    public boolean hasNext() {
        return !as.isEmpty() && as.peek().timeIsUp();
    }

    /**
     * This is the new overriding version of next for iteration.
     * @return the alarm after this one
     */
    @Override
    public Alert next() {
        if (hasNext())
            return as.poll();
        throw new NoSuchElementException();
    }

    /**
     * This method sets up a new alarm.
     * @param name the name of the alarm
     * @param time the triggered time of the alarm
     */
    public void add(String name, LocalDateTime time) {
        as.add(new Alert(name, time));
    }

    /**
     * This method sets up multiple new alarms.
     * @param name the name of the alarms
     * @param from the triggered time of the first alarm
     * @param to the triggered time of the last alarm
     * @param interval the time interval between two alarms
     */
    public void add(String name, LocalDateTime from, LocalDateTime to, Duration interval) {
        while (from.isBefore(to)) {
            as.add(new Alert(name, from));
            from = from.plus(interval);
        }
    }

    /**
     * This method removes an alarm.
     * @param a the target alarm
     */
    public void remove(Alert a) {
        as.remove(a);
    }
}
