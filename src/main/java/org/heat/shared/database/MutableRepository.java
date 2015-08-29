package org.heat.shared.database;

public interface MutableRepository<T> extends Repository<T> {
    void save(T o);
    void remove(T o);
}
