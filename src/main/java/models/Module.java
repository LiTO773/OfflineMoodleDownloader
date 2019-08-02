package models;

public class Module extends DBCollection<Content> {
    public Module(int id, String name, boolean download) {
        super(id, name, download);
    }

    // Rewritten methods to better represent the class
    public boolean addContent(Content c) {
        return addToCollection(c);
    }

    public boolean hasContent(Content c) {
        return hasInCollection(c);
    }

    public boolean removeContent(Content c) {
        return removeFromCollection(c);
    }
}
