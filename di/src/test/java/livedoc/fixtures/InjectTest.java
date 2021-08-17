package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class InjectTest extends BaseFixture {
    private Bar clazz;

    public void getInstanceFromContainer(String className) throws ClassNotFoundException {
        clazz = (Bar) getInstance(className);
    }

    public String tryGetDependencyNameInBar() {
        return clazz.getFoo().getClass().getSimpleName();
    }
}
