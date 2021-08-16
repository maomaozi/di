package livedoc.fixtures;

import com.mmaozi.di.Container;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.utils.ReflectionUtils;

@FuShengTest
public class ContainerTest {

    private Container container;
    private Object clazz;

    public void initContainer() {
        container = new Container();
    }

    public void register(String className) throws ClassNotFoundException {
        container.register(ReflectionUtils.getClassByName(className));
    }

    public void getFoo(String className) throws ClassNotFoundException {
        clazz = container.getInstance(ReflectionUtils.getClassByName(className));
    }

    public String getActualClazzName() {
        return clazz.getClass().getSimpleName();
    }
}
