package models;

import java.io.Serializable;
import java.util.Set;

public class Section extends DBCollection<Content> implements Serializable {
    public Section(int id, String name, boolean download) {
        super(id, name, download);
    }

    // Rewritten methods to better represent the class
    public boolean addContent(Content m) {
        return addToCollection(m);
    }

    public boolean hasContent(Content m) {
        return hasInCollection(m);
    }

    public boolean removeContent(Content m) {
        return removeFromCollection(m);
    }
}
