package org.heat.guicy;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;

public final class MoreScopes {
    private MoreScopes() {}

    public static <Ctx> ContextualScope<Ctx> contextual(Class<? extends Annotation> scopeAnnotation, Key<Ctx> contextKey) {
        return new ContextualScopeImpl<>(scopeAnnotation, contextKey);
    }

    public static <Ctx> ContextualScope<Ctx> contextual(Class<? extends Annotation> scopeAnnotation, Class<Ctx> contextClass) {
        return contextual(scopeAnnotation, Key.get(contextClass));
    }

    public static <Ctx> ContextualScope<Ctx> contextual(Class<? extends Annotation> scopeAnnotation, TypeLiteral<Ctx> contextType) {
        return contextual(scopeAnnotation, Key.get(contextType));
    }
}
