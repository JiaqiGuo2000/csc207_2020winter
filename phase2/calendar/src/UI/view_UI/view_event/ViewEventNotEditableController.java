package UI.view_UI.view_event;

import UI.add_UI.add_alert.CreateAlertController;
import UI.add_UI.add_frequency_alert.CreateFrequencyAlertController;
import UI.add_UI.add_memo.CreateMemoController;
import UI.add_UI.add_tag.CreateTagController;
import UI.add_UI.add_to_series.AddToSeriesController;
import calendar_simulation.CalendarSystem;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ViewEventNotEditableController {
        @FXML
        public Button confirm;
        @FXML
        public TextField EHour;
        @FXML
        public TextField EMin;
        @FXML
        public TextField ESec;
        @FXML
        public DatePicker startDP;
        @FXML
        public TextField SHour;
        @FXML
        public TextField SMin;
        @FXML
        public TextField SSec;
        @FXML
        public Label Warning;
        @FXML
        public ToolBar toolBar;
    @FXML
        private ListView<String> memoListView;
        @FXML
        private ListView<String> tagListView;
        @FXML
        private ListView<String> alertListView;
        @FXML
        private TextField nameTF;
        @FXML
        private DatePicker endDP;


        private CalendarSystem sys = CalendarSystem.getInstance();


        public void initialize(){
            updateGUI();
            toolBar.setDisable(true);
        }

        @FXML
        private void edit(){
            nameTF.setDisable(false);
            startDP.setDisable(false);
            endDP.setDisable(false);
            confirm.setStyle("-fx-fill: #e4e4e4");
            confirm.setText("Confirm");
            confirm.setDisable(false);
            SHour.setDisable(false);
            SMin.setDisable(false);
            SSec.setDisable(false);
            EHour.setDisable(false);
            EMin.setDisable(false);
            ESec.setDisable(false);
        }

        @FXML
        private void confirm(){
            try{

                String name = nameTF.getText();
                int flag = 1;
                if (!name.equals(sys.getCurrentEventName()))
                    flag = sys.editEventName(name);
                if (flag == -1)
                    Warning.setText("INVALID INPUT!");
                LocalDateTime endTime = handleEndTime();
                LocalDateTime startTime = handleStartTime();
                if(sys.editEventTime(endTime, startTime) == 1 && flag==1){
                    nameTF.setDisable(true);
                    startDP.setDisable(true);
                    endDP.setDisable(true);
                    confirm.setDisable(true);
                    SHour.setDisable(true);
                    SMin.setDisable(true);
                    SSec.setDisable(true);
                    EHour.setDisable(true);
                    EMin.setDisable(true);
                    ESec.setDisable(true);
                    confirm.setStyle("-fx-background-color:transparent");
                    confirm.setText("");
                    Warning.setText("");
                }
            }
            catch (Exception e){
                Warning.setText("INVALID INPUT!");
            }
        }

        private LocalDateTime handleStartTime() throws Exception{
            LocalDate startDate = startDP.getValue();
            LocalTime startTime = LocalTime.of(Integer.parseInt(SHour.getText()), Integer.parseInt(SMin.getText()), Integer.parseInt(SSec.getText()));
            return LocalDateTime.of(startDate, startTime);
        }

        private LocalDateTime handleEndTime() throws Exception{
            LocalDate endDate = endDP.getValue();
            LocalTime endTime = LocalTime.of(Integer.parseInt(EHour.getText()), Integer.parseInt(EMin.getText()), Integer.parseInt(ESec.getText()));
            return LocalDateTime.of(endDate, endTime);
        }

        @FXML
        private void addMemo() throws Exception{
            Stage stage = new Stage();
            Scene scene = CreateMemoController.getScene();
            stage.setScene(scene);
            stage.setTitle("Add Memo");
            stage.showAndWait();
            updateGUI();
        }

        @FXML
        private void addToSeries() throws Exception{
            Stage stage = new Stage();
            Scene scene = AddToSeriesController.getScene();
            stage.setScene(scene);
            stage.setTitle("Add To Series");
            stage.showAndWait();
            updateGUI();
        }

        @FXML
        private void addTag() throws Exception{
            Stage stage = new Stage();
            Scene scene = CreateTagController.getScene();
            stage.setScene(scene);
            stage.setTitle("Add Tag");
            stage.showAndWait();
            updateGUI();
        }

        @FXML
        private void addIndividualAlert() throws Exception{
            Stage stage = new Stage();
            Scene scene = CreateAlertController.getScene();
            stage.setScene(scene);
            stage.setTitle("Add Individual Alert");
            stage.showAndWait();
            updateGUI();
        }

        @FXML
        private void addFrequencyAlert() throws Exception{
            Stage stage = new Stage();
            Scene scene = CreateFrequencyAlertController.getScene();
            stage.setScene(scene);
            stage.setTitle("Add Frequency Alert");
            stage.showAndWait();
            updateGUI();
        }


        @FXML
        private void deleteThisEvent(){
            sys.deleteCurrentEvent();
            Stage stage = (Stage) startDP.getScene().getWindow();
            stage.close();
        }

        private void updateGUI(){
            nameTF.setText(sys.getCurrentEventName());
            endDP.setValue(sys.getCurrentEventEndDT().toLocalDate());
            startDP.setValue(sys.getCurrentEventStartDT().toLocalDate());
            LocalTime elt = sys.getCurrentEventEndDT().toLocalTime();
            LocalTime slt = sys.getCurrentEventStartDT().toLocalTime();
            SHour.setText(slt.format(DateTimeFormatter.ofPattern("HH")));
            SMin.setText(slt.format(DateTimeFormatter.ofPattern("mm")));
            SSec.setText(slt.format(DateTimeFormatter.ofPattern("ss")));
            EHour.setText(elt.format(DateTimeFormatter.ofPattern("HH")));
            EMin.setText(elt.format(DateTimeFormatter.ofPattern("mm")));
            ESec.setText(elt.format(DateTimeFormatter.ofPattern("ss")));
            startDP.setDisable(true);
            SHour.setDisable(true);
            SMin.setDisable(true);
            SSec.setDisable(true);
            EHour.setDisable(true);
            EMin.setDisable(true);
            ESec.setDisable(true);
            nameTF.setDisable(true);
            endDP.setDisable(true);
            confirm.setDisable(true);


            memoListView.setItems(FXCollections.observableArrayList(sys.getCurrentMemoListInfo()));
            tagListView.setItems(FXCollections.observableArrayList(sys.getCurrentTagListInfo()));
            alertListView.setItems(FXCollections.observableArrayList(sys.getCurrentAlertListInfo()));

            memoListView.getSelectionModel().clearSelection();
            tagListView.getSelectionModel().clearSelection();
            alertListView.getSelectionModel().clearSelection();
        }

    }
