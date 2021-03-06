package com.mmaozi.di.scope;

import com.mmaozi.di.container.IContainer;

public class SingletonProvider extends BasicScopeProvider {

    @Override
    public <T> boolean available(Class<T> clz) {
        return true;
    }

    @Override
    public <T> T getInstance(Class<T> clz, IContainer container, Object context) {
        return (T) getOptional(clz).orElseGet(() -> putAndReturn(clz, container.newInstance(clz)));

        // has issue with recursive update with code below
        // return (T) singletons.computeIfAbsent(clz, key -> container.newInstance(clz));
    }
}
