package livedoc.fixtures;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class InjectTest extends BaseFixture {

    public String getInstanceWithCircularDependency(String className) throws ClassNotFoundException {
        try {
            getInstance(className);
        } catch (Exception ex) {
            return ex.getClass().getSimpleName();
        }
        return "no exception";
    }

}
