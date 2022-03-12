package UI.add_UI.add_to_series;

import UI.add_UI.add_memo.CreateMemoController;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * This is the controller of the add event to series panel for GUI
 */
public class AddToSeriesController {
    @FXML
    private ListView<String> listView;

    private CalendarSystem sys = CalendarSystem.getInstance();

    /**
     * This method set up the first look of the panel
     */
    public void initialize(){
        listView.setItems(FXCollections.observableArrayList(sys.getAllEventSeriesNames()));
    }

    @FXML
    private void confirm(){
        List<String> list = listView.getSelectionModel().getSelectedItems();
        for(String i: list){
            sys.addIntoSeries(i);
        }
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(AddToSeriesController.class.getResource("AddToSeries.fxml")));
    }
}
