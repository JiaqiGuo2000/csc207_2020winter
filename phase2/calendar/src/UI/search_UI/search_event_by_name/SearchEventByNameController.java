package UI.search_UI.search_event_by_name;

import UI.search_UI.search_event_by_tag.SearchEventByTagController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import calendar_simulation.CalendarSystem;

import java.io.IOException;

/**
 * A class that is responsible for the GUI panel of searching event by name.
 */
public class SearchEventByNameController {
    @FXML
    public ListView<String> listView;
    @FXML
    public Label status;
    @FXML
    private TextField targetName;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void searchEventByName() {
        String targetName = this.targetName.getText();
        if(sys.searchByName(targetName) == 1){
            status.setText("");
            listView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        }else{
            status.setText("No Result Found");
        }
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(SearchEventByNameController.class.getResource("SearchEventByName.fxml")));
    }
}
