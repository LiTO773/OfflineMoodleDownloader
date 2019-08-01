package models;

public abstract class Downloadable {
    private String name;
    private boolean download;

    public Downloadable(String name, boolean download) {
        this.name = name;
        this.download = download;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}
