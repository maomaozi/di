package com.mmaozi.di.scope;

import com.mmaozi.di.container.IContainer;
import com.mmaozi.di.scope.annotations.Prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BasicScopeProvider {

    private final Map<Object, Object> cache = new HashMap<>();

    public <T> boolean available(Class<T> clz) {
        return Objects.nonNull(clz.getDeclaredAnnotation(Prototype.class));
    }

    public <T> T getInstance(Class<T> clz, IContainer container, Object context) {
        return container.newInstance(clz);
    }

    public Object putAndReturn(Object key, Object instance) {
        cache.put(key, instance);
        return instance;
    }

    public Optional<Object> getOptional(Object key) {
        return Optional.ofNullable(cache.get(key));
    }

    public void remove(Object key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}
