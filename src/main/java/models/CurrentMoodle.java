package models;

import files.DataOperations;

import java.io.IOException;

/**
 * Static class that stores the currently used Moodle
 */
public class CurrentMoodle {
    // Used to store all the data structures necessary during the execution
    private static MoodleStorage allMoodles = new MoodleStorage();
    private static int moodlePos = -1; // -1 indicates that it is a new Moodle

    // Stores the information about a new Moodle
    private static Moodle newMoodleStruct;

    public static boolean loadAllMoodles() {
        try {
            allMoodles = DataOperations.readData();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeAllMoodles() {
        try {
            DataOperations.writeData();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static MoodleStorage getAllMoodles() {
        return allMoodles;
    }

    public static void clear() {
        moodlePos = -1;
        newMoodleStruct = null;
    }

    public static void newMoodle(Moodle moodle) {
        moodlePos = -1;
        newMoodleStruct = moodle;
    }

    public static void setMoodleByPos(int pos) {
        moodlePos = pos;
    }

    public static boolean moodleExistsByURLandUsername(String url, String username) {
        return allMoodles.moodleExistsByURLandUsername(url, username);
    }

    public static Moodle getMoodle() {
        return moodlePos == -1 ? newMoodleStruct : allMoodles.get(moodlePos);
    }

    public static boolean saveMoodle() {
        if (!allMoodles.add(new Moodle(newMoodleStruct))) {
            return false;
        }
        moodlePos = allMoodles.size() - 1;
        newMoodleStruct = null;
        return true;
    }

    public static boolean removeMoodle(int moodlePos) {
        return allMoodles.remove(moodlePos);
    }

    // Refresh Time information
    public static RefreshTime getRefreshTime() {
        return allMoodles.getRefreshTime();
    }

    public static void setRefreshTime(RefreshTime refreshTime) {
        allMoodles.setRefreshTime(refreshTime);
    }
}
