package com.mmaozi.di;

import com.google.common.collect.Iterables;
import com.mmaozi.di.exception.CreateInstanceFailedException;
import com.mmaozi.di.scope.ScopeProvider;
import com.mmaozi.di.scope.SingletonProvider;
import com.mmaozi.di.utils.ReflectionUtils;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

public class Container {

    private final Set<Class<?>> registeredClass = new HashSet<>();
    private final Stack<Class<?>> creationStack = new Stack<>();
    private final List<ScopeProvider> providers = List.of(new SingletonProvider());

    public void register(Class<?> clazz) {
        registeredClass.add(clazz);
    }

    public <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, null);
    }

    private <T> T getInstance(Class<T> clazz, AnnotatedElement from) {
        if (!registeredClass.contains(clazz)) {
            throw new CreateInstanceFailedException(clazz.getSimpleName() + " is not register in container");
        }

        ScopeProvider scopeProvider = providers.stream()
                                               .filter(provider -> provider.available(clazz, from))
                                               .findFirst()
                                               .orElse(null);

        if (nonNull(scopeProvider)) {
            T instance = scopeProvider.getInstance(clazz);
            if (nonNull(instance)) {
                return instance;
            }
        }

        checkCircularDependency(clazz);

        Constructor<?> constructor = ReflectionUtils.getInjectableConstructor(clazz)
                                                    .orElseGet(() -> getNoArgsConstructor(clazz));

        List<Object> parameters = instantiateParameters(clazz, constructor);

        try {
            Object instance = constructor.newInstance(parameters.toArray());

            if (nonNull(scopeProvider)) {
                scopeProvider.registerInstance(instance);
            }
            return (T) instance;
        } catch (Exception ex) {
            throw new CreateInstanceFailedException("Cannot create new instance for class " + clazz.getSimpleName(), ex);
        }
    }

    private List<Object> instantiateParameters(Class<?> clazz, Constructor<?> constructor) {

        creationStack.push(clazz);
        List<Object> parameters = Arrays.stream(constructor.getParameters())
                                        .map(parameter -> getInstance(resolveRealType(parameter), parameter))
                                        .collect(Collectors.toList());
        creationStack.pop();

        return parameters;
    }

    private Class<?> resolveRealType(Parameter parameter) {
        Class<?> type = parameter.getType();

        if (!type.isInterface()) {
            return type;
        }

        return Arrays.stream(parameter.getDeclaredAnnotations())
                     .filter(annotation -> nonNull(annotation.annotationType().getAnnotation(Qualifier.class)))
                     .map(this::getClassWithAnnotation)
                     .findFirst()
                     .orElseThrow(() -> new CreateInstanceFailedException("No qualified implement for interface " + parameter.getClass().getSimpleName()));

    }

    private Class<?> getClassWithAnnotation(Annotation annotation) {
        List<Class<?>> matchedClasses = registeredClass
                .stream()
                .filter(clz -> matchClassWithAnnotation(clz, annotation))
                .collect(Collectors.toList());

        if (matchedClasses.isEmpty()) {
            throw new CreateInstanceFailedException("No proper class found for qualifier @" + annotation.annotationType().getSimpleName());
        }

        if (matchedClasses.size() > 1) {
            throw new CreateInstanceFailedException(String.format("More than one qualified class found for qualifier @%s:\n%s",
                    annotation.annotationType().getSimpleName(),
                    matchedClasses.stream()
                                  .map(Class::getName)
                                  .collect(Collectors.joining("\n"))
            ));
        }

        return Iterables.getLast(matchedClasses);
    }

    private boolean matchClassWithAnnotation(Class<?> clz, Annotation annotation) {
        Annotation declaredAnnotation = clz.getDeclaredAnnotation(annotation.annotationType());
        if (Objects.isNull(declaredAnnotation)) {
            return false;
        }

        try {
            return ReflectionUtils.compareAnnotation(declaredAnnotation, annotation);
        } catch (Exception ex) {
            throw new CreateInstanceFailedException("Unexpected exception", ex);
        }
    }

    private void checkCircularDependency(Class<?> clazz) {
        IntStream.range(0, creationStack.size())
                 .filter(idx -> creationStack.get(idx).equals(clazz))
                 .findFirst()
                 .ifPresent(idx -> {
                     throw new CreateInstanceFailedException(buildCircularDependencyErrorMessage(idx));
                 });
    }

    private String buildCircularDependencyErrorMessage(int idx) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Found circular dependencies while creating class ");
        stringBuilder.append(creationStack.get(idx).getName());
        stringBuilder.append("\n-----------------------------\n");
        stringBuilder.append(creationStack.get(idx).getSimpleName());

        for (int i = idx; i < creationStack.size(); ++i) {
            stringBuilder
                    .append("\n ↓ \n")
                    .append(creationStack.get(i).getSimpleName())
                    .append("\n");
        }

        return stringBuilder.toString();
    }


    private Constructor<?> getNoArgsConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new CreateInstanceFailedException("Cannot find proper constructor for class " + clazz.getSimpleName());
        }
    }
}
