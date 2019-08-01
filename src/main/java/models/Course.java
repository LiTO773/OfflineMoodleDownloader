package models;

import java.util.Set;

public class Course extends DBCollection<Section> {
    public Course(String id, String name, boolean download, Set<Section> collection) {
        super(id, name, download, collection);
    }
}
