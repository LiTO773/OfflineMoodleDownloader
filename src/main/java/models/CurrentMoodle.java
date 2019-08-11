package models;

/**
 * Static class that stores the currently used Moodle
 */
public class CurrentMoodle {
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
