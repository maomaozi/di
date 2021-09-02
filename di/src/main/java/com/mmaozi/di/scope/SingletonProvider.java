package com.mmaozi.di.scope;

import com.mmaozi.di.container.IContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SingletonProvider implements ScopeProvider {

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    @Override
    public <T> boolean available(Class<T> clz) {
        return true;
    }

    @Override
    public <T> T getInstance(Class<T> clz, IContainer container, Object context) {
        return (T) Optional.ofNullable(singletons.get(clz))
                           .orElseGet(() -> {
                               T instance = container.newInstance(clz);
                               singletons.put(clz, instance);
                               return instance;
                           });
        // has issue with recursive update with code below
        // return (T) singletons.computeIfAbsent(clz, key -> container.newInstance(clz));
    }
}
