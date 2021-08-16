package com.mmaozi.di;

import com.mmaozi.di.exception.CreateInstanceFailedException;

import java.util.HashSet;
import java.util.Set;

public class Container {

    private final Set<Class<?>> registeredClass = new HashSet<>();

    public void register(Class<?> clazz) {
        registeredClass.add(clazz);
    }

    public Object getInstance(Class<?> clazz) {
        if (!registeredClass.contains(clazz)) {
            throw new CreateInstanceFailedException(clazz.getSimpleName() + " is not register in container");
        }

        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new CreateInstanceFailedException(
                    "Cannot find proper constructor for class " + clazz.getSimpleName());
        }
    }
}
