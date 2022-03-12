package UI.ring_alerts;

import calendar_simulation.CalendarSystem;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

/**
 * This class is responsible for triggering and stopping the alarms
 */
public class RingAlerts {

    private static final Duration dt = Duration.seconds(5);
    private static CalendarSystem sys = CalendarSystem.getInstance();
    private static Timeline alarm = new Timeline(new KeyFrame(dt, e -> {
        if (sys.isLoggedIn()) {
            String a = sys.getNextAlert();
            if (a != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, a);
                alert.show();
            }
        }
    }));

    static {
        alarm.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * This method triggers the alarm
     */
    public static void start() {
        alarm.play();
    }

    /**
     * This method stops the alarm
     */
    public static void end() {
        alarm.stop();
    }
}
