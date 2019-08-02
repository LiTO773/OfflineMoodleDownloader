package models;

import java.util.Set;

public class Section extends DBCollection<Module> {
    public Section(int id, String name, boolean download) {
        super(id, name, download);
    }

    // Rewritten methods to better represent the class
    public boolean addModule(Module m) {
        return addToCollection(m);
    }

    public boolean hasModule(Module m) {
        return hasInCollection(m);
    }

    public boolean removeModule(Module m) {
        return removeFromCollection(m);
    }
}
