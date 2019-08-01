package models;

import java.util.Set;

public class Folder extends DBCollection<File> implements DBContent {
    public Folder(String id, String name, boolean download, Set<File> collection) {
        super(id, name, download, collection);
    }
}
