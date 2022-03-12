package UI.add_UI.add_tag;

import UI.time_magic.TimeMagic;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * This is the controller for create tag panel for GUI
 */
public class CreateTagController {

    @FXML
    private ListView<String> TagListView;
    @FXML
    private TextField NewTag;


    private CalendarSystem sys = CalendarSystem.getInstance();

    /**
     * This method sets up the first look of panel
     */
    public void initialize(){
        TagListView.setItems(FXCollections.observableArrayList(sys.getContentOfAllTags()));
    }

    @FXML
    private void confirmTag(){
        String s = TagListView.getSelectionModel().getSelectedItem();
        String newName = NewTag.getText();
        sys.addTagToEvent(newName);
        if (s != null)
            sys.addTagToEvent(s);
        Stage stage = (Stage) NewTag.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(CreateTagController.class.getResource("CreateTag.fxml")));
    }
}
