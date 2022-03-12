package backend_system.Managers;

import backend_system.Entities.Alert;
import clock.Clock;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A class contains all the alert created by the user the manipulates alerts.
 */
public class AlertManager implements Serializable {
    private TreeSet<Alert> alst = new TreeSet<Alert>();
    private TreeSet<Alert> history = new TreeSet<Alert>();


    /**
     * create several frequency alert with given information
     *
     * @param name           the name of the alert
     * @param dt             the triggered time of the event
     * @param untilWhen      the time when the last alert could possibly be triggered
     * @param temporalAmount the number of hours between two consecutive alerts
     */
    public void createFrequencyAlert(String name, LocalDateTime dt, LocalDateTime untilWhen, int temporalAmount) {
        while (dt.isBefore(untilWhen)) {
            createNormalAlert(name, dt);
            dt = dt.plusHours(temporalAmount);
        }
    }

    /**
     * show all the alerts have triggered in the past
     *
     * @return a list of Alert
     */
    public List<Alert> getPastAlertList() {
        List<Alert> a = new ArrayList(this.history);
        return a;
    }

    /**
     * show all the alert created
     *
     * @return a list of Alert
     */
    public ArrayList<Alert> getAlertList() {
        return new ArrayList<Alert>(this.alst);
    }

    /**
     * create an alert wich given information
     *
     * @param name the name of the alert
     * @param dt   the triggered time of the alert
     */
    public void createNormalAlert(String name, LocalDateTime dt) {
        if (Clock.getTime().isBefore(dt)) {
            this.alst.add(new Alert(name, dt));
        } else {
            System.out.println("The alert time should be in the future.");
        }
    }

    /**
     * remove the given alert
     *
     * @param a the alert to be removed
     */
    public void deleteAlert(Alert a) {
        alst.remove(a);
    }

    /**
     * trigger the alert if the triggered time of an alert has come
     */
    public void ring() {
        while (!alst.isEmpty()) {
            Alert a = alst.first();
            if (a.ring())
                history.add(alst.pollFirst());
            else
                break;
        }
    }

    public static class MemoManager extends NoteManager {
        // Can be able to extend more functionality
    }
}
