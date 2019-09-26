package models;

import models.enums.DeletedActions;
import models.enums.NewActions;
import models.enums.RefreshTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Stores everything the application needs
public class MoodleStorage implements Serializable {
    public static final long serialVersionUID = 1L;
    private ArrayList<Moodle> moodles = new ArrayList<>();

    // Settings variables
    private RefreshTime refreshTime = RefreshTime.HOUR1;
    private NewActions newFileAction = NewActions.DOWNLOAD;
    private DeletedActions deletedFileAction = DeletedActions.KEEP;

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

    // Settings
    public RefreshTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(RefreshTime refreshTime) {
        this.refreshTime = refreshTime;
    }

    public NewActions getNewFileAction() {
        return newFileAction;
    }

    public void setNewFileAction(NewActions newFileAction) {
        this.newFileAction = newFileAction;
    }

    public DeletedActions getDeletedFileAction() {
        return deletedFileAction;
    }

    public void setDeletedFileAction(DeletedActions deletedFileAction) {
        this.deletedFileAction = deletedFileAction;
    }
}
