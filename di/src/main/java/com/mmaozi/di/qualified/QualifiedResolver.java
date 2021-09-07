package com.mmaozi.di.qualified;

import com.google.common.collect.Iterables;
import com.mmaozi.di.exception.CreateInstanceFailedException;
import com.mmaozi.di.utils.ReflectionUtils;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

public class QualifiedResolver {
    public static Class<?> resolveRealClass(Parameter parameter, Collection<Class<?>> classes) {
        Class<?> type = parameter.getType();

        if (!type.isInterface()) {
            return type;
        }

        List<Annotation> annotations = Arrays
                .stream(parameter.getDeclaredAnnotations())
                .filter(annotation -> nonNull(annotation.annotationType().getAnnotation(Qualifier.class)))
                .collect(Collectors.toList());

        if (annotations.isEmpty()) {
            return getImplementWithoutAnnotation(type, classes);
        }


        List<Class<?>> implementClasses = annotations
                .stream()
                .map(annotation -> getMatchedClass(annotation, classes))
                .collect(Collectors.toList());

        return getOnlyImplement(type, implementClasses);
    }

    private static Class<?> getImplementWithoutAnnotation(Class<?> clz, Collection<Class<?>> classes) {
        List<Class<?>> allImplements = ReflectionUtils.getImplements(clz, classes);
        return getOnlyImplement(clz, allImplements);
    }

    private static Class<?> getMatchedClass(Annotation annotation, Collection<Class<?>> classes) {
        List<Class<?>> matchedClasses = ReflectionUtils.getClassWithAnnotation(annotation, classes, true);
        return getOnlyImplement(annotation.annotationType(), matchedClasses);
    }

    private static Class<?> getOnlyImplement(Class<?> clz, List<Class<?>> allImplements) {
        if (allImplements.isEmpty()) {
            throw new CreateInstanceFailedException("No qualified implement for " + clz.getName());
        }

        if (allImplements.size() > 1) {
            throw new CreateInstanceFailedException(String.format("Multiple implementation for %s\n%s", clz.getName(),
                    allImplements.stream()
                                 .map(Class::getName)
                                 .collect(joining("\n")))
            );
        }

        return Iterables.getOnlyElement(allImplements);
    }
}
