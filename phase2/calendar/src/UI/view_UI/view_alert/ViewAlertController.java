package UI.view_UI.view_alert;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ViewAlertController {
    @FXML
    public TextField alertName;
    @FXML
    public Button confirm;
    @FXML
    public DatePicker DP;
    @FXML
    public TextField Hour;
    @FXML
    public TextField Min;
    @FXML
    public TextField Sec;

    private CalendarSystem sys = CalendarSystem.getInstance();

    public void initialize(){
        alertName.setText(sys.getCurrentAlertName());
        LocalDate date = sys.getCurrentAlertTime().toLocalDate();
        LocalTime time = sys.getCurrentAlertTime().toLocalTime();
        DP.setValue(date);
        Hour.setText(time.format(DateTimeFormatter.ofPattern("HH")));
        Min.setText(time.format(DateTimeFormatter.ofPattern("mm")));
        Sec.setText(time.format(DateTimeFormatter.ofPattern("ss")));
        turnOnTurnOff(true);
    }

    @FXML
    private void edit(){
        turnOnTurnOff(false);
        alertName.setStyle("-fx-background-color: white");
    }

    @FXML
    private void confirm(){
        LocalDateTime newDT = LocalDateTime.of(DP.getValue(), LocalTime.of(Integer.parseInt(Hour.getText()), Integer.parseInt(Min.getText()), Integer.parseInt(Sec.getText())));
        sys.editCurrentAlert(alertName.getText(), newDT);
        turnOnTurnOff(true);
        alertName.setStyle("-fx-background-color: transparent");
    }

    @FXML
    private void delete(){
        sys.deleteCurrentAlert();
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

    private void turnOnTurnOff(boolean a){
        confirm.setVisible(!a);
        confirm.setDisable(a);
        alertName.setDisable(a);
        DP.setDisable(a);
        Hour.setDisable(a);
        Min.setDisable(a);
        Sec.setDisable(a);
    }
}
