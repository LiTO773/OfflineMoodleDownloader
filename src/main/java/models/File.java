package models;

import java.io.Serializable;

public class File extends Downloadable implements Content, Serializable {
    String moduleName;
    int timeModified;
    String fileURL;

    public File(String moduleName, String fileName, int timeModified, String fileURL, boolean download) {
        super(fileName, download);
        this.moduleName = moduleName;
        this.timeModified = timeModified;
        this.fileURL = fileURL;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFileName() {
        return this.getName();
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

    @Override
    public String toString() {
        return "File{" +
                "moduleName='" + moduleName + '\'' +
                ", fileName='" + this.getFileName() + '\'' +
                ", timeModified=" + timeModified +
                ", fileURL='" + fileURL + '\'' +
                ", download=" + this.isDownloadable() +
                '}';
    }
}
