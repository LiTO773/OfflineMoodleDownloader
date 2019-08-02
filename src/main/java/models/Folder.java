package models;

import java.util.Set;

public class Folder extends DBCollection<File> implements Content {
    public Folder(int id, String name, boolean download) {
        super(id, name, download);
    }

    // Rewritten methods to better represent the class
    public boolean addFile(File f) {
        return addToCollection(f);
    }

    public boolean hasFile(File f) {
        return hasInCollection(f);
    }

    public boolean removeFile(File f) {
        return removeFromCollection(f);
    }
}
