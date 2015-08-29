package org.heat.shared;

import java.util.Collection;
import java.util.LinkedList;

public final class Collections {
    private Collections() {}

    public static interface ToByteFunction<T> {
        byte apply(T param);
    }

    public static <T> byte[] export(Collection<T> values, ToByteFunction<T> fn) {
        byte[] res = new byte[values.size()];
        int i = 0;
        for (T value : values) {
            res[i++] = fn.apply(value);
        }
        return res;
    }

    @SuppressWarnings("Convert2streamapi")
    public static <T> LinkedList<T> cloneLinkedList(LinkedList<T> list) {
        LinkedList<T> res = new LinkedList<>();
        addAllLinkedList(res, list);
        return res;
    }

    @SuppressWarnings("Convert2streamapi")
    public static <T> void addAllLinkedList(LinkedList<T> self, LinkedList<T> list) {
        for (T item : list) {
            self.add(item);
        }
    }
}
