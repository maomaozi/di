package com.mmaozi.di.container;

import com.mmaozi.di.scope.ScopeProvider;

import java.lang.annotation.Annotation;
import java.util.List;

public interface IContainer {
    void register(Class<?> clazz);

    <T> T getInstance(Class<T> clazz);

    <T> T getInstance(Class<T> clazz, Object context);

    <T> T newInstance(Class<T> clazz);

    List<Object> getInstances(Annotation annotation);

    void addScope(ScopeProvider scopeProvider);
}
