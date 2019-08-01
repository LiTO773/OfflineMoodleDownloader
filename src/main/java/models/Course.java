package models;

import java.util.Set;

public class Course extends DBCollection<Section> {
    public Course(int id, String name, boolean download) {
        super(id, name, download, null);
    }

    public Course(int id, String name, boolean download, Set<Section> collection) {
        super(id, name, download, collection);
    }
}
