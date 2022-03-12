package UI.messaging;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * This is the controller of the send message panel for GUI.
 */
public class SendMessage {
    @FXML private TextField receiver;
    @FXML private TextField event;
    @FXML private TextArea message;

    @FXML private void send() {
        String e = event.getText();
        if (e.isEmpty())
            e = null;
        int ret = CalendarSystem.getInstance().sendMessage(receiver.getText(), message.getText(), e);
        if (ret == -1) {
            Alert a = new Alert(Alert.AlertType.ERROR, "The receiver does not exist.");
            a.show();
        } else if (ret == -2) {
            Alert a = new Alert(Alert.AlertType.ERROR, "The event does not exist in the common calendar.");
            a.show();
        }
        Window window = receiver.getScene().getWindow();
        if (window instanceof Stage)
            ((Stage) window).close();
    }

    /**
     * This method sets the default receiver of messaging
     * @param to the name of the receiver in String
     */
    public void setDefaultReceiver(String to) {
        receiver.setText(to);
    }

    /**
     * The getter for the send message panel
     * @return a GUI scene
     * @throws IOException an exception in file I/O
     */
    public static Scene getScene() throws IOException {
        return new Scene(FXMLLoader.load(SendMessage.class.getResource("SendMessage.fxml")));
    }

    /**
     * The getter for the send message panel of a particular receiver
     * @param receiver the name of the target receiver
     * @return a GUi scene
     * @throws IOException an exception in file I/O
     */
    public static Scene getScene(String receiver) throws IOException {
        FXMLLoader loader = new FXMLLoader(SendMessage.class.getResource("SendMessage.fxml"));
        Parent root = loader.load();
        SendMessage controller = loader.getController();
        controller.setDefaultReceiver(receiver);
        return new Scene(root);
    }
}
