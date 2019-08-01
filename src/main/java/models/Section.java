package models;

import java.util.Set;

public class Section extends DBCollection<Module> {
    public Section(int id, String name, boolean download, Set<Module> collection) {
        super(id, name, download, collection);
    }
}
