package UI.add_UI.add_frequency_alert;

import UI.add_UI.add_memo.CreateMemoController;
import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This is the controller of the create multiple alerts panel for GUI
 */
public class CreateFrequencyAlertController {
    @FXML
    private DatePicker FreDP;
    @FXML
    private TextField FreHour;
    @FXML
    private TextField FreMin;
    @FXML
    private TextField FreSec;
    @FXML
    private TextField freqAlertName;
    @FXML
    private TextField DurationDays;
    @FXML
    private TextField DurationMin;
    @FXML
    private TextField DurationHours;
    @FXML
    private Label FreStatus;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void confirmFrequencyAlert(){
        try{
            LocalDateTime newDT = LocalDateTime.of(FreDP.getValue(), LocalTime.of(Integer.parseInt(FreHour.getText()), Integer.parseInt(FreMin.getText()), Integer.parseInt(FreSec.getText())));
            Duration duration = Duration.ofDays(Integer.parseInt(DurationDays.getText())).plusHours(Integer.parseInt(DurationHours.getText())).plusMinutes(Integer.parseInt(DurationMin.getText()));
            if(sys.setAlert(freqAlertName.getText(), newDT, duration) == 1){
                Stage stage = (Stage) FreDP.getScene().getWindow();
                stage.close();
            }else{
                FreStatus.setText("Creation Fail");
            }
        }catch (Exception e){
            FreStatus.setText("INVALID INPUT!");
        }
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(CreateFrequencyAlertController.class.getResource("CreateFrequencyAlert.fxml")));
    }
}
