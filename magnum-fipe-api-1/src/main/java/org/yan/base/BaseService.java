package org.yan.base;

import io.vertx.core.impl.logging.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class BaseService {
    protected <T> Optional<T> safeExecute(Supplier<T> action) {
        try {
            return Optional.ofNullable(action.get());
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
