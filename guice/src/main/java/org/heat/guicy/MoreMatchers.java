package org.heat.guicy;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

public final class MoreMatchers {
    private MoreMatchers() {}

    public static Matcher<TypeLiteral<?>> subclassesOf(Class<?> klass) {
        return new AbstractMatcher<TypeLiteral<?>>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return klass.isAssignableFrom(typeLiteral.getRawType());
            }
        };
    }
}
