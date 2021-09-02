package com.mmaozi.di.scope;

import com.mmaozi.di.container.IContainer;

public interface ScopeProvider {

    <T> boolean available(Class<T> clz);

    <T> T getInstance(Class<T> clz, IContainer container, Object context);
}
