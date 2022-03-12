package calendar_simulation;

import clock.Clock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * This class is responsible for handling the user's input and pass the commands to the CalendarSystem class.
 */
public class InputHandler {

    private OutputHandler outputHandler;
    private CalendarSystem calendarSystem;

    /**
     * Create a new InputHandler associated with the CalendarSystem being operation and construct an OutputHandler to
     * handle the output message
     *
     * @param cal the calender system that is being operated by the user
     */
    InputHandler(CalendarSystem cal) {
        calendarSystem = cal;
        outputHandler = new OutputHandler(calendarSystem);
    }

    /**
     * This method receives the user's command and pass it into system by calling different methods in CalendarSystem.
     *
     * @param cmdLine the command input by the user
     */
    public void handleCommand(String cmdLine) {
        String[] args = cmdLine.split("\\s+");

        switch (args[0]) {
            case "quit":
                quit();
                return;
            case "help":
                outputHandler.printHelp();
                return;
            case "time":
                time();
                return;
            case "jump":
                jump();
                return;
            case "speed":
                speed();
                return;
        }

        if (calendarSystem.isLoggedIn()) {
            switch (args[0]) {
                case "view":
                    view();
                    break;
                case "search":
                    search();
                    break;
                case "create":
                    create();
                    break;
                case "select":
                    select();
                    break;
                case "unselect":
                    unselect();
                    break;
                case "add":
                    add();
                    break;
                case "delete":
                    delete();
                    break;
                case "delete-account":
                    deleteAccount();
                    break;
                case "change-password":
                    changePassword();
                    break;
                case "logoff":
                    logoff();
                    break;
                default:
                    outputHandler.printError("Unknown command");
                    outputHandler.printHelp();
            }
        } else {
            switch (args[0]) {
                case "login":
                    login();
                    break;
                case "sign-up":
                    signUp();
                    break;
                default:
                    outputHandler.printError("Unknown command");
                    outputHandler.printHelp();
            }
        }
    }

    private String enter(String action, String before, String afterFailure) {
        System.out.println(before);
        String input = handleInput();
        if (input == null)
            outputHandler.printError(action, afterFailure);
        return input;
    }

    private void login() {
        String cur_selected = "Login";
        System.out.println("Login Selected, please answer the following questions:");

        //Parse Username
        String username = enter(cur_selected,
                "What is your Username?",
                "Username parsing failed, exiting login process");
        if (username == null) return;

        //Parse Password
        String password = enter(cur_selected,
                "What is your Password?",
                "Password parsing failed, exiting login process");
        if (password == null) return;

        //Find <Username,Password> In database
        try {
            if (calendarSystem.login(username, password) == 1) {
                outputHandler.printSuccess("Login");
            } else {
                outputHandler.printError(cur_selected, "The user does not exist or the password is not correct");
            }
        } catch (IOException | ClassNotFoundException e) {
            outputHandler.printError(cur_selected, "Error in reading from the account database");
        }
    }

    private void logoff() {
        String cur_selected = "Logoff";
        System.out.println("Logoff Selected, logging off...");
        if (calendarSystem.logoff() == 1)
            outputHandler.printSuccess(cur_selected);
        else
            outputHandler.printError(cur_selected);
    }

    private void signUp() {
        String cur_selected = "Sign-up";
        System.out.println("Sign-up Selected, please answer the following questions:");

        // Parse username
        String username = enter(cur_selected,
                "What is your Username?",
                "Username parsing failed, exiting login process");
        if (username == null) return;

        // Is username available?
        if (calendarSystem.accountExists(username)) {
            outputHandler.printError(cur_selected, "Username is not available");
            return;
        }

        // Parse password
        String password = enter(cur_selected,
                "What is your Password?",
                "Password parsing failed, exiting login process");
        if (password == null) return;

        // Create the account
        try {
            calendarSystem.createAccount(username, password);
            outputHandler.printSuccess("Sign-up");
        } catch (IOException e) {
            outputHandler.printError(cur_selected, "Error in writing to the account database");
        }
    }

