package UI.time_magic;

import clock.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the controller of the time magic panel for GUI.
 */
public class TimeMagic {

    @FXML private Label currentTime;
    @FXML private TextField speed;
    @FXML private TextField target;

    @FXML private void setSpeed() {
        double v = Double.parseDouble(speed.getText());
        if (v > 0) {
            Clock.setSpeed(v);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "The speed should be no less than 0.");
            a.show();
        }
    }

    @FXML private void jumpTo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime t = LocalDateTime.parse(target.getText(), formatter);
        if (t.isAfter(Clock.getTime().plusSeconds(1))) {
            Clock.jumpTo(t);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Time does not flow backward.");
            a.show();
        }
    }

    @FXML private void initialize() {
        initCurrentTime();
        initSpeed();
    }

    // https://stackoverflow.com/a/52785067
    private void initCurrentTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            currentTime.setText(Clock.getTime().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void initSpeed() {
        speed.setText(Double.toString(Clock.getSpeed()));
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(TimeMagic.class.getResource("TimeMagic.fxml")));
    }
}
