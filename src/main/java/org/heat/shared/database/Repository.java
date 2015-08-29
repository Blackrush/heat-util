package org.heat.shared.database;

import java.util.Optional;

public interface Repository<T> {
    Optional<T> find(long id);
}
