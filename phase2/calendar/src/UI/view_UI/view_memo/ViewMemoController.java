package UI.view_UI.view_memo;

import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewMemoController {
    @FXML
    public ListView<String> listView;
    @FXML
    private TextArea contentTA;
    @FXML
    private Button confirm;

    private CalendarSystem sys = CalendarSystem.getInstance();

    public void initialize(){
        sys.getEventsOfCurrentMemo();
        listView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        contentTA.setText(sys.getCurrentMemoInfo());
        confirm.setDisable(true);
        contentTA.setDisable(true);
    }

    @FXML
    private void edit(){
        contentTA.setDisable(false);
        confirm.setDisable(false);
        confirm.setText("Confirm");
        confirm.setStyle("-fx-background-color: #e4e4e4");
    }

    @FXML
    private void confirm(){
        sys.editCurrentMemo(1, contentTA.getText());
        sys.getEventsOfCurrentMemo();
        listView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        contentTA.setDisable(true);
        confirm.setDisable(true);
        confirm.setStyle("-fx-background-color: transparent");
        confirm.setText("");
    }

    @FXML
    private void delete(){
        sys.deleteCurrentMemo(1);
        Stage stage = (Stage) contentTA.getScene().getWindow();
        stage.close();
    }

}
