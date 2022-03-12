package UI.messaging;

import calendar_simulation.CalendarSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller of the receive message panel for GUI.
 */
public class ViewMessage {
    @FXML private GridPane gridPane;
    @FXML private Label sender;
    @FXML private Label event;
    @FXML private Label message;
    private static final int eventRow = 1;

    /**
     * This method sets up the first look of the panel
     * @param sender the sender of the message
     * @param event the event attached with this message
     * @param message the content of the message
     */
    public void set(String sender, String event, String message) {
        this.sender.setText(sender);
        this.event.setText(event);
        this.message.setText(message);
    }

    /**
     * This method removes the event row.
     */
    public void removeEventRow() {
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == eventRow);
        for (Node child: gridPane.getChildren()) {
            int r = GridPane.getRowIndex(child);
            if (r > eventRow) {
                GridPane.setRowIndex(child, r - 1);
            }
        }
    }

    @FXML private void reply() throws IOException {
        Stage stage = new Stage();
        Scene scene = SendMessage.getScene(this.sender.getText());
        stage.setScene(scene);
        stage.setTitle("Reply");
        stage.show();
    }

    @FXML private void accept(ActionEvent e) {
        CalendarSystem calendarSystem = CalendarSystem.getInstance();
        int ret = calendarSystem.addUserToEvent(calendarSystem.getUsername(), event.getText());
        if (ret == -1) {
            Alert a = new Alert(Alert.AlertType.ERROR, "The event no longer exists.");
            a.show();
        }
        Button button = (Button) e.getSource();
        button.setVisible(false);
    }
}
