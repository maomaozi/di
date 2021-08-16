package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class ContainerTest extends BaseFixture {

    private Object clazz;

    public String tryGetUnRegisteredClass(String className) {
        try {
            getInstance(className);
        } catch (Exception ex) {
            return ex.getClass().getSimpleName();
        }

        return "no exception";
    }

    public void getInstanceFromContainer(String className) throws ClassNotFoundException {
        clazz = getInstance(className);
    }

    public String getActualClazzName() {
        return clazz.getClass().getSimpleName();
    }
}
