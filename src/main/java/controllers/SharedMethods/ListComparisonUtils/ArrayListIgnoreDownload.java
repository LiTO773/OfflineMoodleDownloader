package controllers.SharedMethods.ListComparisonUtils;

import models.Downloadable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class for comparisons, ignores the download property when  trying to find an object
 */
public class ArrayListIgnoreDownload<E> extends ArrayList<E> {
    public ArrayListIgnoreDownload(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayListIgnoreDownload() {
    }

    public ArrayListIgnoreDownload(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Downloadable)) return -1;

        for (int i = 0; i < super.size(); i++) {
            if ((((Downloadable) o).equalsWithoutDownload(get(i)))) {
                return i;
            }
        }

        return -1;
    }
}
