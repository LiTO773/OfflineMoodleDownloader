package models;

public class File implements DBContent {
    int id;
    String name;
    int timeModified;
    String fileURL;
    boolean download;

    public File(int id, String name, int timeModified, String fileURL, boolean download) {
        this.id = id;
        this.name = name;
        this.timeModified = timeModified;
        this.fileURL = fileURL;
        this.download = download;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(int timeModified) {
        this.timeModified = timeModified;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}
