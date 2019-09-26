package models.enums;

public enum NewActions {
    DOWNLOAD("Automatically Download"), ASK("Ask what to do"), DONT_DOWNLOAD("Never Download");

    private String text;

    NewActions(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}