package models;

import java.util.Set;

public class Module extends DBCollection<DBContent> {
    public Module(int id, String name, boolean download, Set<DBContent> collection) {
        super(id, name, download, collection);
    }
}
