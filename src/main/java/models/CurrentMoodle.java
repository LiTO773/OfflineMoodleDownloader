package models;

/**
 * Static class that stores the currently used Moodle
 */
public class CurrentMoodle {
    // Used for the first scene and loading scene
    private static String url;
    private static String username;
    private static String password;

    public static void setParams(String url, String username, String password) {
        CurrentMoodle.url = url;
        CurrentMoodle.username = username;
        CurrentMoodle.password = password;
    }

    public static String getUrl() {
        return url == null ? "" : url;
    }

    public static String getUsername() {
        return username == null ? "" : username;
    }

    public static String getPassword() {
        return password == null ? "" : password;
    }

    // Used to store all the data structures necessary
    private static Moodle current;

    public static void clear() {
        current = null;
    }

    public static void setMoodle(Moodle moodle) {
        current = moodle;
    }

    public static Moodle getMoodle() {
        return current;
    }
}
