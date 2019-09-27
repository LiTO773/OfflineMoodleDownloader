package models;

import controllers.SharedMethods.ListComparisonUtils.ArrayListIgnoreDownload;
import helpers.FileSizeCalculator;

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
    public boolean equalsWithoutDownload(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBCollection<?> that = (DBCollection<?>) o;

        ArrayListIgnoreDownload<?> thisCollection = new ArrayListIgnoreDownload<>(this.collection);
        ArrayListIgnoreDownload<?> thatCollection = new ArrayListIgnoreDownload<>(that.collection);

        return this.getName().equals(that.getName()) &&
                id == that.id &&
                thisCollection.stream().allMatch(c -> thatCollection.indexOf(c) != -1) &&
                this.collection.size() == that.collection.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBCollection<?> that = (DBCollection<?>) o;

        return this.getName().equals(that.getName()) &&
                id == that.id &&
                this.collection.stream().allMatch(c -> that.collection.contains(c)) &&
                this.collection.size() == that.collection.size() &&
                this.isDownloadable() == that.isDownloadable();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, collection);
    }

    public int totalSize() {
        int total = 0;
        for (E elem: collection) {
            if (elem instanceof File) {
                total += ((File) elem).getFileSize();
            } else if (elem instanceof DBCollection) {
                total += ((DBCollection) elem).totalSize();
            }
        }

        return total;
    }

    public String sizeString() {
        return FileSizeCalculator.calculate(totalSize());
    }
}
