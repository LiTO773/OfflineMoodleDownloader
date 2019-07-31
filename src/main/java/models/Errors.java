package models;

public class Errors {
    public static String INVALID_URL = "The inserted URL doesn't belong to a Moodle Application. Please recheck it.";
    public static String INCOMPATIBLE_MOODLE = "The Moodle you inserted does exist, however it doesn't support the use of this application. Please ask the administrator to enable Mobile App support if possible.";
    public static String INVALID_CREDENTIALS = "The inserted username/password is incorrect. Please try again.";
    public static String MISSING_PARAMS = "Please insert a username and password.";
    public static String IO_ERROR = "There was an IO error. Please check if you are running the application properly.";
    public static String TEXTFIELD_EMPTY = "Please fill all the spaces.";
    public static String USERID_ERROR = "An error occured while trying to get the 'userid'. The operation cannot continue.";
}
