package UI.main_window;

import UI.messaging.SendMessage;
import UI.ring_alerts.RingAlerts;
import UI.search_UI.search_event_by_date.SearchEventByDateController;
import UI.search_UI.search_event_by_name.SearchEventByNameController;
import UI.search_UI.search_event_by_tag.SearchEventByTagController;
import UI.time_magic.TimeMagic;
import UI.view_UI.view_all_memos.ViewAllMemosController;
import calendar_simulation.CalendarSystem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller of the main panel for GUI.
 */
public class MainWindowController {
    @FXML
    private Label status;
    @FXML
    private Tab optionTabPane;
    @FXML
    private Button addSeries;
    @FXML
    private ImageView logOffButton;
    @FXML
    private ListView<String> seriesListView;
    @FXML
    private ListView<String> eventsListView;
    @FXML
    private Button addEventButton;
    @FXML
    private CheckBox pastCheckbox;
    @FXML
    private CheckBox ongoingCheckbox;
    @FXML
    private CheckBox futureCheckbox;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField newCalendarTF;

    private CalendarSystem sys = CalendarSystem.getInstance();

    private static ArrayList<String> selectedCalendarList = new ArrayList<String>();

    /**
     * This method set up the first look of the panel
     */
    public void initialize(){
        selectedCalendarList.clear();
        List<String> names = sys.getCalendarNamesOfCurrentUser();
        listView.setItems(FXCollections.observableArrayList(names));
        futureCheckbox.setSelected(true);
        ongoingCheckbox.setSelected(true);
        addEventButton.setDisable(true);
        addSeries.setDisable(true);
        optionTabPane.setDisable(true);
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->{
                    if(wasSelected && !isNowSelected) selectedCalendarList.remove(item);
                    else if(!wasSelected && isNowSelected) selectedCalendarList.add(item);
                        sys.updateCurrentCalendar(selectedCalendarList);
                        updateMainGUI();
                        if(selectedCalendarList.size() != 1){
                            status.setText("Read-only");
                            status.setTextFill(Paint.valueOf("#9e1a1a"));
                            addEventButton.setDisable(true);
                            addSeries.setDisable(true);
                            optionTabPane.setDisable(true);
                        }else{
                            status.setText("Operable");
                            status.setTextFill(Paint.valueOf("#00c800"));
                            addEventButton.setDisable(false);
                            addSeries.setDisable(false);
                            optionTabPane.setDisable(false);
                        }
                });
                return observable;
            }}));
        addListenerToCheckbox(pastCheckbox);
        addListenerToCheckbox(ongoingCheckbox);
        addListenerToCheckbox(futureCheckbox);
        eventsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if(eventsListView.getSelectionModel().getSelectedItem() != null) {
                    int index = eventsListView.getSelectionModel().getSelectedIndex();
                    sys.selectEvent(index);
                    try {
                        viewSpecificEvent(status.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        seriesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(seriesListView.getSelectionModel().getSelectedItem() != null){
                String select = seriesListView.getSelectionModel().getSelectedItem();
                sys.selectSeries(select);
                try{ViewSeries(status.getText());}catch (IOException e){e.printStackTrace();}}
            }
        });
        updateMainGUI();
    }

    private void addListenerToCheckbox(CheckBox checkbox){
        checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(oldValue != newValue){
                    updateMainGUI();
                }
            }
    });
    }

    @FXML
    private void createNewCalendar(){
        String name = newCalendarTF.getText();
        sys.createCalendar(name);
        listView.setItems(FXCollections.observableArrayList(sys.getCalendarNamesOfCurrentUser()));
        selectedCalendarList.clear();
        newCalendarTF.clear();
        updateMainGUI();
    }

    private void setSelectedEventsNames(){
        boolean past = pastCheckbox.isSelected();
        boolean ongoing = ongoingCheckbox.isSelected();
        boolean future = futureCheckbox.isSelected();
        sys.chooseWhatEventsToDisplay(past, ongoing, future);
    }

    @FXML
    private void viewEvents(){
        if(selectedCalendarList.size() != 0) {
            eventsListView.setItems(FXCollections.observableArrayList(sys.getCurrentEventListName()));
        }
    }

    @FXML
    private void viewSeries(){
        List<String> l = sys.getAllEventSeriesNames();
        if(l != null){
            seriesListView.setItems(FXCollections.observableArrayList(l));
        }
    }

    @FXML
    private void updateMainGUI(){
        if(sys.currentCalendarIsNotNull())
        { setSelectedEventsNames();
        viewEvents();
        viewSeries();}else{
            eventsListView.setItems(FXCollections.observableArrayList());
            seriesListView.setItems(FXCollections.observableArrayList());
        }
        eventsListView.getSelectionModel().clearSelection();
        seriesListView.getSelectionModel().clearSelection();
    }


    @FXML
    private void createNewEvent() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../create_UI/create_event/CreateEvent.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initOwner(logOffButton.getScene().getWindow());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void ViewSeries(String text) throws IOException {
        sys.searchBySeriesName(seriesListView.getSelectionModel().getSelectedItem());
        Stage primaryStage = new Stage();
        Parent root;
        if(text.equals("Read-only"))root = FXMLLoader.load(getClass().getResource("../view_UI/view_series/ViewSeriesNotEditable.fxml"));
        else root = FXMLLoader.load(getClass().getResource("../view_UI/view_series/ViewSeries.fxml"));
        primaryStage.setScene(new Scene(root, 504, 349));
        primaryStage.initOwner(logOffButton.getScene().getWindow());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void createNewSeries() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../create_UI/create_series/CreateSeries.fxml"));
        primaryStage.setScene(new Scene(root, 600, 452));
        primaryStage.initOwner(logOffButton.getScene().getWindow());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void viewSpecificEvent(String text) throws IOException{
        Stage primaryStage = new Stage();
        Parent root;
        if(text.equals("Read-only")){
            root = FXMLLoader.load(getClass().getResource("../view_UI/view_event/ViewEventNotEditable.fxml"));
        }else{
        root = FXMLLoader.load(getClass().getResource("../view_UI/view_event/ViewEvent.fxml"));}
        primaryStage.setScene(new Scene(root));
        primaryStage.initOwner(logOffButton.getScene().getWindow());
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void logOff() throws IOException{
        sys.logoff();
        RingAlerts.end();
        Stage stage = (Stage)addEventButton.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
        primaryStage.setScene(new Scene(root, 647, 426));
        primaryStage.show();
    }

    @FXML
    private void searchEventByDate() throws IOException{
        Stage stage = new Stage();
        Scene scene = SearchEventByDateController.getScene();
        stage.setScene(scene);
        stage.setTitle("Search Event By Date");
        stage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void searchEventByName() throws IOException{
        Stage stage = new Stage();
        Scene scene = SearchEventByNameController.getScene();
        stage.setScene(scene);
        stage.setTitle("Search Event By Name");
        stage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void searchEventByTag() throws IOException{
        Stage stage = new Stage();
        Scene scene = SearchEventByTagController.getScene();
        stage.setScene(scene);
        stage.setTitle("Search Event By Tag");
        stage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void viewAllMemos() throws IOException{
        sys.viewAllMemos();
        Stage stage = new Stage();
        Scene scene = ViewAllMemosController.getScene();
        stage.setScene(scene);
        stage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void timeMagic() throws IOException {
        Stage stage = new Stage();
        Scene scene = TimeMagic.getScene();
        stage.setScene(scene);
        stage.setTitle("Time Magic");
        stage.showAndWait();
        updateMainGUI();
    }

    @FXML
    private void sendMessage() throws IOException {
        Stage stage = new Stage();
        Scene scene = SendMessage.getScene();
        stage.setScene(scene);
        stage.setTitle("Send Message");
        stage.showAndWait();
    }

    /*private int ensureNotEmpty() {
        if (selectedCalendarList.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Please select at least one calendar.");
            a.show();
            return -1;
        }
        return 1;
    }*/
}

