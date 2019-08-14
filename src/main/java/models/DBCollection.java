package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class dictates what parameters classes that contain multiple elems should have.
 * @param <E>
 */
public abstract class DBCollection<E> extends Downloadable implements Serializable {
    private int id;
    private List<E> collection;

    public DBCollection() {}

    public DBCollection(int id, String name, boolean download) {
        super(name, download);
        this.id = id;
        this.collection = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<E> getCollection() {
        return collection;
    }

    // These 3 methods are package private since they should be rewritten in each class with the appropriate moduleName
    boolean addToCollection(E e) { return collection.add(e); }
    boolean hasInCollection(E e) { return collection.contains(e); }
    boolean removeFromCollection(E e) { return collection.remove(e); }
    // ---

    @Override
    public String toString() {
        return "DBCollection{" +
                "id=" + id +
                ", moduleName='" + this.getName() + '\'' +
                ", download=" + this.isDownloadable() +
                '}';
    }
}
