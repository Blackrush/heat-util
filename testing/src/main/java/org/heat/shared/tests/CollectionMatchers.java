package org.heat.shared.tests;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;

import java.util.Collection;
import java.util.Map;

public final class CollectionMatchers {
    private CollectionMatchers() {}

    public static final class SizeMatcher<T> extends BaseMatcher<T> {
        public final int size;

        public SizeMatcher(int size) {
            this.size = size;
        }

        @Override
        public boolean matches(Object item) {
            if (item instanceof Collection) {
                return ((Collection) item).size() == size;
            } else if (item instanceof Map) {
                return ((Map) item).size() == size;
            }
            throw new Error("unknown collection " + item.getClass());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has size");
            description.appendValue(size);
        }
    }

    public static final Matcher<?> IS_EMPTY_MATCHER = new BaseMatcher<Object>() {
        @Override
        public boolean matches(Object item) {
            if (item instanceof Collection) {
                return ((Collection) item).isEmpty();
            } else if (item instanceof Map) {
                return ((Map) item).isEmpty();
            }
            throw new Error("unknown collection " + item.getClass());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is empty");
        }
    };

    public static final class AllIsMatcher<TT> extends BaseMatcher {
        private final Matcher<TT> matcher;

        public AllIsMatcher(Matcher<TT> matcher) {
            this.matcher = matcher;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof Iterable)) {
                return false;
            }

            Iterable it = (Iterable) item;
            for (Object o : it) {
                if (!matcher.matches(o)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("all is");
            matcher.describeTo(description);
        }
    }

    public static <T> Matcher<T> hasSize(int len) {
        return new SizeMatcher<>(len);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> isEmpty() {
        return (Matcher<T>) IS_EMPTY_MATCHER;
    }

    public static <T> Matcher<T> notEmpty() {
        return new IsNot<>(isEmpty());
    }

    @SuppressWarnings("unchecked")
    public static <TT, T extends Iterable<TT>> Matcher<T> allIs(Matcher<TT> matcher) {
        return new AllIsMatcher<>(matcher);
    }
}
