package UI.create_UI.create_series;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This is the controller of the create series panel for GUI.
 */
public class CreateSeriesController {
    @FXML
    public DatePicker endDay;
    @FXML
    public DatePicker startDay;
    @FXML
    private Button createButton;
    @FXML
    private Label status;
    @FXML
    private TextField endMin;
    @FXML
    private TextField endSec;
    @FXML
    private TextField endHour;
    @FXML
    private TextField startSec;
    @FXML
    private TextField startHour;
    @FXML
    private TextField startMin;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField nameOfSeries;
    @FXML
    private TextField frequencyDays;
    @FXML
    private TextField frequencyHours;
    @FXML
    private TextField Repeat;


    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void createSeries(){
        String name = nameTF.getText();
            LocalDate startDate = startDay.getValue();
            String startHour = this.startHour.getText();
            String startMin = this.startMin.getText();
            String startSec = this.startSec.getText();
            LocalDate endDate = endDay.getValue();
            String endHour = this.endHour.getText();
            String endMin = this.endMin.getText();
            String endSec = this.endSec.getText();
            String seriesName = nameOfSeries.getText();
            String freDays = frequencyDays.getText();
            String freHours = frequencyHours.getText();
            String repeat = Repeat.getText();

            try{
                sys.createEvent(name, LocalDateTime.of(startDate, LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMin), Integer.parseInt(startSec))),
                        LocalDateTime.of(endDate, LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMin), Integer.parseInt(endSec))), seriesName, Duration.ofDays(Integer.parseInt(freDays)).plusHours(Integer.parseInt(freHours)), Integer.parseInt(repeat));
                Stage stage = (Stage) this.createButton.getScene().getWindow();
                stage.close();
            }catch (Exception e1){
                status.setText("Invalid input");
            }
        }
}
