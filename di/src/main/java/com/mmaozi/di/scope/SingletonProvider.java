package com.mmaozi.di.scope;

import javax.inject.Singleton;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingletonProvider implements ScopeProvider {

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    @Override
    public <T> boolean available(Class<T> clz, AnnotatedElement from) {
        return Objects.nonNull(clz.getDeclaredAnnotation(Singleton.class)) ||
                (Objects.nonNull(from) && Objects.nonNull(from.getDeclaredAnnotation(Singleton.class)));
    }

    @Override
    public <T> T getInstance(Class<T> clz) {
        return (T) singletons.get(clz);
    }

    @Override
    public <T> void registerInstance(T instance) {
        singletons.put(instance.getClass(), instance);
    }
}
