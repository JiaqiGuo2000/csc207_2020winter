package UI.view_UI.view_all_memos;

import UI.time_magic.TimeMagic;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewAllMemosController {
    @FXML
    public ListView<String> listView;

    private CalendarSystem sys = CalendarSystem.getInstance();

    public void initialize(){
        listView.setItems(FXCollections.observableArrayList(sys.getCurrentMemoListInfo()));
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(listView.getSelectionModel().getSelectedItem() != null) {
                    int index = listView.getSelectionModel().getSelectedIndex();
                    sys.selectMemo(index);
                    try {
                        viewSpecificMemo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void viewSpecificMemo() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../view_memo/ViewMemoEntirely.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initOwner(listView.getScene().getWindow());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        sys.viewAllMemos();
        listView.setItems(FXCollections.observableArrayList(sys.getCurrentMemoListInfo()));
    }

    /**
     * This is the getter of this panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    static public Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(ViewAllMemosController.class.getResource("ViewAllMemos.fxml")));
    }
}
