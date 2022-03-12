package UI.add_UI.add_alert;

import UI.add_UI.add_tag.CreateTagController;
import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This is the controller of the create single alert panel for GUI
 */
public class CreateAlertController {

    @FXML
    private DatePicker DP;
    @FXML
    private TextField Hour;
    @FXML
    private TextField Min;
    @FXML
    private TextField Sec;
    @FXML
    private Label status;
    @FXML
    private TextField alertName;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void confirmAlert(){
        try{
            LocalDateTime newDT = LocalDateTime.of(DP.getValue(), LocalTime.of(Integer.parseInt(Hour.getText()), Integer.parseInt(Min.getText()), Integer.parseInt(Sec.getText())));
            if(sys.setAlert(alertName.getText(), newDT) == 1){
                Stage stage = (Stage) DP.getScene().getWindow();
                stage.close();
            }else{
                status.setText("Creation Fail");
            }
        }catch (Exception e){
            status.setText("INVALID INPUT!");
        }
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(CreateAlertController.class.getResource("CreateAlert.fxml")));
    }
}
