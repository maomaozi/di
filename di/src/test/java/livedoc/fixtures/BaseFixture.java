package livedoc.fixtures;

import com.mmaozi.di.container.Container;
import livedoc.utils.ReflectionUtils;

public class BaseFixture {
    private Container container;

    public void initContainer() {
        container = new Container();
    }

    public void registerClass(String className) throws ClassNotFoundException {
        container.register(ReflectionUtils.getClassByName(className));
    }

    public Object getInstance(String className) throws ClassNotFoundException {
        return container.getInstance(ReflectionUtils.getClassByName(className));
    }

    public String getClassName(Object clz) {
        return clz.getClass().getSimpleName();
    }

    public void registerClasses(String classes) throws ClassNotFoundException {
        for (String className : classes.split(",")) {
            registerClass(className);
        }
    }

}
