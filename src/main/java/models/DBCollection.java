package models;

import java.util.HashSet;
import java.util.Set;

/**
 * This class dictates what parameters classes that contain multiple elems should have.
 * @param <E>
 */
public abstract class DBCollection<E> {
    private int id;
    private String name;
    private boolean download;
    private Set<E> collection;

    public DBCollection(int id, String name, boolean download) {
        this.id = id;
        this.name = name;
        this.download = download;
        this.collection = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<E> getCollection() {
        return collection;
    }

    // These 3 methods are package private since they should be rewritten in each class with the appropriate moduleName
    boolean addToCollection(E e) { return collection.add(e); }
    boolean hasInCollection(E e) { return collection.contains(e); }
    boolean removeFromCollection(E e) { return collection.remove(e); }
    // ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "DBCollection{" +
                "id=" + id +
                ", moduleName='" + name + '\'' +
                ", download=" + download +
                '}';
    }
}
