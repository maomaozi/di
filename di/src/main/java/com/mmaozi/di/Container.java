package com.mmaozi.di;

import com.mmaozi.di.exception.CreateInstanceFailedException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
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

        Constructor<?> constructor = ReflectionUtils.getInjectableConstructor(clazz)
                                                    .orElseGet(() -> getNoArgsConstructor(clazz));
        ArrayList<Object> parameters = new ArrayList<>();

        Arrays.stream(constructor.getParameters())
              .sequential()
              .forEach(parameter -> parameters.add(getInstance(parameter.getType())));

        try {
            return constructor.newInstance(parameters.toArray());
        } catch (Exception ex) {
            throw new CreateInstanceFailedException("Cannot create new instance for class " + clazz.getSimpleName(), ex);
        }
    }

    private Constructor<?> getNoArgsConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new CreateInstanceFailedException("Cannot find proper constructor for class " + clazz.getSimpleName());
        }
    }
}
