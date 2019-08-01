package models;

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

    public DBCollection(int id, String name, boolean download, Set<E> collection) {
        this.id = id;
        this.name = name;
        this.download = download;
        this.collection = collection;
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

    public void setCollection(Set<E> collection) {
        this.collection = collection;
    }

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
                ", name='" + name + '\'' +
                ", download=" + download +
                '}';
    }
}
