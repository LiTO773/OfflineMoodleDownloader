package models;

// This class is mainly used for the course selection view when setting up a new Moodle
public abstract class Downloadable {
    private String name;
    private boolean downloadable;

    public Downloadable() {

    }

    public Downloadable(String name, boolean downloadable) {
        this.name = name;
        this.downloadable = downloadable;
    }

    public String getName() {
        return name;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean val) {
        downloadable = val;
    }
}
