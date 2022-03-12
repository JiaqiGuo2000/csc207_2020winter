package UI.view_UI.view_series;

import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewSeriesNotEditableController {
    @FXML
    public TextField nameTF;
    @FXML
    public ListView<String> listView;
    @FXML
    public Button confirm;
    @FXML
    public Button editButton;
    @FXML
    public Button delete;

    private CalendarSystem sys = CalendarSystem.getInstance();

    public void initialize(){
        listView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        nameTF.setText(sys.getCurrentSeriesName());

        confirm.setDisable(true);
        nameTF.setDisable(true);
        editButton.setDisable(true);
        delete.setDisable(true);
    }

    @FXML
    private void edit(){
        nameTF.setDisable(false);
        confirm.setDisable(false);
        confirm.setText("Confirm");
        confirm.setStyle("-fx-background-color: #e4e4e4");
    }

    @FXML
    private void confirm(){
        sys.editCurrentSeries(nameTF.getText());
        nameTF.setDisable(true);
        confirm.setDisable(true);
        confirm.setStyle("-fx-background-color: transparent");
        confirm.setText("");
    }

    @FXML
    private void delete(){
        sys.deleteCurrentSeries();
        Stage stage = (Stage) nameTF.getScene().getWindow();
        stage.close();
    }
}
