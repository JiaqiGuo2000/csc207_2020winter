package UI.create_UI.create_event;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This is the controller of the create event panel for GUI.
 */
public class CreateEventController {
    @FXML
    private DatePicker startDay;
    @FXML
    private DatePicker endDay;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField startHour;
    @FXML
    private TextField startMin;
    @FXML
    private TextField startSec;
    @FXML
    private TextField endHour;
    @FXML
    private TextField endMin;
    @FXML
    private TextField endSec;
    @FXML
    private Button createButton;
    @FXML
    private Label status;
    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void createEvent(){
        try{
            String name = nameTF.getText();
            LocalDate startDate = startDay.getValue();
            LocalTime startTime = LocalTime.of(Integer.parseInt(startHour.getText()), Integer.parseInt(startMin.getText()), Integer.parseInt(startSec.getText()));
            LocalDate endDate = endDay.getValue();
            LocalTime endTime = LocalTime.of(Integer.parseInt(endHour.getText()), Integer.parseInt(endMin.getText()),  Integer.parseInt(endSec.getText()));

            sys.createEvent(name, LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime));

            Stage stage = (Stage) this.createButton.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
            status.setText("INVALID INPUT!");
        }
    }
}
