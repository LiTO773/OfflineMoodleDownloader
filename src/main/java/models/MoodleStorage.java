package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Stores everything the application needs
public class MoodleStorage implements Serializable {
    public static final long serialVersionUID = 1L;
    private ArrayList<Moodle> moodles = new ArrayList<>();
    private RefreshTime refreshTime = RefreshTime.HOUR1;

    public MoodleStorage() {
    }

    // moodles
    public boolean add(Moodle moodle) {
        return moodles.add(moodle);
    }

    public boolean remove(int pos) {
        return moodles.remove(pos) != null;
    }

    public int size() {
        return moodles.size();
    }

    public Moodle get(int pos) {
        return moodles.get(pos);
    }

    public List<Moodle> getClone() {
        return (List<Moodle>) moodles.clone();
    }

    public boolean moodleExistsByURLandUsername(String url, String username) {
        for (Moodle m: moodles) {
            if (m.getUrl().equals(url) && m.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // refreshTime
    public RefreshTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(RefreshTime refreshTime) {
        this.refreshTime = refreshTime;
    }
}
