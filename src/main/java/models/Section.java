package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class Section extends DBCollection<Content> implements Serializable {
    public Section(int id, String name, boolean download) {
        super(id, name, download);
    }

    public Section(Section obj) {
        this(obj.getId(), obj.getName(), obj.isDownloadable());

        for (Content c: obj.getCollection()) {
            if (c instanceof File) {
                super.addToCollection(new File((File) c));
            } else if (c instanceof Folder) {
                super.addToCollection(new Folder((Folder) c));
            }
        }
    }

    // Rewritten methods to better represent the class
    public boolean addContent(Content m) {
        return addToCollection(m);
    }

    public boolean hasContent(Content m) {
        return hasInCollection(m);
    }

    public boolean removeContent(Content m) {
        return removeFromCollection(m);
    }
}
