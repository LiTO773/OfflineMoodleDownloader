package models.enums;

public enum DeletedActions {
    KEEP("Keep files"), ASK("Ask what to do"), DELETE("Delete files");

    private String text;

    DeletedActions(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}