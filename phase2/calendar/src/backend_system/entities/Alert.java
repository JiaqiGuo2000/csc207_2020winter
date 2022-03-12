package backend_system.entities;

import clock.Clock;

import java.awt.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The class Alert represents a notification object to this calendar system.
 */
public class Alert implements Comparable<Alert>, Serializable {
    private String alertName;
    private LocalDateTime alertDateTime;

    /**
     * Creates Alert with its message and the time it alerts.
     *
     * @param name the message of this alert.
     * @param dt   the date and time that it alerts.
     */
    public Alert(String name, LocalDateTime dt) {
        this.alertName = name;
        this.alertDateTime = dt;
    }

    /**
     * Check if the the time for this alert is up.
     *
     * @return whether the time for this alert is up or not
     */
    public boolean timeIsUp() {
        LocalDateTime t = Clock.getTime();
        return alertDateTime.isBefore(t) || alertDateTime.isEqual(t);
    }

    /**
     * Ring if the time for this alert is up.
     *
     * @return where or not current time matches the alert time
     */
    public boolean ring() {
        boolean b = timeIsUp();
        if (b) {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Ding! " + this);
        }
        return b;
    }

    /**
     * Returns the alert message of this Alert.
     *
     * @return the message of this alert.
     */
    public String getAlertName() {
        return this.alertName;
    }

    /**
     * Returns the time and date of this alert.
     *
     * @return the a LocalDateTime object that represents date and time of this alert.
     */
    public LocalDateTime getAlertDateTime() {
        return this.alertDateTime;
    }

    /**
     * Compare two Alerts objects for ordering.
     *
     * @param o the Alert to be compared.
     * @return 0 if the argument Alert is equal to this Alert; -1 if this Alert is before the argument Alert;
     * 1 if this alert is after the argument Alert.
     */
    @Override
    public int compareTo(Alert o) {
        return alertDateTime.compareTo(o.getAlertDateTime());
    }

    /**
     * Return the alert message with the date and time as a string.
     *
     * @return a string that contains both this Alert's name and date and time.
     */
    @Override
    public String toString() {
        return this.alertName + " at " + alertDateTime.toString();
    }

    /**
     * The new overriding version of equals. It compares the time of two Alerts.
     * @param o The compared alert
     * @return true if time is the same
     *          false if time is different
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Alert) {
            Alert a = (Alert) o;
            return alertName.equals(a.getAlertName()) && alertDateTime.equals(a.getAlertDateTime());
        }
        return false;
    }

    /**
     * The new overriding version of hashCode.
     * @return the hash outcome
     */
    @Override
    public int hashCode() {
        return Objects.hash(alertName, alertDateTime);
    }

    /**
     * The new overriding version of equals. It compares the time of two Alerts.
     * @param name the new message for this alert
     */
    public void setAlertName(String name){this.alertName = name;}

    /**
     * The new overriding version of equals. It compares the time of two Alerts.
     * @param dt the new time of the alert
     */
    public void setAlertDateTime(LocalDateTime dt){alertDateTime = dt;}
}
