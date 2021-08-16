package livedoc.fixtures;

import com.mmaozi.di.Container;
import livedoc.utils.ReflectionUtils;

public class BaseFixture {
    private Container container;
    private Object clazz;

    public void initContainer() {
        container = new Container();
    }

    public void registerClass(String className) throws ClassNotFoundException {
        container.register(ReflectionUtils.getClassByName(className));
    }

    public void getInstance(String className) throws ClassNotFoundException {
        clazz = container.getInstance(ReflectionUtils.getClassByName(className));
    }

    public String getActualClazzName() {
        return clazz.getClass().getSimpleName();
    }
}
