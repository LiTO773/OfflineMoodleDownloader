package models;

public class File implements Content {
    String moduleName;
    String fileName;
    int timeModified;
    String fileURL;
    boolean download;

    public File(String moduleName, String fileName, int timeModified, String fileURL, boolean download) {
        this.moduleName = moduleName;
        this.fileName = fileName;
        this.timeModified = timeModified;
        this.fileURL = fileURL;
        this.download = download;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    @Override
    public String toString() {
        return "File{" +
                "moduleName='" + moduleName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", timeModified=" + timeModified +
                ", fileURL='" + fileURL + '\'' +
                ", download=" + download +
                '}';
    }
}
