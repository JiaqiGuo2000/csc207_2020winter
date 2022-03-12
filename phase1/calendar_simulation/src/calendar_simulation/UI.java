package calendar_simulation;

import clock.Clock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is the user interface of the calendar system, it is the entrance of the whole program.
 */
public class UI {

    public static String PROMPT = "Command> ";
    private static boolean stop = false;
    private CalendarSystem calendarSystem;
    private InputHandler inputHandler;
    private OutputHandler outputHandler;
    private BufferedReader stdin;

    /**
     * create a new UI object with a CalendarSystem object in it to initiate the program
     *
     * @throws IOException            an exception in file I/O
     * @throws ClassNotFoundException an exception in serialization
     */
    public UI() throws IOException, ClassNotFoundException {
        calendarSystem = new CalendarSystem();
        inputHandler = new InputHandler(calendarSystem);
        outputHandler = new OutputHandler(calendarSystem);
    }

    /**
     * this method could prevent the calendar system from entering next command-handling cycle
     */
    public static void stopUI() {
        stop = true;
    }

    /**
     * the entrance of the program which starts the command-handling cycle
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        UI app = new UI();
        app.run();
    }

    /**
     * the handling cycle of the program that receives command from the user
     */
    public void run() {
        // Adjust Clock to the setting last time
        Clock.init(new File("clock.txt"));

        while (!stop) {
            stdin = new BufferedReader(new InputStreamReader(System.in));
            updatePrompt();
            System.out.print(PROMPT);

            try {
                String cmdLine = stdin.readLine();
                inputHandler.handleCommand(cmdLine);
                calendarSystem.checkAndRing();
            } catch (Exception e) {
                calendarSystem.logoff();
                stopUI();
                outputHandler.printError("Application Terminated");
            }
        }
    }

    private void updatePrompt() {
        if (calendarSystem.isLoggedIn()) {
            PROMPT = calendarSystem.getAccountUser() + " > ";
        } else {
            PROMPT = "> ";
        }
    }

}
