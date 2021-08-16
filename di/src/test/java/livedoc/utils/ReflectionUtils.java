package livedoc.utils;

public class ReflectionUtils {

    public static Class<?> getClassByName(String packageName, String className) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%s", packageName, className));
    }

    public static Class<?> getClassByName(String className) throws ClassNotFoundException {
        return Class.forName(String.format("livedoc.fixtures.%s", className));
    }
}
