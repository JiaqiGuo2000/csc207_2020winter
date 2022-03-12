package calendar_simulation;

/**
 * This method is responsible for the standard output of the UI.
 */
public class OutputHandler {

    private CalendarSystem calendarSystem;

    /**
     * Create a new OutputHandler associated with the CalendarSystem being operated
     *
     * @param cal the CalendarSystem being operated by the user
     */
    OutputHandler(CalendarSystem cal) {
        calendarSystem = cal;
    }

    /**
     * print the Error message with given custom details about the command
     *
     * @param cur_selected the command which produced error
     * @param errorMsg     the error message about the error
     */
    public void printError(String cur_selected, String errorMsg) {
        System.out.println(cur_selected + " has failed! " + errorMsg);
    }

    /**
     * print the Error message about the command
     *
     * @param cur_selected the command which produced error
     */
    public void printError(String cur_selected) {
        System.out.println(cur_selected + " has failed! Please try again");
    }

    /**
     * print the success message if the command is executed successfully
     *
     * @param cur_selected the command which is executed successfully
     */
    public void printSuccess(String cur_selected) {
        System.out.println(cur_selected + " was successful!");
    }

    /**
     * print the help message about the commands that the user could use with their details
     */
    public void printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(UI.PROMPT).append("USER HELP (Usage):\n");
        sb.append(UI.PROMPT);
        sb.append("::::::::::::::::::::::::::::::::");
        sb.append(":::::::::::::::::::::::::::::::::::::::::::::\n");
        if (!calendarSystem.isLoggedIn()) {
            sb.append(UI.PROMPT).append("login");
            sb.append("\t\t\t\t\t Begin login process\n");
            sb.append(UI.PROMPT).append("sign-up");
            sb.append("\t\t\t\t Create a new account\n");
        } else {
            sb.append(UI.PROMPT).append("view");
            sb.append("\t\t\t\t\t Enter view mode to view events, memos, alerts\n");
            sb.append(UI.PROMPT).append("search");
            sb.append("\t\t\t\t Enter search mode to search events, series, tags, dates\n");
            sb.append(UI.PROMPT).append("select");
            sb.append("\t\t\t\t Select specified event (*Must Call After View/Search*)\n");
            sb.append(UI.PROMPT).append("unselect");
            sb.append("\t\t\t\t Unselect previously selected event\n");
            sb.append(UI.PROMPT).append("create");
            sb.append("\t\t\t\t Begin creation of an event\n");
            sb.append(UI.PROMPT).append("add");
            sb.append("\t\t\t\t\t Begin addition of memos|tags|series|alerts\n");
            sb.append(UI.PROMPT).append("delete");
            sb.append("\t\t\t\t Begin deletion of tags|alerts|memos\n");

            sb.append(UI.PROMPT).append("delete-account");
            sb.append("\t\t Delete this account\n");
            sb.append(UI.PROMPT).append("change-password");
            sb.append("\t\t Change the password of this account\n");

            sb.append(UI.PROMPT).append("logoff");
            sb.append("\t\t\t\t Log off\n");
        }
        sb.append(UI.PROMPT).append("time");
        sb.append("\t\t\t\t\t What time is it?\n");
        sb.append(UI.PROMPT).append("jump");
        sb.append("\t\t\t\t\t Time magic: Jump to a future time\n");
        sb.append(UI.PROMPT).append("speed");
        sb.append("\t\t\t\t Time magic: Set speed\n");
        sb.append(UI.PROMPT).append("quit ");
        sb.append("\t\t\t\t Exit the program\n");
        sb.append(UI.PROMPT);
        sb.append("::::::::::::::::::::::::::::::::");
        sb.append(":::::::::::::::::::::::::::::::::::::::::::::\n");
        System.out.println(sb.toString());
    }

}
