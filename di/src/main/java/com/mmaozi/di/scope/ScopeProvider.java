package com.mmaozi.di.scope;

import java.lang.reflect.AnnotatedElement;

public interface ScopeProvider {

    <T> boolean available(Class<T> clz, AnnotatedElement from);

    <T> T getInstance(Class<T> clz);

    <T> void registerInstance(T instance);
}
