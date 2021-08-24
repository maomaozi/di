package com.mmaozi.di.utils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    public static boolean compareAnnotation(Annotation lhs, Annotation rhs) throws Exception {

        if (lhs.getClass() != rhs.getClass()) {
            return false;
        }

        for (Method method : lhs.annotationType().getDeclaredMethods()) {

            Object lhsValue = method.invoke(lhs);
            Object rhsValue = method.invoke(rhs);

            if (lhsValue == rhsValue) {
                continue;
            } else if (lhsValue == null) {
                return false;
            }

            if (!lhsValue.equals(rhsValue)) {
                return false;
            }
        }
        return true;
    }

}