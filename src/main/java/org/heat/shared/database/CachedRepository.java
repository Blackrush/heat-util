package org.heat.shared.database;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Optional;

public abstract class CachedRepository<T> implements Repository<T> {
    protected final Repository<T> repository;
    final LoadingCache<Object, Optional<T>> cache;

    public CachedRepository(Repository<T> repository, CacheBuilder<Object, Object> cacheBuilder) {
        this.repository = repository;
        this.cache = cacheBuilder.build(new CacheLoader<Object, Optional<T>>() {
            @Override
            public Optional<T> load(Object key) throws Exception {
                return CachedRepository.this.load(key);
            }
        });
    }

    protected abstract Optional<T> load(Object key);

    protected final Optional<T> findCache(Object key) {
        return cache.getUnchecked(key);
    }

}
