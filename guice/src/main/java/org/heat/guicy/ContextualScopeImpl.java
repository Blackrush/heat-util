package org.heat.guicy;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.util.Types;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
class ContextualScopeImpl<Ctx> implements ContextualScope<Ctx> {
    @NonNull final Class<? extends Annotation> scopeAnnotation;
    @NonNull final Key<Ctx> contextKey;

    ThreadLocal<Ctx> context = new ThreadLocal<>();
    Map<Ctx, Map<Key<?>, Object>> scoped = new HashMap<>();

    @Override
    public void configure(Binder binder) {
        binder.bindScope(scopeAnnotation, this);
        binder.bind(contextKey).toProvider(this);
        binder.bind(thisKey()).toInstance(this);
    }

    @Override
    public Ctx get() {
        Ctx ctx = context.get();
        if (ctx == null) {
            throw new IllegalStateException();
        }
        return ctx;
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            Ctx ctx = get();
            Map<Key<?>, Object> instances = scoped.computeIfAbsent(ctx, x -> new HashMap<>());
            Object instance = instances.computeIfAbsent(key, x -> unscoped.get());

            @SuppressWarnings("unchecked")
            T res = (T) instance;
            return res;
        };
    }

    public Key<ContextualScope<Ctx>> thisKey() {
        Type thisType = Types.newParameterizedType(ContextualScope.class, contextKey.getTypeLiteral().getRawType());

        @SuppressWarnings("unchecked")
        Key<ContextualScope<Ctx>> res = (Key) Key.get(thisType);
        return res;
    }

    public void enter(Ctx ctx) {
        if (context.get() != null) {
            throw new IllegalStateException();
        }
        context.set(ctx);
    }

    public void quit() {
        context.remove();
    }

    public void clean(Ctx ctx) {
        scoped.remove(ctx);
    }
}
