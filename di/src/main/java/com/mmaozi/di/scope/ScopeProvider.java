package com.mmaozi.di.scope;

public interface ScopeProvider {

    <T> boolean available(Class<T> clz);

    <T> T getInstance(Class<T> clz);

    <T> void registerInstance(T instance);
}
