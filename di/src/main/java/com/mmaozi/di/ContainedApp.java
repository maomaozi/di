package com.mmaozi.di;

import com.google.common.collect.Iterables;
import com.mmaozi.di.annotations.Main;
import com.mmaozi.di.exception.AppStartFailedException;
import com.mmaozi.di.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContainedApp {

    private final Container container;
    private final Class<?> mainClass;

    public ContainedApp(Class<?> mainClass) {
        container = new Container();
        this.mainClass = mainClass;
    }

    public <T> T getInstance(Class<T> clz) {
        return container.getInstance(clz);
    }


    public void run(Object... args) {

        List<String> classes = ReflectionUtils.findAllClassesInPackage(mainClass.getPackage());

        scanPackages(classes);

        List<Method> mainMethods = Arrays.stream(mainClass.getMethods())
                                         .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(Main.class)))
                                         .collect(Collectors.toList());

        if (mainMethods.isEmpty()) {
            throw new AppStartFailedException("Run application failed, no proper @Main");
        } else if (mainMethods.size() != 1) {
            throw new AppStartFailedException("Run application failed, multiple @Main found");
        }

        Method main = Iterables.getLast(mainMethods);
        try {
            main.invoke(container.getInstance(mainClass), args);
        } catch (Exception ex) {
            throw new AppStartFailedException(String.format("Run application failed, run %s method failed", main.getName()), ex);
        }
    }

    private void scanPackages(List<String> classes) {
        classes.stream()
               .filter(className -> !className.startsWith("com.mmaozi.di"))
               .map(this::getClass)
               .forEach(container::register);
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new AppStartFailedException("Run application failed, cannot load " + className, ex);
        }
    }
}
