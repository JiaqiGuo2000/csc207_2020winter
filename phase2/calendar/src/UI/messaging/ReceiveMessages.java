package UI.messaging;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * This is the helper for receiving messages
 */
public class ReceiveMessages {
    /**
     * The helper code sets up the receive message panel
     * @throws IOException an exception in file I/O
     */
    public static void main() throws IOException {
        List<String[]> ms = CalendarSystem.getInstance().receiveMessages();
        for (String[] m: ms) {
            FXMLLoader loader = new FXMLLoader(ReceiveMessages.class.getResource("ViewMessage.fxml"));
            Scene scene = new Scene(loader.load());
            ViewMessage controller = loader.getController();
            if (m[2] == null) {
                controller.removeEventRow();
                controller.set(m[0], "", m[1]);
            } else {
                controller.set(m[0], m[2], m[1]);
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Message");
            stage.showAndWait();
        }
    }
}
