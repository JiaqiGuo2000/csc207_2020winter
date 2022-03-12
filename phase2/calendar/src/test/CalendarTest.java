package test;

import backend_system.Calendar;
import backend_system.entities.Event;
import clock.Clock;
import clock.Time;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a test for calendar
 */
public class CalendarTest {

    /**
     * testing code
     * @param args the argument
     */
    static public void main(String[] args) {
        Calendar em = new Calendar("");

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
                    Event e = new Event(i + ", " + j, t[i], t[j]);
                    es.add(e);
                }
            }
        }
        Collections.shuffle(es);
        for (Event e : es) em.addEvent(e);

        for (int i = 0; i < n; ++i, c = c.plusDays(Math.random() < 0.5 ? 1 : 2)) {
            Clock.jumpTo(c);
            List<Event> past = new ArrayList<>(), ongoing = new ArrayList<>(), future = new ArrayList<>();
            for (Event e : es) {
                int cmp = e.ifInProgress(c);
                (cmp == 1 ? future : (cmp == 0 ? ongoing : past)).add(e);
            }
            Collections.sort(past);
            Collections.sort(future);
            assert past.equals(em.getEvents(Time.PAST));
            assert future.equals(em.getEvents(Time.FUTURE));
        }
        System.out.println("Pass");
    }
}
