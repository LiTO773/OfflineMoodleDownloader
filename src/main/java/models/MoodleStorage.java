package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoodleStorage implements Serializable {
    public static final long serialVersionUID = 1L;
    private ArrayList<Moodle> moodles = new ArrayList<>();

    public MoodleStorage() {
    }

    public boolean add(Moodle moodle) {
        return moodles.add(moodle);
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
}
