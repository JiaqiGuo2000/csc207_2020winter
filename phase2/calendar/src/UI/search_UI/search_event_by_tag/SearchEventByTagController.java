package UI.search_UI.search_event_by_tag;

import UI.time_magic.TimeMagic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import calendar_simulation.CalendarSystem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A class that is responsible for the GUI panel of searching event by tag.
 */
public class SearchEventByTagController {
    @FXML
    public Label status;
    @FXML
    private TextField targetName;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void searchEventByTag() throws Exception{
        String TN = this.targetName.getText();
        if(sys.searchByTag(TN) == 1){
            sys.selectTag(TN);
            status.setText("");
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../../view_UI/view_tag/ViewTagEntirely.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.initOwner(targetName.getScene().getWindow());
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
            targetName.clear();
        }else{
            status.setText("No Tag Found");
        }
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(SearchEventByTagController.class.getResource("SearchEventByTag.fxml")));
    }
}
