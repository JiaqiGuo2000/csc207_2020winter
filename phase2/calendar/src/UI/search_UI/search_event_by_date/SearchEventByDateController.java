package UI.search_UI.search_event_by_date;

import UI.search_UI.search_event_by_name.SearchEventByNameController;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

/**
 * A class that is responsible for the GUI panel of searching event by date.
 */
public class SearchEventByDateController {

    @FXML
    private ListView<String> listView;
    @FXML
    private DatePicker targetDate;
    @FXML
    private Label status;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void searchEventByDate() {
        if(sys.searchByDate(targetDate.getValue()) == 1){
            status.setText("");
            listView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        }
        else{
            status.setText("No Result Found");
        }
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(SearchEventByDateController.class.getResource("SearchEventByDate.fxml")));
    }

}