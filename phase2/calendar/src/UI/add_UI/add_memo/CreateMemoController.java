package UI.add_UI.add_memo;

import UI.add_UI.add_alert.CreateAlertController;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CreateMemoController {
    @FXML
    private ListView<String> listView;
    @FXML
    private TextArea contentTA;
    @FXML
    private Button confirm;

    private CalendarSystem sys = CalendarSystem.getInstance();

    public void initialize(){
        listView.setItems(FXCollections.observableArrayList(sys.getContentOfAllMemos()));
    }

    @FXML
    private void confirmMemo(){
        String s = listView.getSelectionModel().getSelectedItem();
        String newName = contentTA.getText();
        sys.addNewMemo(newName);
        if (s != null)
            sys.addNewMemo(s);
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(CreateMemoController.class.getResource("CreateMemo.fxml")));
    }
}
