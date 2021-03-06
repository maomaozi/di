package com.mmaozi.di.utils;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static Optional<Constructor<?>> getConstructorWithAnnotationType(Class<?> clazz, Class<?> annotationType) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                     .filter(constructor -> Arrays.stream(constructor.getDeclaredAnnotations())
                                                  .anyMatch(annotation -> annotation.annotationType().equals(annotationType)))
                     .findFirst()
                     .map(constructor -> {
                         constructor.setAccessible(true);
                         return constructor;
                     });
    }

    public static boolean compareAnnotation(Annotation lhs, Annotation rhs) throws InvocationTargetException, IllegalAccessException {

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

    public static List<String> findAllClassesInPackage(Package pack) {

        String packageName = pack.getName();

        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                            .getAllClasses()
                            .stream()
                            .map(ClassPath.ClassInfo::getName)
                            .filter(className -> className.startsWith(packageName))
                            .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<Class<?>> getImplements(Class<?> interfaceType, Collection<Class<?>> classes) {
        return classes
                .stream()
                .filter(clz -> Arrays.asList(clz.getInterfaces()).contains(interfaceType))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> getClassWithAnnotation(Annotation annotation, Collection<Class<?>> classes, boolean strictMatch) {
        return classes
                .stream()
                .filter(clz -> isClassMatchWithAnnotation(clz, annotation, strictMatch))
                .collect(Collectors.toList());
    }

    private static boolean isClassMatchWithAnnotation(Class<?> clz, Annotation annotation, boolean strictMatch) {
        Annotation declaredAnnotation = clz.getDeclaredAnnotation(annotation.annotationType());
        if (Objects.isNull(declaredAnnotation)) {
            return false;
        }

        try {
            return strictMatch ? ReflectionUtils.compareAnnotation(declaredAnnotation, annotation) :
                    declaredAnnotation.annotationType().equals(annotation.annotationType());
        } catch (Exception e) {
            return false;
        }
    }

}