    private void changePassword() {
        String cur_selected = "Change-password";

        // Parse new password
        String newPassword = enter(cur_selected,
                "Password selected, please enter the new password",
                "Password parsing failed, existing changing process");
        if (newPassword == null) return;

        try {
            calendarSystem.changePassword(newPassword);
            outputHandler.printSuccess(cur_selected);
        } catch (IOException e) {
            outputHandler.printError(cur_selected, "Error in writing to the account database");
        }
    }

    private void deleteAccount() {
        String cur_selected = "Delete-account";
        try {
            calendarSystem.deleteAccount();
            outputHandler.printSuccess(cur_selected);
        } catch (IOException e) {
            outputHandler.printError(cur_selected, "Error in writing to the account database");
        }
    }

    private void quit() {
        if (calendarSystem.isLoggedIn()) {
            logoff();
        }
        UI.stopUI();
        System.out.println("Application Exiting");
    }

    private void view() {
        String cur_selected = "View";
        System.out.println(cur_selected + " Selected, type <events | tags | memo | alerts> to view corresponding actions");

        //Parse action type
        String action = handleInput();
        switch (action) {
            case "events":
                view_events();
                break;
            case "memo":
                view_memo();
                break;
            case "alerts":
                view_alerts();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid input");
        }
    }

    private void view_events() {
        String cur_selected = "View Events";
        System.out.println(cur_selected + " Selected, type <all | past | ongoing | future | ofmemos> to view corresponding events" +
                "(*Note: ofmemos must be called after view memos has been called");

        //Parse event type
        String event = handleInput();
        int ret = 0;
        switch (event) {
            case "all":
                ret = calendarSystem.viewAllEvent();
                break;
            case "past":
                ret = calendarSystem.viewPastEvent();
                break;
            case "ongoing":
                ret = calendarSystem.viewOngoingEvent();
                break;
            case "future":
                ret = calendarSystem.viewFutureEvent();
                break;
            case "ofmemos":
                ret = view_events_ofmemos();
            default:
                outputHandler.printError(cur_selected, "Invalid input");
                break;
        }

        if (ret == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected);
        }
    }

    private int view_events_ofmemos() {
        System.out.println("Please type in the memo # to view events associated with it (Note: memo # is from all memos list");

        int memo_num = Integer.parseInt(handleInput());

        return calendarSystem.viewEventsFromMemos(memo_num);
    }

    private void view_memo() {
        String cur_selected = "View Memo";
        System.out.println(cur_selected + " Selected, Printing all Memos:");

        if (calendarSystem.viewAllMemos() == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected);
        }
    }

    private void view_alerts() {
        String cur_selected = "View Alerts";
        System.out.println(cur_selected + " Selected, printing alerts");

        if (calendarSystem.viewAllAlerts() == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected, "There may be no alerts");
        }
    }

    private void search() {
        String cur_selected = "Search";
        System.out.println(cur_selected + " Selected, type <name | seriesName | tag | date> to search by corresponding actions");

        //Parse action type
        String action = handleInput();
        switch (action) {
            case "name":
                search_name();
                break;
            case "seriesName":
                search_series();
                break;
            case "tag":
                search_tag();
                break;
            case "date":
                search_date();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid input");
        }
    }

    private void search_name() {
        String cur_selected = "Search by name";
        System.out.println(cur_selected + " Selected, Please type in the name of event to search:");

        //Parse action type
        String name = handleInput();
        if (calendarSystem.searchByName(name) == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected);
        }
    }

    private void search_series() {
        String cur_selected = "Search by series name";
        System.out.println(cur_selected + " Selected, Please type in the name of series to search:");

        //Parse action type
        String name = handleInput();
        if (calendarSystem.searchBySeriesName(name) == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected);
        }
    }

    private void search_tag() {
        String cur_selected = "Search by tag";
        System.out.println(cur_selected + " Selected, Please type in the tag to search:");

        //Parse action type
        String tag = handleInput();
        if (calendarSystem.searchByTag(tag) == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected);
        }
    }

    private void search_date() {
        String cur_selected = "Search by date";
        System.out.println(cur_selected + " Selected, Please type in the following search:");

        int year, month, day;

        try {
            //Parse action type
            System.out.println("Year?");
            year = Integer.parseInt(handleInput());
            System.out.println("Month?");
            month = Integer.parseInt(handleInput());
            System.out.println("Day?");
            day = Integer.parseInt(handleInput());

            LocalDate date = LocalDate.of(year, month, day);

            if (calendarSystem.searchByDate(date) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected);
            }
        } catch (NumberFormatException ne) {
            System.out.println("Error: Please retry to create event and input a valid date/time.");
            outputHandler.printHelp();
        } catch (DateTimeParseException de) {
            System.out.println("Error: Please type in a valid date on the calendar. Exiting");
            outputHandler.printHelp();
        }
    }

    private void create() {
        String cur_selected = "Create Event";
        System.out.println(cur_selected + " Selected, type <series | single> to create corresponding event type");

        //Parse action type
        String type = handleInput();
        switch (type) {
            case "series":
                createSeriesEvent();
                break;
            case "single":
                createSingleEvent();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid input");
                break;
        }
    }

    private void createSeriesEvent() {
        String cur_selected = "Create Series Event";
        System.out.println(cur_selected + " Selected, Please Type in the Following:");

        String name, series_name;
        int start_year, start_month, start_day, start_hour, start_min, end_year, end_month, end_day,
                end_hour, end_min, frequency, num_repeat;

        try {

            System.out.println("Name of Event?");
            name = handleInput();
            System.out.println("Start year of Event?");
            start_year = Integer.parseInt(Objects.requireNonNull(handleInput()));
            System.out.println("Start month of Event?");
            start_month = Integer.parseInt(handleInput());
            System.out.println("Start day of Event?");
            start_day = Integer.parseInt(handleInput());
            System.out.println("Start hour of Event?");
            start_hour = Integer.parseInt(handleInput());
            System.out.println("Start minute of Event?");
            start_min = Integer.parseInt(handleInput());

            System.out.println("End year of Event?");
            end_year = Integer.parseInt(handleInput());
            System.out.println("End month of Event?");
            end_month = Integer.parseInt(handleInput());
            System.out.println("End day of Event?");
            end_day = Integer.parseInt(handleInput());
            System.out.println("End hour of Event?");
            end_hour = Integer.parseInt(handleInput());
            System.out.println("End minute of Event?");
            end_min = Integer.parseInt(handleInput());

            System.out.println("Name of Series");
            series_name = handleInput();

            System.out.println("Time interval between two consecutive events (in days)");
            frequency = Integer.parseInt(handleInput());

            System.out.println("How many times do you want each event in the series to repeat?");
            num_repeat = Integer.parseInt(handleInput());

            LocalDateTime start = LocalDateTime.of(start_year, start_month, start_day, start_hour, start_min);
            LocalDateTime end = LocalDateTime.of(end_year, end_month, end_day, end_hour, end_min);

            if (calendarSystem.createEvent(name, start, end, series_name, frequency, num_repeat) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected);
            }
        } catch (NumberFormatException ne) {
            System.out.println("Error: Please retry to create event and input a valid date/time.");
            outputHandler.printHelp();
        } catch (DateTimeParseException de) {
            System.out.println("Error: Please type in a valid date on the calendar. Exiting");
            outputHandler.printHelp();
        }

    }

    private void createSingleEvent() {
        String cur_selected = "Create Single Event";
        System.out.println(cur_selected + " Selected, Please Type in the Following:");
        System.out.println("Name of Event?");
        String name = handleInput();
        int startYear, startMonth, startDay, startHour, startMin;
        int endYear, endMonth, endDay, endHour, endMin;
        try {
            System.out.println("Start year of Event?");
            startYear = Integer.parseInt(handleInput());
            System.out.println("Start month of Event?");
            startMonth = Integer.parseInt(handleInput());
            System.out.println("Start day of Event?");
            startDay = Integer.parseInt(handleInput());
            System.out.println("Start hour of Event?");
            startHour = Integer.parseInt(handleInput());
            System.out.println("Start minute of Event?");
            startMin = Integer.parseInt(handleInput());

            System.out.println("End year of Event?");
            endYear = Integer.parseInt(handleInput());
            System.out.println("End month of Event?");
            endMonth = Integer.parseInt(handleInput());
            System.out.println("End day of Event?");
            endDay = Integer.parseInt(handleInput());
            System.out.println("End hour of Event?");
            endHour = Integer.parseInt(handleInput());
            System.out.println("End minute of Event?");
            endMin = Integer.parseInt(handleInput());
            LocalDateTime start = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMin);
            LocalDateTime end = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMin);

            if (calendarSystem.createEvent(name, start, end) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected);
            }
        } catch (NumberFormatException ne) {
            System.out.println("Error: Please retry to create event and input a valid date/time.");
            outputHandler.printHelp();
        } catch (DateTimeParseException de) {
            System.out.println("Error: Please type in a valid date on the calendar. Exiting");
            outputHandler.printHelp();
        }

    }

    private void select() {
        String cur_selected = "Select";
        System.out.println(cur_selected + ", please type in <event| memo | tag | alert> to call corresponding" +
                " actions (*Note: must have selected an event to do memo | tag | alert*)");

        //Parse action type
        String action = handleInput();

        switch (action) {
            case "event":
                selectEvent();
                break;
            case "memo":
                select_memo();
                break;
            case "tag":
                select_tag();
                break;
            case "alert":
                select_alert();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid input");
                break;
        }
    }

    private void selectEvent() {
        String cur_selected = "Select event";
        System.out.println(cur_selected + ", please type in the event # to select (note: you must view events before selecting or it will fail)");

        //Parse action type
        System.out.println("Event #?");
        calendarSystem.getCurrentEventList();
        System.out.println("");
        int eventNum;
        try {
            eventNum = Integer.parseInt(handleInput());
            if (calendarSystem.selectEvent(eventNum) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected, "You must View/Search before selecting");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid event number");
            outputHandler.printHelp();
        }
    }


    private void select_memo() {
        String cur_selected = "Select memo";
        System.out.println(cur_selected + ", please type in the memo # to select (note: you must view events before selecting or it will fail)");

        //Parse action type
        System.out.println("Memo #?");
        calendarSystem.getCurrentMemoList();
        System.out.println("");
        int memoNum;
        try {
            memoNum = Integer.parseInt(handleInput());
            if (calendarSystem.selectMemo(memoNum) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected, "You must View/Search before selecting");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid memo number");
        }

    }


    private void select_tag() {
        String cur_selected = "Select tag";
        System.out.println(cur_selected + ", please type in the tag # to select (note: you must view events before selecting or it will fail)");

        //Parse action type
        System.out.println("Tag #?");
        calendarSystem.getCurrentTagList();
        System.out.println("");
        int tagNum;
        try {
            tagNum = Integer.parseInt(handleInput());
            if (calendarSystem.selectTag(tagNum) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected, "You must View/Search before selecting");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid tag number");
        }
    }


    private void select_alert() {
        String cur_selected = "Select alert";
        System.out.println(cur_selected + ", please type in the alert # to select (note: please \"view\" events before selecting)");

        //Parse action type
        System.out.println("Alert #?");
        int alertNum;
        try {
            alertNum = Integer.parseInt(handleInput());
            if (calendarSystem.selectAlert(alertNum) == 1) {
                outputHandler.printSuccess(cur_selected);
            } else {
                outputHandler.printError(cur_selected, "You must View/Search before selecting");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid alert number");
        }

    }

    private void unselect() {
        String cur_selected = "Un-Select event";
        System.out.println("Un-Selecting event previously selected");

        if (calendarSystem.deselectEvent() == 1) {
            outputHandler.printSuccess(cur_selected);
        } else {
            outputHandler.printError(cur_selected, "No previously selected event");
        }

    }

    private void add() {
        String cur_selected = "Add";
        System.out.println(cur_selected + "Selected, type <memo | tag | series | alert> to " +
                "add corresponding type (Note: You must have selected an event previously");

        //Parse action type
        String action = handleInput();

        switch (action) {
            case "memo":
                add_memo();
                break;
            case "tag":
                add_tag();
                break;
            case "series":
                add_series();
                break;
            case "alert":
                add_alert();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid Input");
        }
    }

    private void add_memo() {
        String cur_selected = "Add Memo";
        System.out.println(cur_selected + "Selected, type <new, old> to create new memo for event, or add old memo to current event");

        String action = handleInput();
        if (action.equals("new")) {
            System.out.println("Type in memo to add:");
            String memo = handleInput();
            if (calendarSystem.addNewMemo(memo) == -1) {
                System.out.println("You need to select an event first!");
            }
        } else if (action.equals("old")) {
            System.out.println("Type in memo # to add to current event");
            int num;
            try {
                num = Integer.parseInt(handleInput());
                if (calendarSystem.addMemoToEvent(num) == -1) {
                    System.out.println("You need to select an event first!");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Please retry and input a valid memo number.");
            }

        } else {
            outputHandler.printError(cur_selected, "Invalid Input or event not selected");
        }

    }

    private void add_tag() {
        String cur_selected = "Add Tag";
        System.out.println(cur_selected + "Selected, type Tag name to add to current event");

        String tagName = handleInput();
        if (calendarSystem.addTagToEvent(tagName) == -1) {
            System.out.println("You need to select an event first!");
        }
    }

    private void add_series() {
        String cur_selected = "Add Series";
        System.out.println(cur_selected + "Selected, type series name to add to current event");

        String seriesName = handleInput();
        if (calendarSystem.addIntoSeries(seriesName) == -1) {
            System.out.println("You need to select an event first!");
        }
    }

    private void add_alert() {
        String cur_selected = "Add Alert";
        System.out.println(cur_selected + "Selected, type <single | series> to add corresponding alert");

        //Parse action type
        String action = handleInput();

        switch (action) {
            case "single":
                add_single_alert();
                break;
            case "series":
                add_series_alert();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid Input");
        }

    }

    private void add_single_alert() {
        String cur_selected = "Add Single Alert";
        System.out.println(cur_selected + "Selected,Please answer the following questions");

        String name;
        int year, month, day, hour, min;

        try {
            System.out.println("Name of Alert?");
            name = handleInput();
            System.out.println("Year of Alert?");
            year = Integer.parseInt(handleInput());
            System.out.println("Month of Alert?");
            month = Integer.parseInt(handleInput());
            System.out.println("Day of Alert?");
            day = Integer.parseInt(handleInput());
            System.out.println("Hour of Alert?");
            hour = Integer.parseInt(handleInput());
            System.out.println("Minute of Alert?");
            min = Integer.parseInt(handleInput());

            LocalDateTime time = LocalDateTime.of(year, month, day, hour, min);

            if (calendarSystem.setAlert(name, time) == -1) {
                System.out.println("You need to select an event first!");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Error: Please retry to create event and input a valid date/time.");
            outputHandler.printHelp();
            return;
        } catch (DateTimeParseException de) {
            System.out.println("Error: Please type in a valid date on the calendar. Exiting");
            outputHandler.printHelp();
            return;
        }
    }

    private void add_series_alert() {
        String cur_selected = "Add Series Alert";
        System.out.println(cur_selected + "Selected,Please answer the following questions");

        String name;
        int year, month, day, hour, min, freq;

        try {
            System.out.println("Name of Alert?");
            name = handleInput();

            System.out.println("Year of the First Alert?");
            year = Integer.parseInt(handleInput());
            System.out.println("Month of Alert?");
            month = Integer.parseInt(handleInput());
            System.out.println("Day of Alert?");
            day = Integer.parseInt(handleInput());
            System.out.println("Hour of Alert?");
            hour = Integer.parseInt(handleInput());
            System.out.println("Minute of Alert?");
            min = Integer.parseInt(handleInput());

            System.out.println("Period of Alerts (in hours)?");
            freq = Integer.parseInt(handleInput());

            LocalDateTime time = LocalDateTime.of(year, month, day, hour, min);

            if (calendarSystem.setAlert(name, time, freq) == -1) {
                System.out.println("You need to select an event first!");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Error: Please retry to create event and input a valid date/time.");
            outputHandler.printHelp();
        } catch (DateTimeParseException de) {
            System.out.println("Error: Please type in a valid date on the calendar. Exiting");
            outputHandler.printHelp();
        }
    }

    private void delete() {
        String cur_selected = "Delete";
        System.out.println(cur_selected + "Selected, type <memo | tag | alert> to " +
                "delete corresponding type (Note: You must have selected an event previously");

        //Parse action type
        String action = handleInput();

        switch (action) {
            case "memo":
                delete_memo();
                break;
            case "tag":
                delete_tag();
                break;
            case "alert":
                delete_alert();
                break;
            default:
                outputHandler.printError(cur_selected, "Invalid Input");
        }
    }

    private void delete_memo() {
        String cur_selected = "Delete Memo";
        System.out.println(cur_selected + "Selected, type Memo number to delete in current event");

        int memoNum;
        try {
            memoNum = Integer.parseInt(handleInput());
            if (calendarSystem.deleteMemo(memoNum) == -1) {
                System.out.println("You need to select an event first!");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid memo number");
        }

    }

    private void delete_tag() {
        String cur_selected = "Delete Tag";
        System.out.println(cur_selected + "Selected, type Tag number to delete in current event");

        int tagNum;
        try {
            tagNum = Integer.parseInt(handleInput());
            if (calendarSystem.deleteTag(tagNum) == -1) {
                System.out.println("You need to select an event first!");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid tag number");
        }

    }

    private void delete_alert() {
        String cur_selected = "Delete Alert";
        System.out.println(cur_selected + "Selected, type Alert number to delete in current event");

        int alertNum;
        try {
            alertNum = Integer.parseInt(handleInput());
            if (calendarSystem.deleteAlerts(alertNum) == -1) {
                System.out.println("You need to select an event first!");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Please retry and input a valid alert number");
        }
    }

    private void time() {
        System.out.println(Clock.getTime());
    }

    private void jump() {
        String cur_selected = "Jump";
        String s = enter(cur_selected,
                "Jump selected, please enter a future time (e.g. 3020-03-06T10:15:30): ",
                "Time parsing failed, exiting jump process");
        try {
            LocalDateTime t = LocalDateTime.parse(s), now = Clock.getTime();
            if (t.isBefore(now.plusSeconds(1))) {
                outputHandler.printError(cur_selected, "Time is not in the future, existing jump process");
            } else {
                Clock.jumpTo(t);
                outputHandler.printSuccess(cur_selected);
            }
        } catch (DateTimeParseException e) {
            outputHandler.printError(cur_selected, "Time parsing failed, exiting jump process");
        }
    }

    private void speed() {
        String cur_selected = "Speed";
        String s = enter(cur_selected,
                "Speed selected, please enter a speed (e.g. 1.5): ",
                "Speed parsing failed, existing speed setting process");
        try {
            double v = Double.parseDouble(s);
            if (v < 0) {
                outputHandler.printError(cur_selected, "Negative speed is not supported, existing speed setting process");
            } else {
                Clock.setSpeed(v);
                outputHandler.printSuccess(cur_selected);
            }
        } catch (NumberFormatException e) {
            outputHandler.printError(cur_selected, "Speed parsing failed, existing speed setting process");
        }
    }

    private String handleInput() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(UI.PROMPT);

        try {
            return input.readLine();
        } catch (IOException ie) {
            UI.stopUI();
            outputHandler.printError("Command did not respond - Application Terminated");
            return null;
        }
    }


}
