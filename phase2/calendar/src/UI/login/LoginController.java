package UI.login;

//import UI.P.A;
import UI.main_window.MainWindowController;
import UI.messaging.ReceiveMessages;
import UI.ring_alerts.RingAlerts;
import calendar_simulation.CalendarSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller of the log in panel for GUI.
 */
public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button button;
    @FXML
    private Hyperlink hyperlink_account;
    @FXML
    public Label Warning;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void login(){
        String user = this.username.getText();
        String password = this.password.getText();
        try{if (sys.login(user, password) == 1) {
            loginSuccess();
        } else {
            loginFailed();
        } }catch (IOException | ClassNotFoundException e){
            System.out.println("Error in reading from the account database");
        }
    }

    private void loginSuccess(){
        try{
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../main_window/MainWindow.fxml"));
            //primaryStage.setScene(new Scene(root, 826, 800));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            ReceiveMessages.main();
            RingAlerts.start();
        }
        catch(IOException ie){
            System.out.println("Error in Loading Main GUI");
        }
    }

    private void loginFailed(){
        Warning.setText("Login Failed");
    }

    @FXML
    private void createAccount() throws IOException {
        Stage primaryStage = (Stage) username.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../sign_up/SignUp.fxml"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

}
