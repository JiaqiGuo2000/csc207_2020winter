package UI.sign_up;

import calendar_simulation.CalendarSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller of the sign up panel for GUI.
 */
public class SignUpController {
    @FXML
    private Label status;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField cPassword;
    @FXML
    private Button button;

    private CalendarSystem sys = CalendarSystem.getInstance();

    @FXML
    private void createNewAccount() {
        String username = this.username.getText();
        String password1 = password.getText();
        String password2 = cPassword.getText();

        if(sys.accountExists(username)){
            status.setText("Username already exists");
    }else if(!password1.equals(password2)){
            status.setText("Passwords are not same");
        }else{
            try {
                status.setText("Sign up success");
                sys.createAccount(username, password1);
                Stage stage = (Stage)button.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
                stage.setScene(new Scene(root, 647, 426));
            } catch (IOException e) {
                status.setText("Error in writing to database");
            }
        }
    }
}
