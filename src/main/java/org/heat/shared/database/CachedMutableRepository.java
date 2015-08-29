package org.heat.shared.database;

import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.Optional;

public abstract class CachedMutableRepository<T> extends CachedRepository<T> implements MutableRepository<T> {
    private final MutableRepository<T> mutRepo;

    public CachedMutableRepository(MutableRepository<T> repository, CacheBuilder<Object, Object> cacheBuilder) {
        super(repository, cacheBuilder);
        this.mutRepo = repository;
    }

    protected abstract List<Object> getKeys(T o);

    @Override
    public final void save(T o) {
        mutRepo.save(o);
        Optional<T> opt = Optional.of(o);
        getKeys(o).forEach(key -> cache.put(key, opt));
    }

    @Override
    public final void remove(T o) {
        mutRepo.remove(o);
        cache.invalidateAll(getKeys(o));
    }
}
