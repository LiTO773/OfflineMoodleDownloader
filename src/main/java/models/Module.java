package models;

import java.util.Set;

public class Module extends DBCollection<DBContent> {
    public Module(String id, String name, boolean download, Set<DBContent> collection) {
        super(id, name, download, collection);
    }
}
