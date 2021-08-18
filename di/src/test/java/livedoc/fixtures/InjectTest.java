package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import livedoc.fixtures.objects.Bar;

@FuShengTest
public class InjectTest extends BaseFixture {
    private Bar clazz;

    public void getInstanceFromContainer(String className) throws ClassNotFoundException {
        clazz = (Bar) getInstance(className);
    }

    public String getInstanceWithCircularDependency(String className) throws ClassNotFoundException {
        try {
            getInstance(className);
        } catch (Exception ex) {
            return ex.getClass().getSimpleName();
        }
        return "no exception";
    }

    public String tryGetDependencyNameInBar() {
        return clazz.getFoo().getClass().getSimpleName();
    }
}
