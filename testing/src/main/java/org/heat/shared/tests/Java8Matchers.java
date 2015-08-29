package org.heat.shared.tests;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;

import java.util.Optional;

public class Java8Matchers {
    public static Matcher<Optional<?>> OPTIONAL_IS_PRESENT = new BaseMatcher<Optional<?>>() {
        @Override
        public boolean matches(Object item) {
            return item instanceof Optional<?> && ((Optional) item).isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is present");
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Matcher<Optional<T>> isPresent() {
        return (Matcher) OPTIONAL_IS_PRESENT;
    }

    public static <T> Matcher<Optional<T>> notPresent() {
        return IsNot.<Optional<T>>not(isPresent());
    }
}
