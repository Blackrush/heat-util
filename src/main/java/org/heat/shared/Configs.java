package org.heat.shared;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class Configs {
    private Configs() {}
    
    public static final Config CACHE_BUILDER_DEFAULTS = ConfigFactory.parseMap(ImmutableMap.<String, Object>builder()
            .put("initial-capacity", -1L)
            .put("maximum-size", -1L)
            .put("maximum-weight", -1L)
            .put("concurrency-level", -1L)
            .put("weak-keys", false)
            .put("soft-values", false)
            .put("weak-values", false)
            .put("record-stats", false)
            .put("expire-after-access", -1L)
            .put("expire-after-write", -1L)
            .put("refresh-after-write", -1L)
    .build());
    
    public static CacheBuilder<Object, Object> newCacheBuilder(Config config) {
        config = config.withFallback(CACHE_BUILDER_DEFAULTS);

        int initialCapacity = config.getInt("initial-capacity");
        long maximumSize = config.getLong("maximum-size");
        long maximumWeight = config.getLong("maximum-weight");
        int concurrencyLevel = config.getInt("concurrency-level");
        long expireAfterAccess = config.getLong("expire-after-access");
        long expireAfterWrite = config.getLong("expire-after-write");
        long refreshAfterWrite = config.getLong("refresh-after-write");

        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (initialCapacity != -1) {
            builder.initialCapacity(initialCapacity);
        }
        if (maximumSize != -1L) {
            builder.maximumSize(maximumSize);
        }
        if (maximumWeight != -1L) {
            builder.maximumWeight(maximumWeight);
        }
        if (concurrencyLevel != -1) {
            builder.concurrencyLevel(concurrencyLevel);
        }
        if (config.getBoolean("weak-keys")) {
            builder.weakKeys();
        }
        if (config.getBoolean("soft-values")) {
            builder.softValues();
        }
        if (config.getBoolean("weak-values")) {
            builder.weakValues();
        }
        if (config.getBoolean("record-stats")) {
            builder.recordStats();
        }
        if (expireAfterAccess != -1L) {
            builder.expireAfterAccess(expireAfterAccess, MILLISECONDS);
        }
        if (expireAfterWrite != -1L) {
            builder.expireAfterWrite(expireAfterWrite, MILLISECONDS);
        }
        if (refreshAfterWrite != -1L) {
            builder.refreshAfterWrite(refreshAfterWrite, MILLISECONDS);
        }
        return builder;
    }
}
