package models;

import java.util.Set;

/**
 * This class dictates what parameters classes that contain multiple elems should have.
 * @param <E>
 */
public abstract class DBCollection<E> extends Downloadable {
    private String id;
    private Set<E> collection;

    public DBCollection(String id, String name, boolean download, Set<E> collection) {
        super(name, download);
        this.id = id;
        this.collection = collection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<E> getCollection() {
        return collection;
    }

    public void setCollection(Set<E> collection) {
        this.collection = collection;
    }
}
