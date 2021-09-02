package com.mmaozi.di.scope;

import com.mmaozi.di.container.IContainer;
import com.mmaozi.di.scope.annotations.Prototype;

import java.util.Objects;

public class PrototypeProvider implements ScopeProvider {

    @Override
    public <T> boolean available(Class<T> clz) {
        return Objects.nonNull(clz.getDeclaredAnnotation(Prototype.class));
    }

    @Override
    public <T> T getInstance(Class<T> clz, IContainer container, Object context) {
        return container.newInstance(clz);
    }
}
