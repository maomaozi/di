package com.mmaozi.di;

import com.mmaozi.di.exception.CreateInstanceFailedException;
import com.mmaozi.di.qualified.QualifiedResolver;
import com.mmaozi.di.scope.ScopeProvider;
import com.mmaozi.di.scope.SingletonProvider;
import com.mmaozi.di.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Container {

    private final Set<Class<?>> registeredClass = new HashSet<>();
    private final List<ScopeProvider> providers = List.of(new SingletonProvider());

    private final CircularDependencyChecker circularDependencyChecker = new CircularDependencyChecker();

    public void register(Class<?> clazz) {
        registeredClass.add(clazz);
    }

    public <T> T getInstance(Class<T> clazz) {
        if (!registeredClass.contains(clazz)) {
            throw new CreateInstanceFailedException(clazz.getSimpleName() + " is not register in container");
        }

        ScopeProvider scopeProvider = providers.stream()
                                               .filter(provider -> provider.available(clazz))
                                               .findFirst()
                                               .orElse(null);

        if (nonNull(scopeProvider)) {
            T instance = scopeProvider.getInstance(clazz);
            if (nonNull(instance)) {
                return instance;
            }
        }

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

    public List<Object> getInstances(Annotation annotation) {
        return ReflectionUtils.getClassWithAnnotation(annotation, registeredClass, false)
                              .stream()
                              .map(this::getInstance)
                              .collect(Collectors.toList());
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
