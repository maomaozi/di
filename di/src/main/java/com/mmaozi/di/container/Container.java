package com.mmaozi.di.container;

import com.mmaozi.di.CircularDependencyChecker;
import com.mmaozi.di.exception.CreateInstanceFailedException;
import com.mmaozi.di.qualified.QualifiedResolver;
import com.mmaozi.di.scope.BasicScopeProvider;
import com.mmaozi.di.scope.SingletonProvider;
import com.mmaozi.di.utils.ReflectionUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Container implements IContainer {

    private final Set<Class<?>> registeredClass = new HashSet<>();
    private final Deque<BasicScopeProvider> providers = new ArrayDeque<>();

    public Container() {
        providers.addFirst(new SingletonProvider());
        providers.addFirst(new BasicScopeProvider());
    }

    private final CircularDependencyChecker circularDependencyChecker = new CircularDependencyChecker();

    public void register(Class<?> clazz) {
        registeredClass.add(clazz);
    }

    public <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, null);
    }

    public <T> T getInstance(Class<T> clazz, Object context) {
        if (!registeredClass.contains(clazz)) {
            throw new CreateInstanceFailedException(clazz.getSimpleName() + " is not register in container");
        }

        return providers.stream()
                        .filter(provider -> provider.available(clazz))
                        .findFirst()
                        .map(provider -> provider.getInstance(clazz, this, context))
                        .orElseThrow(() -> new CreateInstanceFailedException("No proper scope provider for class " + clazz.getSimpleName()));
    }

    public List<Object> getInstances(Annotation annotation) {
        return ReflectionUtils.getClassWithAnnotation(annotation, registeredClass, false)
                              .stream()
                              .map(this::getInstance)
                              .collect(Collectors.toList());
    }

    public <T> T newInstance(Class<T> clazz) {
        Constructor<?> constructor = ReflectionUtils.getConstructorWithAnnotationType(clazz, Inject.class)
                                                    .orElseGet(() -> getNoArgsConstructor(clazz));

        List<Object> parameters = instantiateParameters(clazz, constructor);

        try {
            return (T) constructor.newInstance(parameters.toArray());
        } catch (Exception ex) {
            throw new CreateInstanceFailedException("Cannot create instance for class " + clazz.getSimpleName(), ex);
        }
    }

    @Override
    public void addScope(BasicScopeProvider scopeProvider) {
        providers.addFirst(scopeProvider);
    }

    private List<Object> instantiateParameters(Class<?> clazz, Constructor<?> constructor) {

        circularDependencyChecker.in(clazz);
        List<Object> parameters = Arrays.stream(constructor.getParameters())
                                        .map(annotation -> QualifiedResolver.resolveRealClass(annotation, registeredClass))
                                        .map(this::getInstance)
                                        .collect(Collectors.toList());
        circularDependencyChecker.out();

        return parameters;
    }

    private Constructor<?> getNoArgsConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new CreateInstanceFailedException("Cannot find proper constructor for class " + clazz.getSimpleName());
        }
    }
}
