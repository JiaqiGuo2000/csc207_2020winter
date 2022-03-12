package UI;

import calendar_simulation.CalendarSystem;
import clock.Clock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * This is the entrance of the program
 */
public class Main extends Application {

    /**
     * This method starts the program by loading a scene of GUI
     * @param primaryStage the entrance stage of the GUI
     */
    @Override
    public void start(Stage primaryStage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("login/Login.fxml"));
            primaryStage.setScene(new Scene(root, 647, 426));
            primaryStage.setTitle("Calendar");
            primaryStage.show();}catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This is the start of the whole program
     * @param args the argument
     */
    public static void main(String[] args) {
        Clock.init(new File("clock.txt"));
        launch(args);
        CalendarSystem.getInstance().logoff();
    }
}
