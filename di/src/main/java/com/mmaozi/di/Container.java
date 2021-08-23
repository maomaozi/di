package com.mmaozi.di;

import com.mmaozi.di.exception.CreateInstanceFailedException;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
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

public class Container {

    private final Set<Class<?>> registeredClass = new HashSet<>();
    private final Stack<Class<?>> creationStack = new Stack<>();

    public void register(Class<?> clazz) {
        registeredClass.add(clazz);
    }

    public Object getInstance(Class<?> clazz) {
        if (!registeredClass.contains(clazz)) {
            throw new CreateInstanceFailedException(clazz.getSimpleName() + " is not register in container");
        }

        checkCircularDependency(clazz);

        Constructor<?> constructor = ReflectionUtils.getInjectableConstructor(clazz)
                                                    .orElseGet(() -> getNoArgsConstructor(clazz));

        List<Object> parameters = instantiateParameters(clazz, constructor);

        try {
            return constructor.newInstance(parameters.toArray());
        } catch (Exception ex) {
            throw new CreateInstanceFailedException("Cannot create new instance for class " + clazz.getSimpleName(), ex);
        }
    }

    private List<Object> instantiateParameters(Class<?> clazz, Constructor<?> constructor) {

        creationStack.push(clazz);
        List<Object> parameters = Arrays.stream(constructor.getParameters())
                                        .map(this::resolveRealType)
                                        .map(this::getInstance)
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
                     .map(Annotation::annotationType)
                     .filter(annotation -> Objects.nonNull(annotation.getAnnotation(Qualifier.class)))
                     .map(this::getClassWithAnnotation)
                     .findFirst()
                     .orElseThrow(() -> new CreateInstanceFailedException("No qualified implement for interface " + parameter.getClass().getSimpleName()));

    }

    private Class<?> getClassWithAnnotation(Class<? extends Annotation> annotation) {
        return registeredClass
                .stream()
                .filter(clz -> Objects.nonNull(clz.getDeclaredAnnotation(annotation)))
                .findFirst()
                .orElseThrow(() -> new CreateInstanceFailedException("No proper class found for qualifier " + annotation.getSimpleName()));
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

        for (int i = idx; i < creationStack.size(); ++i) {
            stringBuilder
                    .append("Depends ")
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
