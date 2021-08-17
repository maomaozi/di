package com.mmaozi.di;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionUtils {

    public static Optional<Constructor<?>> getInjectableConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                     .filter(constructor -> Arrays.stream(constructor.getDeclaredAnnotations())
                                                  .sequential()
                                                  .anyMatch(annotation -> annotation.annotationType()
                                                                                    .equals(Inject.class)))
                     .findFirst()
                     .map(constructor -> {
                         constructor.setAccessible(true);
                         return constructor;
                     });
    }
}
