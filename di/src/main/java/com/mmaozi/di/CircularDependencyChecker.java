package com.mmaozi.di;

import com.mmaozi.di.exception.CreateInstanceFailedException;

import java.util.Stack;
import java.util.stream.IntStream;

public class CircularDependencyChecker {

    private final Stack<Class<?>> creationStack = new Stack<>();

    public void in(Class<?> clazz) {
        checkCircularDependency(clazz);
        creationStack.push(clazz);
    }

    public void out() {
        creationStack.pop();
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
}
