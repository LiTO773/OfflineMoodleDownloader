package models;

import java.io.Serializable;
import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBCollection<?> that = (DBCollection<?>) o;

        return this.getName().equals(that.getName()) &&
                this.isDownloadable() == that.isDownloadable() &&
                id == that.id &&
                this.collection.stream().allMatch(c -> that.collection.contains(c));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, collection);
    }
}
